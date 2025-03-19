package ptithcm.itmc.taskracer.controller.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.common.annotation.email.ValidateEmail;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @ValidateEmail
    @NotNull
    @NotBlank
    private String email;
}
