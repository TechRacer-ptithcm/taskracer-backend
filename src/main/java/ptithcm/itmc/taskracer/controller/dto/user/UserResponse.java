package ptithcm.itmc.taskracer.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private Tier tier;
    private Integer streak;
    private String username;
    private String email;
    private Boolean active;
    private String name;
    private Gender gender;
    private LocalDateTime birth;
}
