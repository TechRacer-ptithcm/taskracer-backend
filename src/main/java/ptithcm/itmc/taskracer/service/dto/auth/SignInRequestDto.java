package ptithcm.itmc.taskracer.service.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequestDto {
    private String inputAccount; // maybe username or email
    private String password;
}
