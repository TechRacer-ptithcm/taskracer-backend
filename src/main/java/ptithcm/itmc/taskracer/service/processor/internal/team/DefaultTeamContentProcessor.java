package ptithcm.itmc.taskracer.service.processor.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTeamContentRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamContentServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamContentProcessor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TEAM-CONTENT-PROCESSOR")
public class DefaultTeamContentProcessor implements ITeamContentProcessor {
    private final JpaTeamContentRepository jpaTeamContentRepository;
    private final JpaTeamRepository jpaTeamRepository;
    private final TeamContentServiceMapper mapper;
    @Override
    public JpaTeamContent save(TeamContentDto content) {
        if(!jpaTeamRepository.existsById(content.getTeamId()))
        {
            throw new ResourceNotFound("team id not found");
        }
        var formatData = mapper.toJpa(content);
        formatData.setLikeCount(0);
        if(formatData.getFileAttachment() != null)
        {
            formatData.getFileAttachment().forEach(file -> file.setContentId(formatData));
        }
        return jpaTeamContentRepository.save(formatData);
    }

    @Override
    public JpaTeamContent update(TeamContentDto content) {
        var getData = jpaTeamContentRepository.findById(content.getId());
        if(getData.isEmpty())
        {
            throw new ResourceNotFound("content id not found");
        }
        var data = mapper.merge(getData.get(), mapper.toJpa(content));
        return jpaTeamContentRepository.save(data);
    }

    @Override
    public void delete(UUID contentId) {
        var getData = jpaTeamContentRepository.findById(contentId);
        if(getData.isEmpty())
        {
            throw new ResourceNotFound("content id not found");
        }
        jpaTeamContentRepository.delete(getData.get());
    }
}
