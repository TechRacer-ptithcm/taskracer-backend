package ptithcm.itmc.taskracer.service.provider;

import org.springframework.data.domain.Pageable;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;

import java.util.List;
import java.util.UUID;

public interface ITeamContentProvider {
    PageableObject<List<TeamContentDto>> getAll(Pageable pageable);

    TeamContentDto getById(UUID id);
}
