package ptithcm.itmc.taskracer.service.processor.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
import ptithcm.itmc.taskracer.service.dto.team.TeamInviteHistoryDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamMemberDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamInviteHistoryServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamMemberServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamMemberProcessor;

import java.util.UUID;

@Slf4j(topic = "TEAM-MEMBER-PROCESSOR")
@Component
@RequiredArgsConstructor
public class DefaultTeamMemberProcessor implements ITeamMemberProcessor {
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaTeamMemberRepository jpaTeamMemberRepository;
    private final JpaTeamInviteHistoryRepository jpaTeamInviteHistoryRepository;
    private final TeamServiceMapper teamServiceMapper;
    private final TeamInviteHistoryServiceMapper teamInviteHistoryServiceMapper;
    private final TeamMemberServiceMapper teamMemberServiceMapper;
    private final JpaRoleRepository jpaRoleRepository;
    @Override
    public void remove(String slug, UUID userId, UUID removedUserId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to remove user from this team.");
        }
        jpaTeamMemberRepository.deleteByTeamIdAndUserId(findTeam.getId(), removedUserId);
    }

    @Override
    public void invite(String slug, UUID userId, UUID invitedUserId) {
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
    public void accept(String slug, UUID userId) {
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
    public void reject(String slug, UUID userId) {
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
    public void leave(String slug, UUID userId) {
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
    public void join(String slug, UUID userId) {
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
    public void requestToJoin(String slug, UUID userId) {
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
