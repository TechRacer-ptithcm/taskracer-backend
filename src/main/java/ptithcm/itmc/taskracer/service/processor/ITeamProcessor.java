package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

import java.util.UUID;

public interface ITeamProcessor {
    JpaTeam create(TeamDto teamDto, UUID ownerId);

    JpaTeam update(String slug, TeamDto teamDto, UUID userId);

    void delete(String slug, UUID userId);
}
