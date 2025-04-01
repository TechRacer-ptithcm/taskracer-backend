package ptithcm.itmc.taskracer.controller.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotNull
    @NotBlank
    @JsonProperty("account")
    private String inputAccount; // maybe username or email

    @NotNull
    @NotBlank
    private String password;
}
