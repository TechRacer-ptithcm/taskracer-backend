package ptithcm.itmc.taskracer.service.process.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaOtpRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
@Deprecated
public class CleanExpiredOtp {
    private final JpaOtpRepository jpaOtpRepository;

    //    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void clean() {
        jpaOtpRepository.deleteByExpireAtBefore(LocalDateTime.now());
        log.info("Clean otp at: {}", LocalDateTime.now());
    }
}
