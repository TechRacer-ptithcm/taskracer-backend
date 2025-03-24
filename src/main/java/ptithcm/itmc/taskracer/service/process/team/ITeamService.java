package ptithcm.itmc.taskracer.service.process.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

public interface ITeamService {
    TeamDto createNewTeam(TeamDto teamDto);
}

@Service
@Slf4j
@RequiredArgsConstructor
class TeamServiceProcessor implements ITeamService {
    private final JpaTeamRepository jpaTeamRepository;

    @Override
    public TeamDto createNewTeam(TeamDto teamDto) {
        return null;
    }
}
