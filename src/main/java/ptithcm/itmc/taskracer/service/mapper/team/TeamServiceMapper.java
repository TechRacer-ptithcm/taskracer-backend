package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.dto.team.TeamDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TeamServiceMapper {
    default UUID map(JpaUser user) {
        return user.getId();
    }

    default JpaUser map(UUID id) {
        return JpaUser.builder()
                .id(id)
                .build();
    }

    Set<UUID> map(Set<JpaUser> users);

    @Mapping(target = "owner.id", source = "ownerId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    JpaTeam toJpa(TeamDto data);

    @Mapping(target = "ownerId", source = "owner.id")
    TeamDto toDto(JpaTeam data);

    default List<TeamDto> toDto(List<JpaTeam> data) {
        return data.stream().map(this::toDto).toList();
    }
}
