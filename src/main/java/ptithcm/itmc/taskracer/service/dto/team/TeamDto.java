package ptithcm.itmc.taskracer.service.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
    private Integer id;
    private String slug;
    private String name;
    private UUID ownerId;
    private Visibility visibility;
    private Set<UUID> users;

}
