package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;

@Mapper(componentModel = "spring", uses = {TierMapper.class, TeamServiceMapper.class})
public interface TeamContentServiceMapper {
    @Mapping(target = "userId.tier", source = "userId.tier", qualifiedByName = "mapTier")
    @Mapping(target = "teamId.users", source = "teamId.users", qualifiedByName = "teamMap")
    TeamContentDto toDto(JpaTeamContent jpa);
}
