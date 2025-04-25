package ptithcm.itmc.taskracer.controller.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamRequest {
    private String name;
    private Visibility visibility;
}
