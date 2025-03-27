package ptithcm.itmc.taskracer.service.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserServiceMapper {
    UserServiceMapper INSTANCE = Mappers.getMapper(UserServiceMapper.class);

    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "tier.id", source = "tier")
    JpaUser toJpa(UserDto request);

    @Mapping(target = "tier", source = "tier.name")
    UserDto toDto(JpaUser request);

    default List<UserDto> toDto(List<JpaUser> request) {
        if (request.isEmpty()) {
            return Collections.emptyList();
        }
        return request.stream()
                .map(this::toDto)
                .toList();
    }

    @Named("toSetUsersDto")
    default Set<UserDto> toSetUserDto(Set<JpaUser> request) {
        if (request == null) {
            return Collections.emptySet();
        }
        return request.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    default UserDto merge(UserDto target, UserDto source) {
        if (StringUtils.hasText(source.getName())) {
            target.setName(source.getName());
        }
        if (source.getGender() != null) {
            target.setGender(source.getGender());
        }
        if (source.getBirth() != null) {
            target.setBirth(source.getBirth());
        }

        return target;
    }
}
