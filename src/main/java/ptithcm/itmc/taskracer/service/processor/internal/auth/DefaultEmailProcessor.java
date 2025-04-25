package ptithcm.itmc.taskracer.service.processor.internal.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.processor.IEmailProcessor;
import ptithcm.itmc.taskracer.util.jwt.CommonUtil;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-EMAIL-PROCESSOR")
public class DefaultEmailProcessor implements IEmailProcessor {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${task-racer.expire.minute}")
    private int expireTimeByMinute;

    @Override
    @Async
    public void sendOtp(UserDto userData) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String otpCode = CommonUtil.generateOtpCode();
        redisTemplate.opsForValue().set("otp:" + otpCode, userData.getUsername(),
                expireTimeByMinute,
                TimeUnit.of(java.time.temporal.ChronoUnit.MINUTES));
        helper.setTo(userData.getEmail());
        helper.setSubject("OTP Code");
        helper.setText("Your code: " + otpCode + ". This code will be expired in 5 minutes", true);
        log.info("Data email: {}", helper.getMimeMessage());
        javaMailSender.send(helper.getMimeMessage());
    }
}
