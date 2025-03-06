package ptithcm.itmc.taskracer.service.process.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    public UserDto getUser(String username) {
        var data = jpaUserRepository.findByUsername(username);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        return userServiceMapper.toUserDto(data.get());
    }
}
