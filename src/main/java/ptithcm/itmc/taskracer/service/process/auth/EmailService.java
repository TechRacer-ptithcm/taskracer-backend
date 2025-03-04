package ptithcm.itmc.taskracer.service.process.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.repository.JpaOtpRepository;
import ptithcm.itmc.taskracer.service.dto.auth.OtpCodeDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.auth.AuthServiceMapper;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JpaOtpRepository jpaOtpRepository;
    private final AuthServiceMapper authServiceMapper;
    private final JavaMailSender javaMailSender;

    private String generateOtpCode() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger minValue = new BigInteger("100000");
        BigInteger maxValue = new BigInteger("999999");
        BigInteger range = maxValue.subtract(minValue).add(BigInteger.ONE);
        BigInteger otpCode = new BigInteger(range.bitLength(), secureRandom).mod(range).add(minValue);
        return otpCode.toString();
    }

    @Async
    public void sendOtp(UserDto userData) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String otpCode = generateOtpCode();
        helper.setTo(userData.getEmail());
        helper.setSubject("OTP Code");
        helper.setText("Your active code: " + otpCode + ". This code will be expired in 5 minutes", true);
        log.info("Data email: {}", helper.getMimeMessage());
        javaMailSender.send(helper.getMimeMessage());
        OtpCodeDto otp = OtpCodeDto.builder()
                .userId(userData.getId())
                .otp(otpCode)
                .expireAt(java.time.LocalDateTime.now().plusMinutes(5))
                .build();
        jpaOtpRepository.save(authServiceMapper.toJpaOtp(otp));
    }
}
