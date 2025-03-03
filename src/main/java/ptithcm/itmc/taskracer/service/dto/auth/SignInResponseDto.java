package ptithcm.itmc.taskracer.service.dto.auth;

import lombok.Builder;
import lombok.Data;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

import java.util.UUID;

@Data
@Builder
public class SignInResponseDto {
    private UUID id;
    private String username;
    private String email;
    private boolean active;
    private Tier tier;
    private String accessToken;
}
