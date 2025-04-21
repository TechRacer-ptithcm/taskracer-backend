package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

import java.util.UUID;

public interface ITeamProcessor {
    JpaTeam create(TeamDto teamDto, UUID ownerId);

    JpaTeam update(String slug, TeamDto teamDto, UUID userId);

    void delete(String slug, UUID userId);

    void removeUser(String slug, UUID userId, UUID removedUserId);

    void inviteUser(String slug, UUID userId, UUID invitedUserId);

    void acceptInvite(String slug, UUID userId);

    void rejectInvite(String slug, UUID userId);

    void leaveTeam(String slug, UUID userId);

    void joinTeam(String slug, UUID userId);

    void requestToJoinTeam(String slug, UUID userId);
}
