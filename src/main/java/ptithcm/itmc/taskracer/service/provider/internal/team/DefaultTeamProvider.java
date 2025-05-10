package ptithcm.itmc.taskracer.service.provider.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaRoleRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamInviteHistoryRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamMemberRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.repository.model.JpaTeamMember;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;
import ptithcm.itmc.taskracer.service.mapper.team.TeamInviteHistoryServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamMemberServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;
import ptithcm.itmc.taskracer.service.provider.ITeamProvider;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TEAM-PROVIDER")
public class DefaultTeamProvider implements ITeamProvider {
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaTeamMemberRepository jpaTeamMemberRepository;
    private final JpaTeamInviteHistoryRepository jpaTeamInviteHistoryRepository;
    private final TeamServiceMapper teamServiceMapper;
    private final TeamInviteHistoryServiceMapper teamInviteHistoryServiceMapper;
    private final TeamMemberServiceMapper teamMemberServiceMapper;
    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public JpaTeam getTeamBySlug(String slug) {
        return jpaTeamRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFound("Team slug not found."));
    }

    @Override
    public PageableObject<List<JpaTeam>> getAllTeam(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var data = jpaTeamRepository.findAllByVisibility(pageable, Visibility.PUBLIC);
        return PageableObject.<List<JpaTeam>>builder()
                .content(data.getContent())
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .currentPage(data.getNumber())
                .build();
    }

    @Override
    public PageableObject<List<JpaTeam>> getJoinTeams(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var getData = jpaTeamMemberRepository.findAllByUserId(userId, pageable);
        return PageableObject.<List<JpaTeam>>builder()
                .currentPage(getData.getNumber())
                .totalPage(getData.getTotalPages())
                .totalElements(getData.getTotalElements())
                .content(getData.getContent()
                        .stream()
                        .map(JpaTeamMember::getTeam)
                        .toList()
                )
                .build();
    }
}
