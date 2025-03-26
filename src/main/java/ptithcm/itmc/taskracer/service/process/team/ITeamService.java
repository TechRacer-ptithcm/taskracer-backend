package ptithcm.itmc.taskracer.service.process.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;

import java.util.List;
import java.util.UUID;

public interface ITeamService {
    TeamDto createNewTeam(TeamDto teamDto, UUID ownerId);

    TeamDto getTeamBySlug(String slug);

    List<TeamDto> getAllTeam();

    void updateTeam(String slug, TeamDto teamDto, UUID userId);

    void deleteTeam(String slug, UUID userId);

    void removeUserFromTeam(String slug, UUID userId);

    void addTaskToTeam(String slug, UUID userId);

    void inviteUserToTeam(String slug, UUID userId);

    void acceptInvite(String slug, UUID userId);

    void rejectInvite(String slug, UUID userId);

    void leaveTeam(String slug, UUID userId);

    void joinTeam(String slug, UUID userId);

    void acceptRequestJoinTeam(String slug, UUID userId);

}

@Service
@Slf4j
@RequiredArgsConstructor
class TeamServiceProcessor implements ITeamService {
    private final JpaTeamRepository jpaTeamRepository;
    private final TeamServiceMapper teamServiceMapper;

    @Override
    public TeamDto createNewTeam(TeamDto teamDto, UUID ownerId) {
        if (jpaTeamRepository.findBySlug(teamDto.getSlug()).isPresent()) {
            throw new DuplicateDataException("Slug already exists.");
        }
        teamDto.setOwnerId(ownerId);
        teamDto.getUsers().add(ownerId);
        var data = jpaTeamRepository.save(teamServiceMapper.toJpaTeam(teamDto));
        return teamServiceMapper.toTeamDto(data);
    }

    @Override
    public TeamDto getTeamBySlug(String slug) {
        return null;
    }

    @Override
    public List<TeamDto> getAllTeam() {
        return List.of();
    }

    @Override
    public void updateTeam(String slug, TeamDto teamDto, UUID userId) {

    }

    @Override
    public void deleteTeam(String slug, UUID userId) {

    }

    @Override
    public void removeUserFromTeam(String slug, UUID userId) {

    }

    @Override
    public void addTaskToTeam(String slug, UUID userId) {

    }

    @Override
    public void inviteUserToTeam(String slug, UUID userId) {

    }

    @Override
    public void acceptInvite(String slug, UUID userId) {

    }

    @Override
    public void rejectInvite(String slug, UUID userId) {

    }

    @Override
    public void leaveTeam(String slug, UUID userId) {

    }

    @Override
    public void joinTeam(String slug, UUID userId) {

    }

    @Override
    public void acceptRequestJoinTeam(String slug, UUID userId) {

    }
}
