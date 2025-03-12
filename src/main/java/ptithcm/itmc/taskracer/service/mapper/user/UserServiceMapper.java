package ptithcm.itmc.taskracer.service.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserServiceMapper {

    @Mapping(target = "tier.id", source = "tier")
    JpaUser toJpaUserDto(UserDto request);

    @Mapping(target = "tier", source = "tier.name")
    UserDto toUserDto(JpaUser request);

    default List<UserDto> toListUserDto(List<JpaUser> request) {
        if (request.isEmpty()) {
            return Collections.emptyList();
        }
        return request.stream()
                .map(this::toUserDto)
                .toList();
    }
}
