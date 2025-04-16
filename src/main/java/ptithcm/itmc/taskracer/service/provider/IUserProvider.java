package ptithcm.itmc.taskracer.service.provider;

import org.springframework.data.domain.Pageable;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.util.List;

public interface IUserProvider {
    UserDto getUserDataByUserName(String username);

    PageableObject<List<UserDto>> getAllUser(Pageable pageable);
}
