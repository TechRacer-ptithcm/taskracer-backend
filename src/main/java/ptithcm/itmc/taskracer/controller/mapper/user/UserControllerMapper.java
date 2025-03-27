package ptithcm.itmc.taskracer.controller.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.controller.dto.user.UpdateUserRequest;
import ptithcm.itmc.taskracer.controller.dto.user.UserResponse;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
    UserResponse toDomain(UserDto request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "tier", ignore = true)
    @Mapping(target = "streak", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "active", ignore = true)
    UserDto toDto(UpdateUserRequest request);
}
