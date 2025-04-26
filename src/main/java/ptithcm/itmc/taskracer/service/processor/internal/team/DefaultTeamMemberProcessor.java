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
import ptithcm.itmc.taskracer.repository.model.enumeration.*;
import ptithcm.itmc.taskracer.service.dto.team.TeamInviteHistoryDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamMemberDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamInviteHistoryServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamMemberServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamMemberProcessor;
import ptithcm.itmc.taskracer.service.validator.IEligibilityRoleValidator;

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
    private final IEligibilityRoleValidator roleValidator;

    @Override
    public void remove(String slug, UUID userId, UUID removedUserId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        roleValidator.validate(userId, findTeam.getId(), Permission.TEAM_REMOVE_MEMBER);
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
        roleValidator.validate(userId, findTeam.getId(), Permission.TEAM_ADD_MEMBER);
        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserIdAndStatus(findTeam.getId(), invitedUserId, InviteStatus.PENDING);
        if (findInvite.isPresent()) {
            throw new DuplicateDataException("User has already requested to join this team.");
        }

        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to invite user to this team.");
        }
        var dataToSave = TeamInviteHistoryDto.builder()
                .user(invitedUserId)
                .team(findTeam.getId())
                .status(InviteStatus.PENDING)
                .type(InviteStatusType.INVITE)
                .build();
        jpaTeamInviteHistoryRepository.save(teamInviteHistoryServiceMapper.toJpa(dataToSave));
    }

    @Override
    public void accept(String slug, UUID userId) { // description: Accept invitation
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserIdAndStatus(findTeam.getId(), userId, InviteStatus.PENDING)
                .orElseThrow(() -> new RoleInsufficientException("You have not been invited to this team."));
        if(!findInvite.getUser().getId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to accept this invitation.");
        }
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
                .findByTeamIdAndUserIdAndStatus(findTeam.getId(), userId, InviteStatus.PENDING)
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
        var dataToSaveDto = TeamMemberDto.builder()
                .userId(userId)
                .teamId(findTeam.getId())
                .build();
        var logData = TeamInviteHistoryDto.builder()
                .user(userId)
                .team(findTeam.getId())
                .status(InviteStatus.ACCEPTED)
                .type(InviteStatusType.AUTOJOIN)
                .build();
        jpaTeamInviteHistoryRepository.save(teamInviteHistoryServiceMapper.toJpa(logData));
        var dataToSave = teamMemberServiceMapper.toJpa(dataToSaveDto);
        dataToSave.setRole(jpaRoleRepository.findByName(Role.MEMBER));
        jpaTeamMemberRepository.save(dataToSave);
    }

    @Override
    public void requestToJoin(String slug, UUID userId) {
        log.info("find team by slug: {}", slug);
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        log.info("Find invitation by user_id: {}", userId);
        jpaTeamMemberRepository.findByUserIdAndTeamId(userId, findTeam.getId())
                .ifPresent(teamMember -> {
                    throw new DuplicateDataException("You are already a member of this team.");
                });
        jpaTeamInviteHistoryRepository.findByTeamIdAndUserIdAndStatus(findTeam.getId(), userId, InviteStatus.PENDING)
                .ifPresent(invite -> {
                   throw new DuplicateDataException("You have already requested to join this team.");
        });
        var dataToSave = TeamInviteHistoryDto.builder()
                .user(userId)
                .team(findTeam.getId())
                .status(InviteStatus.PENDING)
                .type(InviteStatusType.REQUEST)
                .build();
        jpaTeamInviteHistoryRepository.save(teamInviteHistoryServiceMapper.toJpa(dataToSave));
    }

    @Override
    public void acceptRequest(String slug, UUID userId, UUID requestUserId) { // description: Accept request to join team
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        var findInvite = jpaTeamInviteHistoryRepository
                .findByTeamIdAndUserIdAndStatus(findTeam.getId(), requestUserId, InviteStatus.PENDING)
                .orElseThrow(() -> new RoleInsufficientException("Don't find any invitation."));
        roleValidator.validate(userId, findTeam.getId(), Permission.TEAM_ADD_MEMBER);
        findInvite.setStatus(InviteStatus.ACCEPTED);
        jpaTeamInviteHistoryRepository.save(findInvite);
        var dataToSaveDto = TeamMemberDto.builder()
                .userId(requestUserId)
                .teamId(findTeam.getId())
                .build();
        var dataToSave = teamMemberServiceMapper.toJpa(dataToSaveDto);
        dataToSave.setRole(jpaRoleRepository.findByName(Role.MEMBER));
        jpaTeamMemberRepository.save(dataToSave);
    }
}
