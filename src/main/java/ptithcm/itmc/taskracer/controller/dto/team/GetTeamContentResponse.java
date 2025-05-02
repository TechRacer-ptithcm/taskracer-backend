package ptithcm.itmc.taskracer.controller.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTeamContentResponse {
    private UUID id;

    private UserDto userId;

    private TeamDto teamId;

    private String content;

    private List<String> fileAttachmentUrl;

    private Integer likeCount;
}
