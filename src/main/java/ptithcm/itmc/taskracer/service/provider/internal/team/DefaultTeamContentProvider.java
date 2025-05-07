package ptithcm.itmc.taskracer.service.provider.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTeamContentRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.provider.ITeamContentProvider;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TEAM-CONTENT-PROVIDER")
public class DefaultTeamContentProvider implements ITeamContentProvider {
    private final JpaTeamContentRepository teamContentRepository;
    @Override
    public PageableObject<List<JpaTeamContent>> getAll(Pageable pageable, Integer teamId) {
        var data = teamContentRepository.findAllByTeamId(teamId, pageable);
        return PageableObject.<List<JpaTeamContent>>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .content(data.getContent())
                .totalElements(data.getTotalElements())
                .build();
    }

    @Override
    public JpaTeamContent getById(UUID contentId) {
        var data = teamContentRepository.findById(contentId);
        if(data.isEmpty())
        {
            throw new ResourceNotFound("content not found");
        }
        return data.get();
    }
}
