package ptithcm.itmc.taskracer.service.process.auth;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ExpiredException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder passwordEncoder;
    private final TierMapper tierMapper;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    //    private final JpaOtpRepository jpaOtpRepository;
    private final AesTokenUtil aesTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;


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
                .score(0)
                .name("")
                .build();
        log.info("Create new user: {}", userServiceMapper.toJpaUserDto(user));
        var savedUser = jpaUserRepository.save(userServiceMapper.toJpaUserDto(user));
        emailService.sendOtp(userServiceMapper.toUserDto(savedUser));
        return SignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .active(savedUser.getActive())
                .build();
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        Long expiredTime = TimeUnit.DAYS.toMillis(1);
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
                .tier(tierMapper.toTierDto(user.getTier()).getName())
                .accessToken(jwtUtil.generateToken(userServiceMapper.toUserDto(user), expiredTime))
                .build();
    }

    @Transactional
    public void verifyAccount(String otp) {
        if (!redisTemplate.hasKey("otp:" + otp)) throw new ExpiredException("OTP is not found or already used.");
        String getUsername = (String) redisTemplate.opsForValue().get(otp);
        redisTemplate.delete(otp);
        jpaUserRepository.findByUsername(getUsername)
                .ifPresent(user -> user.setActive(true));
    }

    @Transactional
    public void sendOtpForgotPassword(String account) throws MessagingException {
        var user = jpaUserRepository.findByEmail(account)
                .or(() -> jpaUserRepository.findByUsername(account))
                .orElseThrow(() -> new ResourceNotFound("Email or username not found."));
        emailService.sendOtp(userServiceMapper.toUserDto(user));
    }

    public OtpForgotPasswordDto VerifyChangePassword(String otp) throws Exception {
        if (!redisTemplate.hasKey("otp:" + otp)) throw new ExpiredException("OTP is not found or already used.");
        String getUsername = (String) redisTemplate.opsForValue().get("otp:" + otp);
//        var otpData = jpaOtpRepository.findByOtp(otp)
//                .filter(object -> object.getExpireAt().isAfter(LocalDateTime.now()))
//                .orElseThrow(() -> new ExpiredException("OTP is not found or already used."));
        var userData = jpaUserRepository.findByUsername(getUsername)
                .orElseThrow(() -> new ResourceNotFound("User not found."));
        var expiredTime = LocalDateTime.now()
                .plusMinutes(5)
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

    @Transactional
    public void changePassword(String token, String newPassword) throws Exception {
        String[] resultData = aesTokenUtil.decrypt(token);
        var userData = jpaUserRepository.findByUsername(resultData[0])
                .or(() -> jpaUserRepository.findByEmail(resultData[1]))
                .orElseThrow(() -> new ResourceNotFound("User not found."));
        userData.setPassword(passwordEncoder.encode(newPassword));
        jpaUserRepository.save(userData);
    }
}