package ptithcm.itmc.taskracer.service.processor.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.processor.ITeamContentProcessor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TEAM-CONTENT-PROCESSOR")
public class DefaultTeamContentProcessor implements ITeamContentProcessor {
    @Override
    public JpaTeamContent save(TeamContentDto content) {
        return null;
    }

    @Override
    public JpaTeamContent update(TeamContentDto content) {
        return null;
    }

    @Override
    public void delete(UUID id, UUID userId) {

    }
}
