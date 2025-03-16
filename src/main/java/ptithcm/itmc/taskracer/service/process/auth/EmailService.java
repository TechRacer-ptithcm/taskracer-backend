package ptithcm.itmc.taskracer.service.process.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.util.jwt.CommonUtil;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    //    private final JpaOtpRepository jpaOtpRepository;
//    private final AuthServiceMapper authServiceMapper;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    @Async
    public void sendOtp(UserDto userData) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String otpCode = CommonUtil.generateOtpCode();
        redisTemplate.opsForValue().set("otp:" + otpCode, userData.getUsername(),
                5,
                TimeUnit.of(java.time.temporal.ChronoUnit.MINUTES));
        helper.setTo(userData.getEmail());
        helper.setSubject("OTP Code");
        helper.setText("Your code: " + otpCode + ". This code will be expired in 5 minutes", true);
        log.info("Data email: {}", helper.getMimeMessage());
        javaMailSender.send(helper.getMimeMessage());
//        OtpCodeDto otp = OtpCodeDto.builder()
//                .userId(userData.getId())
//                .otp(otpCode)
//                .expireAt(java.time.LocalDateTime.now().plusMinutes(5))
//                .build();
//        jpaOtpRepository.save(authServiceMapper.toJpaOtp(otp));
    }

    public Optional<UserDto> getUserFromOtp(String otp) throws Exception {
        String key = "otp:" + otp;
        if (!redisTemplate.hasKey(key)) throw new Exception("OTP is not found or already used.");
        String getUsername = (String) redisTemplate.opsForValue().get(key);
        var userData = jpaUserRepository.findByUsername(getUsername).orElseThrow(() -> new Exception("User not found."));
        return Optional.of(userServiceMapper.toUserDto(userData));
    }
}
