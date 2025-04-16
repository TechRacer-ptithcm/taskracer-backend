package ptithcm.itmc.taskracer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.RoleInsufficientException;
import ptithcm.itmc.taskracer.repository.JpaRoleRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamInviteHistoryRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamMemberRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.InviteStatus;
import ptithcm.itmc.taskracer.repository.model.enumeration.Role;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamInviteHistoryDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamMemberDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamInviteHistoryServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamMemberServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;

import java.util.List;
import java.util.UUID;

public interface ITeamService {
    TeamDto createNewTeam(TeamDto teamDto, UUID ownerId);

    TeamDto getTeamBySlug(String slug);

    PageableObject<List<TeamDto>> getAllTeam(int page, int size);

    TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId);

    void deleteTeam(String slug, UUID userId);

    void removeUserFromTeam(String slug, UUID userId, UUID removedUserId);

    void inviteUserToTeam(String slug, UUID userId, UUID invitedUserId);

    void acceptInvite(String slug, UUID userId);

    void rejectInvite(String slug, UUID userId);

    void leaveTeam(String slug, UUID userId);

    void joinTeam(String slug, UUID userId);

    void requestToJoinTeam(String slug, UUID userId);
}

@Service
@Slf4j
@RequiredArgsConstructor
class TeamServiceProcessor implements ITeamService {
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaTeamMemberRepository jpaTeamMemberRepository;
    private final JpaTeamInviteHistoryRepository jpaTeamInviteHistoryRepository;
    private final TeamServiceMapper teamServiceMapper;
    private final TeamInviteHistoryServiceMapper teamInviteHistoryServiceMapper;
    private final TeamMemberServiceMapper teamMemberServiceMapper;
    private final JpaRoleRepository jpaRoleRepository;

    @Override
    @Transactional
    public TeamDto createNewTeam(TeamDto teamDto, UUID ownerId) {
        if (jpaTeamRepository.findBySlug(teamDto.getSlug()).isPresent()) {
            throw new DuplicateDataException("Slug already exists.");
        }
        if (teamDto.getSlug() == null || teamDto.getSlug().isEmpty()) {
            String teamName = teamDto.getName()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", "")
                    .replaceAll("\\s+", "-");
            teamDto.setSlug(teamName + "-" + UUID.randomUUID().toString().substring(0, 8));
        } else {
            teamDto.setSlug(teamDto.getSlug() + "-" + UUID.randomUUID().toString().substring(0, 8));
        }
        teamDto.setOwnerId(ownerId);
        var data = jpaTeamRepository.save(teamServiceMapper.toJpa(teamDto));
        var member = TeamMemberDto.builder()
                .userId(ownerId)
                .teamId(data.getId())
                .build();
        var teamOwner = teamMemberServiceMapper.toJpa(member);
        var role = jpaRoleRepository.findByName(Role.OWNER);
        log.info("role: {}", Role.OWNER);
        teamOwner.setRole(role);
        jpaTeamMemberRepository.save(teamOwner);
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
    @Transactional
    public TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to update this team.");
        }
        var mergeData = teamServiceMapper.merge(findTeam, teamDto);
        var updateData = jpaTeamRepository.save(teamServiceMapper.toJpa(mergeData));
        return teamServiceMapper.toDto(updateData);
    }

    @Override
    @Transactional
    public void deleteTeam(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to delete this team.");
        }
        jpaTeamRepository.deleteBySlug(slug);
    }

    @Override
    @Transactional
    public void removeUserFromTeam(String slug, UUID userId, UUID removedUserId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to remove user from this team.");
        }
        jpaTeamMemberRepository.deleteByTeamIdAndUserId(findTeam.getId(), removedUserId);
    }

    @Override
    @Transactional
    public void inviteUserToTeam(String slug, UUID userId, UUID invitedUserId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));

        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserId(findTeam.getId(), invitedUserId);
        if (findInvite.isPresent()) {
            throw new DuplicateDataException("User has been invited to this team.");
        }

        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to invite user to this team.");
        }
        var dataToSave = TeamInviteHistoryDto.builder()
                .user(invitedUserId)
                .team(findTeam.getId())
                .status(InviteStatus.PENDING)
                .build();
        jpaTeamInviteHistoryRepository.save(teamInviteHistoryServiceMapper.toJpa(dataToSave));
    }

    @Override
    @Transactional
    public void acceptInvite(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserId(findTeam.getId(), userId)
                .orElseThrow(() -> new RoleInsufficientException("You have not been invited to this team."));
        findInvite.setStatus(InviteStatus.ACCEPTED);
        var dataToSaveDto = TeamMemberDto.builder()
                .userId(userId)
                .teamId(findTeam.getId())
                .build();
        var dataToSave = teamMemberServiceMapper.toJpa(dataToSaveDto);
        dataToSave.setRole(jpaRoleRepository.findByName(Role.MEMBER));
        jpaTeamMemberRepository.save(dataToSave);
        jpaTeamInviteHistoryRepository.save(findInvite);
    }

    @Override
    @Transactional
    public void rejectInvite(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserId(findTeam.getId(), userId)
                .orElseThrow(() -> new RoleInsufficientException("You have not been invited to this team."));
        findInvite.setStatus(InviteStatus.REJECTED);
        jpaTeamInviteHistoryRepository.save(findInvite);
    }

    @Override
    @Transactional
    public void leaveTeam(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var existingUser = jpaTeamMemberRepository.findByUserIdAndTeamId(userId, findTeam.getId());
        if (existingUser.isEmpty()) {
            throw new RoleInsufficientException("You are not a member of this team.");
        }
        jpaTeamMemberRepository.deleteByTeamIdAndUserId(findTeam.getId(), userId);
    }

    @Override
    @Transactional
    public void joinTeam(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var existingUser = jpaTeamMemberRepository.findByUserIdAndTeamId(userId, findTeam.getId());
        if (existingUser.isPresent()) {
            throw new DuplicateDataException("You are already a member of this team.");
        }
        if (findTeam.getVisibility() == Visibility.PRIVATE) {
            throw new RoleInsufficientException("This team is private.");
        }
        if (jpaTeamMemberRepository.findByUserIdAndTeamId(userId, findTeam.getId()).isPresent()) {
            throw new DuplicateDataException("You are already a member of this team.");
        }
        var dataToSaveDto = TeamMemberDto.builder()
                .userId(userId)
                .teamId(findTeam.getId())
                .build();
        var dataToSave = teamMemberServiceMapper.toJpa(dataToSaveDto);
        dataToSave.setRole(jpaRoleRepository.findByName(Role.MEMBER));
        jpaTeamMemberRepository.save(dataToSave);
    }

    @Override
    @Transactional
    public void requestToJoinTeam(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var existingUser = jpaTeamMemberRepository.findByUserIdAndTeamId(userId, findTeam.getId());
        if (existingUser.isPresent()) {
            throw new DuplicateDataException("You are already a member of this team.");
        }
        var dataToSave = TeamInviteHistoryDto.builder()
                .user(userId)
                .team(findTeam.getId())
                .status(InviteStatus.PENDING)
                .build();
        jpaTeamInviteHistoryRepository.save(teamInviteHistoryServiceMapper.toJpa(dataToSave));
    }
}
