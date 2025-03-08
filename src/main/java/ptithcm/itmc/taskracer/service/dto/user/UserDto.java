package ptithcm.itmc.taskracer.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
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
