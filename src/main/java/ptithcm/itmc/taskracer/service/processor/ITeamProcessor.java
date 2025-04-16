package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

import java.util.UUID;

public interface ITeamProcessor {
    TeamDto createNewTeam(TeamDto teamDto, UUID ownerId);

    TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId);

    void deleteTeam(String slug, UUID userId);

    void removeUserFromTeam(String slug, UUID userId, UUID removedUserId);

    void inviteUserToTeam(String slug, UUID userId, UUID invitedUserId);

    void acceptInvite(String slug, UUID userId);

    void rejectInvite(String slug, UUID userId);

    void leaveTeam(String slug, UUID userId);

    void joinTeam(String slug, UUID userId);

    void requestToJoinTeam(String slug, UUID userId);
}
