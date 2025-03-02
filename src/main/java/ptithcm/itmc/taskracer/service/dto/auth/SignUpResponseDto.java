package ptithcm.itmc.taskracer.service.dto.auth;

import lombok.Builder;
import lombok.Data;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

@Builder
@Data
public class SignUpResponseDto {
    private String username;
    private String email;
    private Boolean active;
}
