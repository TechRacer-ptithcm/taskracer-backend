package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

@Mapper(componentModel = "spring")
public interface TeamServiceMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    JpaTeam toJpaTeam(TeamDto data);
}
