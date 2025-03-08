package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaOtp;

import java.time.LocalDateTime;
import java.util.Optional;

@Deprecated
public interface JpaOtpRepository extends JpaRepository<JpaOtp, Integer> {
    Optional<JpaOtp> findByOtp(String otp);

    void deleteByExpireAtBefore(LocalDateTime expireAtBefore);
}
