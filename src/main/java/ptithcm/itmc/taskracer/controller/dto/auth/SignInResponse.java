package ptithcm.itmc.taskracer.controller.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

import java.util.UUID;

@Data
@Builder
public class SignInResponse {
    private UUID id;
    private String username;
    private String email;
    private boolean active;
    private Tier tier;
    @JsonProperty("access_token")
    private String accessToken;
}
