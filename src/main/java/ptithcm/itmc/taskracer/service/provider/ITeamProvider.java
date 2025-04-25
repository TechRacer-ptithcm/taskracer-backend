package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;

import java.util.List;

public interface ITeamProvider {
    JpaTeam getTeamBySlug(String slug);

    PageableObject<List<JpaTeam>> getAllTeam(int page, int size);
}
