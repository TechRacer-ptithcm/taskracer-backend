package ptithcm.itmc.taskracer.service.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.InviteStatus;
import ptithcm.itmc.taskracer.repository.model.enumeration.InviteStatusType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamInviteHistoryDto {
    private UUID user;
    private Integer team;
    private InviteStatus status;
    private InviteStatusType type;
}
