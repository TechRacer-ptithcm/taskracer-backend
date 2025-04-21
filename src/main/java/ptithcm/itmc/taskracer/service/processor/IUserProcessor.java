package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

public interface IUserProcessor {
    JpaUser edit(UserDto userData, UserDto ownerDto);
    
}
