package ptithcm.itmc.taskracer.controller.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.controller.dto.team.*;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamControllerMapper {
    @Mapping(target = "owner", source = "ownerId")
    GetTeamResponse toDomain(TeamDto team);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "id", ignore = true)
    TeamDto toDto(CreateNewTeamRequest team);


    TeamDto toDto(UpdateTeamRequest team);

    GetTeamContentResponse toDomain(TeamContentDto team);

    PageableObject<List<GetTeamResponse>> toDomain(PageableObject<List<TeamContentDto>> team);

    TeamContentDto toDto(CreateNewTeamContentRequest request);
}
