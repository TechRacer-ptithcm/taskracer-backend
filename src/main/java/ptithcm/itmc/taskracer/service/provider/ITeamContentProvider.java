package ptithcm.itmc.taskracer.service.provider;

import org.springframework.data.domain.Pageable;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;

import java.util.List;
import java.util.UUID;

public interface ITeamContentProvider {
    PageableObject<List<JpaTeamContent>> getAll(Pageable pageable, Integer teamId);

    JpaTeamContent getById(UUID contentId);
}
