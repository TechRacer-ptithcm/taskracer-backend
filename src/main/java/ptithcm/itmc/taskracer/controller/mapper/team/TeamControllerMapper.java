package ptithcm.itmc.taskracer.controller.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.controller.dto.team.GetTeamResponse;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

@Mapper(componentModel = "spring")
public interface TeamControllerMapper {
    @Mapping(target = "owner", source = "ownerId")
    GetTeamResponse toDomain(TeamDto team);
}
