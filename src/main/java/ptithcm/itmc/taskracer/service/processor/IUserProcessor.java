package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.service.dto.user.UserDto;

public interface IUserProcessor {
    UserDto editUser(UserDto userData, UserDto ownerDto);
    
}
