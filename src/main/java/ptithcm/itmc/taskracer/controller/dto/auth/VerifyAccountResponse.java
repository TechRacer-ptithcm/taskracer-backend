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
public class VerifyAccountResponse {
    private String message;
    @JsonProperty("access_token")
    private String accessToken;
}
