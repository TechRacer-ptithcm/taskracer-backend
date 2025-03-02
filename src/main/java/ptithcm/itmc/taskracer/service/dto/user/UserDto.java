package ptithcm.itmc.taskracer.service.dto.user;

import lombok.Builder;
import lombok.Data;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.dto.tier.TierDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Builder
@Data
public class UserDto {
    private UUID id;
    private Tier tier;
    private Integer score;
    private String username;
    private String password;
    private String email;
    private Boolean active;
    private String name;
    private Gender gender;
    private LocalDateTime birth;
}
