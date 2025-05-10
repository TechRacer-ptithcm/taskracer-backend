package ptithcm.itmc.taskracer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamMemberProcessor;
import ptithcm.itmc.taskracer.service.processor.ITeamProcessor;
import ptithcm.itmc.taskracer.service.provider.ITeamProvider;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final ITeamProcessor processor;
    private final ITeamProvider provider;
    private final ITeamMemberProcessor memberProcessor;
    private final TeamServiceMapper mapper;

    @Transactional
    public TeamDto createNewTeam(TeamDto teamDto, UUID ownerId) {
        var data = processor.create(teamDto, ownerId);
        return mapper.toDto(data);
    }

    public TeamDto getTeamBySlug(String slug) {
        var data = provider.getTeamBySlug(slug);
        return mapper.toDto(data);
    }

    public PageableObject<List<TeamDto>> getAllTeam(int page, int size) {
        return PageableObject.<List<TeamDto>>builder()
                .content(mapper.toDto(provider.getAllTeam(page, size).getContent()))
                .totalElements(provider.getAllTeam(page, size).getTotalElements())
                .totalPage(provider.getAllTeam(page, size).getTotalPage())
                .currentPage(provider.getAllTeam(page, size).getCurrentPage())
                .build();
    }

    @Transactional
    public TeamDto updateTeam(String slug, TeamDto teamDto, UUID userId) {
        var data = processor.update(slug, teamDto, userId);
        return mapper.toDto(data);
    }

    @Transactional
    public void deleteTeam(String slug, UUID userId) {
        processor.delete(slug, userId);
    }

    @Transactional
    public void removeUserFromTeam(String slug, UUID userId, UUID removedUserId) {
        memberProcessor.remove(slug, userId, removedUserId);
    }

    @Transactional
    public void inviteUserToTeam(String slug, UUID userId, UUID invitedUserId) {
        memberProcessor.invite(slug, userId, invitedUserId);
    }

    @Transactional
    public void acceptInvite(String slug, UUID userId) {
        memberProcessor.accept(slug, userId);
    }

    @Transactional
    public void rejectInvite(String slug, UUID userId) {
        memberProcessor.reject(slug, userId);
    }

    @Transactional
    public void leaveTeam(String slug, UUID userId) {
        memberProcessor.leave(slug, userId);
    }

    @Transactional
    public void joinTeam(String slug, UUID userId) {
        memberProcessor.join(slug, userId);
    }

    @Transactional
    public void requestToJoinTeam(String slug, UUID userId) {
        memberProcessor.requestToJoin(slug, userId);
    }

    @Transactional
    public void acceptRequest(String slug, UUID userId, UUID requestUserId) {
        memberProcessor.acceptRequest(slug, userId, requestUserId);
    }

    public PageableObject<List<TeamDto>> getAllTeamJoin(UUID userId, int page, int size) {
        return PageableObject.<List<TeamDto>>builder()
                .content(mapper.toDto(provider.getJoinTeams(userId, page, size).getContent()))
                .totalElements(provider.getAllTeam(page, size).getTotalElements())
                .totalPage(provider.getAllTeam(page, size).getTotalPage())
                .currentPage(provider.getAllTeam(page, size).getCurrentPage())
                .build();
    }
}
