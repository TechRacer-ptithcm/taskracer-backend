package ptithcm.itmc.taskracer.service.process.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.RoleInsufficientException;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;

import java.util.List;
import java.util.UUID;

public interface ITeamService {
    TeamDto createNewTeam(TeamDto teamDto, UUID ownerId);

    TeamDto getTeamBySlug(String slug);

    PageableObject<List<TeamDto>> getAllTeam(int page, int size);

    TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId);

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
        var data = jpaTeamRepository.save(teamServiceMapper.toJpa(teamDto));
        return teamServiceMapper.toDto(data);
    }

    @Override
    public TeamDto getTeamBySlug(String slug) {
        var data = jpaTeamRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFound("Team slug not found."));
        return teamServiceMapper.toDto(data);
    }

    @Override
    public PageableObject<List<TeamDto>> getAllTeam(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var data = jpaTeamRepository.findAllByVisibility(pageable, Visibility.PUBLIC);
        return PageableObject.<List<TeamDto>>builder()
                .content(teamServiceMapper.toDto(data.getContent()))
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .currentPage(data.getNumber())
                .build();
    }

    @Override
    public TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId) {
        var findTeam = jpaTeamRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFound("Team slug not found."));
        if (!findTeam.getOwner().getId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to update this team.");
        }
        return null;
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
