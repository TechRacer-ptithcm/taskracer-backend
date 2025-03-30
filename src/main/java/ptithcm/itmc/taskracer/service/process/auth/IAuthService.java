package ptithcm.itmc.taskracer.service.process.auth;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ExpiredException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.ValidationFailedException;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.dto.auth.*;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.util.jwt.AesTokenUtil;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public interface IAuthService {
    SignUpResponseDto createNewUser(SignUpRequestDto request) throws MessagingException;

    SignInResponseDto signIn(SignInRequestDto request);

    VerifyAccountDto verifyAccount(String otp);

    void sendOtpForgotPassword(String account) throws MessagingException;

    OtpForgotPasswordDto VerifyChangePassword(String otp) throws Exception;

    void resendOtp(String account) throws MessagingException;

    void changePassword(String token, String newPassword) throws Exception;

    String refreshAccessToken(String token);

}

@Service
@RequiredArgsConstructor
@Slf4j
class AuthServiceProcessor implements IAuthService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder passwordEncoder;
    private final TierMapper tierMapper;
    private final JwtUtil jwtUtil;
    private final IEmailService emailService;
    private final AesTokenUtil aesTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${task-racer.expire.hour}")
    private int expireTimeByHour;
    @Value("${task-racer.expire.minute}")
    private int expireTimeByMinute;

    @Override
    @Transactional
    public SignUpResponseDto createNewUser(SignUpRequestDto request) throws MessagingException {
        if (jpaUserRepository.findByUsername(request.getUsername()).isPresent() ||
                jpaUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateDataException("Username or email already exists.");
        }
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
        emailService.sendOtp(userServiceMapper.toDto(savedUser));
        return SignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .active(savedUser.getActive())
                .build();
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto request) {
        Long expiredTime = TimeUnit.HOURS.toMillis(expireTimeByHour);
        var user = jpaUserRepository.findByUsername(request.getInputAccount())
                .or(() -> jpaUserRepository.findByEmail(request.getInputAccount()))
                .orElseThrow(() -> new ResourceNotFound("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFound("Wrong password.");
        }
        return SignInResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .tier(tierMapper.toDto(user.getTier()).getName())
                .accessToken(jwtUtil.generateToken(user.getUsername(), expiredTime))
                .build();
    }

    @Override
    @Transactional
    public VerifyAccountDto verifyAccount(String otp) {
        Long expiredTime = TimeUnit.HOURS.toMillis(expireTimeByHour);
        String key = "otp:" + otp;
        if (!redisTemplate.hasKey(key)) throw new ExpiredException("OTP is not found or already used.");
        String getUsername = (String) (redisTemplate.opsForValue().getAndDelete(key));
        log.info("otp get username: {}", getUsername);
        var userData = jpaUserRepository.findByUsername(getUsername);
        if (userData.isEmpty()) throw new ResourceNotFound("User not found.");
        userData.ifPresent(jpaUser -> jpaUser.setActive(true));
        return VerifyAccountDto.builder()
                .message("Verify account successfully.")
                .accessToken(jwtUtil.generateToken(getUsername, expiredTime))
                .build();
    }

    @Override
    @Transactional
    public void sendOtpForgotPassword(String account) throws MessagingException {
        var user = jpaUserRepository.findByEmail(account)
                .or(() -> jpaUserRepository.findByUsername(account))
                .orElseThrow(() -> new ResourceNotFound("Email or username not found."));
        emailService.sendOtp(userServiceMapper.toDto(user));
    }

    @Override
    public OtpForgotPasswordDto VerifyChangePassword(String otp) throws Exception {
        String key = "otp:" + otp;
        var userData = emailService.getUserFromOtp(otp).orElseThrow(() -> new ExpiredException("OTP is not found or already used."));
        String getUsername = (String) redisTemplate.opsForValue().get(key);
        log.info(getUsername);
        var expiredTime = LocalDateTime.now()
                .plusMinutes(expireTimeByMinute)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        log.info("timestamp: {}", expiredTime);
        var token = aesTokenUtil.encrypt(userData.getUsername(), userData.getEmail(), expiredTime);
        var result = OtpForgotPasswordDto.builder()
                .privateToken(token)
                .build();
        log.info(token);
//        jpaOtpRepository.delete(otpData);
        return result;
    }


    @Override
    public void resendOtp(String account) throws MessagingException {
        var user = jpaUserRepository.findByEmail(account)
                .or(() -> jpaUserRepository.findByUsername(account))
                .orElseThrow(() -> new ResourceNotFound("Email or username not found."));
        emailService.sendOtp(userServiceMapper.toDto(user));
    }

    @Override
    @Transactional
    public void changePassword(String token, String newPassword) throws Exception {
        String[] resultData = aesTokenUtil.decrypt(token);
        var userData = jpaUserRepository.findByUsername(resultData[0])
                .or(() -> jpaUserRepository.findByEmail(resultData[1]))
                .orElseThrow(() -> new ResourceNotFound("User not found."));
        userData.setPassword(passwordEncoder.encode(newPassword));
        jpaUserRepository.save(userData);
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