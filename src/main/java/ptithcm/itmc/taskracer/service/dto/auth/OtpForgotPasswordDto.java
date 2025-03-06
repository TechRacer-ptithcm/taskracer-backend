package ptithcm.itmc.taskracer.service.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpForgotPasswordDto {
    private String privateToken;
}
