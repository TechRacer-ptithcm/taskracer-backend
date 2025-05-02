package ptithcm.itmc.taskracer.service.provider.internal.team;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.provider.ITeamContentProvider;

import java.util.List;
import java.util.UUID;

@Component
public class DefaultTeamContentProvider implements ITeamContentProvider {
    @Override
    public PageableObject<List<TeamContentDto>> getAll(Pageable pageable) {
        return PageableObject.<List<TeamContentDto>>builder()
                .content(List.of())
                .build();
    }

    @Override
    public TeamContentDto getById(UUID id) {
        return null;
    }
}
