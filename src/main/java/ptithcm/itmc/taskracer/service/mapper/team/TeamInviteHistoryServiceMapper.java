package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaTeamInviteHistory;
import ptithcm.itmc.taskracer.service.dto.team.TeamInviteHistoryDto;

@Mapper(componentModel = "spring")
public interface TeamInviteHistoryServiceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team.id", source = "team")
    @Mapping(target = "user.id", source = "user")
    JpaTeamInviteHistory toJpa(TeamInviteHistoryDto teamInviteHistoryDto);
}
