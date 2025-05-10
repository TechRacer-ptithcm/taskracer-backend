package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;

import java.util.List;
import java.util.UUID;

public interface ITeamProvider {
    JpaTeam getTeamBySlug(String slug);

    PageableObject<List<JpaTeam>> getAllTeam(int page, int size);

    PageableObject<List<JpaTeam>> getJoinTeams(UUID userId, int page, int size);
}
