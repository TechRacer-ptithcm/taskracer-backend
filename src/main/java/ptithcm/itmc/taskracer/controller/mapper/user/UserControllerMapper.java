package ptithcm.itmc.taskracer.controller.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.controller.dto.user.UpdateUserRequest;
import ptithcm.itmc.taskracer.controller.dto.user.UserResponse;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
    UserResponse toUserResponse(UserDto request);

    @Mapping(target = "active", ignore = true)
    UserDto toUserDto(UpdateUserRequest request);
}
