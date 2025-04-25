package ptithcm.itmc.taskracer.service.processor.internal.team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.RoleInsufficientException;
import ptithcm.itmc.taskracer.repository.JpaRoleRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamMemberRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.repository.model.enumeration.Role;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamMemberDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamMemberServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.team.TeamServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamProcessor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TEAM-PROCESSOR")
public class DefaultTeamProcessor implements ITeamProcessor {
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaTeamMemberRepository jpaTeamMemberRepository;
    private final TeamServiceMapper teamServiceMapper;
    private final TeamMemberServiceMapper teamMemberServiceMapper;
    private final JpaRoleRepository jpaRoleRepository;
    @Override
    public JpaTeam create(TeamDto teamDto, UUID ownerId) {
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
        return data;
    }

    @Override
    public JpaTeam update(String slug, TeamDto teamDto, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to update this team.");
        }
        var mergeData = teamServiceMapper.merge(findTeam, teamDto);
        return jpaTeamRepository.save(teamServiceMapper.toJpa(mergeData));
    }

    @Override
    public void delete(String slug, UUID userId) {
        var findTeam = teamServiceMapper.toDto(jpaTeamRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound("Team slug not found.")));
        if (!findTeam.getOwnerId().equals(userId)) {
            throw new RoleInsufficientException("You are not allowed to delete this team.");
        }
        jpaTeamRepository.deleteBySlug(slug);
    }
}
