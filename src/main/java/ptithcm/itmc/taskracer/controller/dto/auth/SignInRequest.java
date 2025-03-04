package ptithcm.itmc.taskracer.controller.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @JsonProperty("account")
    private String inputAccount; // maybe username or email
    private String password;
}
