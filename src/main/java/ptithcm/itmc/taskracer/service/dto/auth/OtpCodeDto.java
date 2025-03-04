package ptithcm.itmc.taskracer.service.dto.auth;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OtpCodeDto {
    private Integer id;
    private String otp;
    private UUID userId;
    private LocalDateTime expireAt;
}
