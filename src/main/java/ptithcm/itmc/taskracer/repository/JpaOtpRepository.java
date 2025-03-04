package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaOtp;

public interface JpaOtpRepository extends JpaRepository<JpaOtp, Integer> {
}
