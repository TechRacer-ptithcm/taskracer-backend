package ptithcm.itmc.taskracer.service.dto.team;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamContentDto {
    private UUID id;

    private UserDto userId;

    private TeamDto teamId;

    private String content;

    private String fileAttachmentUrl;

    private Integer likeCount;
}
