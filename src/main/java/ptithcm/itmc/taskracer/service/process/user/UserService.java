package ptithcm.itmc.taskracer.service.process.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    //    @Cacheable(value = "users", key = "T(java.lang.String).format('user:%s:info', #username)", unless = "#username == null")
    public UserDto getUser(String username) {
        if (redisTemplate.opsForValue().get("users:user:" + username + ":info") != null) {
            log.info("userData: {}", redisTemplate.opsForValue().get("users:user:" + username + ":info"));
            return (UserDto) redisTemplate.opsForValue().get("users:user:" + username + ":info");
        }
        var data = jpaUserRepository.findByUsername(username);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        var userData = userServiceMapper.toUserDto(data.get());
        redisTemplate.opsForValue().set("users:user:" + username + ":info", userData, 5, TimeUnit.of(ChronoUnit.MINUTES));
        return userData;
    }
}
