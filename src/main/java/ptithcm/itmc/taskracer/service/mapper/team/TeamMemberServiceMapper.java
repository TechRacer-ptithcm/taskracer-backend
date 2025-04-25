package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaTeamMember;
import ptithcm.itmc.taskracer.service.dto.team.TeamMemberDto;

@Mapper(componentModel = "spring")
public interface TeamMemberServiceMapper {
    @Mapping(target = "team.id", source = "teamId")
    @Mapping(target = "user.id", source = "userId")
    JpaTeamMember toJpa(TeamMemberDto teamMemberDto);
}
