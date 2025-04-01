package ptithcm.itmc.taskracer.controller.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetTeamResponse {
    private String slug;

    private String name;

    private UUID owner;

    private Visibility visibility;

    private Set<UUID> users;

}
