package ptithcm.itmc.taskracer.controller.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignInRequest {
    @JsonProperty("account")
    private String inputAccount; // maybe username or email
    private String password;
}
