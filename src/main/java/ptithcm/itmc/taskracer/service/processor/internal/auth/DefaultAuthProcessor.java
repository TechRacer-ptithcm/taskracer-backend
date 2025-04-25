package ptithcm.itmc.taskracer.service.processor.internal.auth;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.ValidationFailedException;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.dto.auth.*;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.processor.IAuthProcessor;
import ptithcm.itmc.taskracer.service.processor.IEmailProcessor;
import ptithcm.itmc.taskracer.util.jwt.AesTokenUtil;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-AUTH-PROCESSOR")
public class DefaultAuthProcessor implements IAuthProcessor {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IEmailProcessor emailProcessor;
    private final AesTokenUtil aesTokenUtil;
    @Value("${task-racer.expire.hour}")
    private int expireTimeByHour;
    @Value("${task-racer.expire.minute}")
    private int expireTimeByMinute;

    @Override
    @Transactional
    public JpaUser createNewUser(SignUpRequestDto request) throws MessagingException {
        var user = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .tier(Tier.USER)
                .active(false)
                .gender(Gender.MALE)
                .streak(0)
                .name("")
                .build();
        var savedUser = jpaUserRepository.save(userServiceMapper.toJpa(user));
        emailProcessor.sendOtp(userServiceMapper.toDto(savedUser));
        return savedUser;
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto request, UserDto user) {
        Long expiredTime = TimeUnit.HOURS.toMillis(expireTimeByHour);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFound("Wrong password.");
        }
        return SignInResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .tier(user.getTier())
                .accessToken(jwtUtil.generateToken(user.getUsername(), expiredTime))
                .build();
    }

    @Override
    @Transactional
    public VerifyAccountDto active(String username) {
        Long expiredTime = TimeUnit.HOURS.toMillis(expireTimeByHour);
        log.info("Get username: {}", username);
        var userData = jpaUserRepository.findByUsername(username);
        if (userData.isEmpty()) throw new ResourceNotFound("User not found.");
        userData.ifPresent(jpaUser -> jpaUser.setActive(true));
        return VerifyAccountDto.builder()
                .message("Verify account successfully.")
                .accessToken(jwtUtil.generateToken(username, expiredTime))
                .build();
    }

    @Override
    public void sendOtpForgotPassword(UserDto user) throws MessagingException {
        emailProcessor.sendOtp(user);
    }

    @Override
    public OtpForgotPasswordDto VerifyChangePassword(UserDto user) throws Exception {
        var expiredTime = LocalDateTime.now()
                .plusMinutes(expireTimeByMinute)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        log.info("timestamp: {}", expiredTime);
        var token = aesTokenUtil.encrypt(user.getUsername(), user.getEmail(), expiredTime);
        var result = OtpForgotPasswordDto.builder()
                .privateToken(token)
                .build();
        log.info(token);
        return result;
    }

    @Override
    public void resendOtp(UserDto user) throws MessagingException {
        emailProcessor.sendOtp(user);
    }

    @Override
    public void changePassword(String token, String newPassword) throws Exception {
        String[] resultData = aesTokenUtil.decrypt(token);
        var user = jpaUserRepository.findByUsername(resultData[0])
                .or(() -> jpaUserRepository.findByEmail(resultData[1]))
                .orElseThrow(() -> new ResourceNotFound("User not found."));
        user.setPassword(passwordEncoder.encode(newPassword));
        jpaUserRepository.save(user);
    }

    @Override
    public String refreshAccessToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new ValidationFailedException("Invalid refresh token.");
        }
        var username = jwtUtil.extractUsername(token);
        jpaUserRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("User not found."));
        return jwtUtil.generateToken(username, TimeUnit.HOURS.toMillis(expireTimeByHour));
    }
}
