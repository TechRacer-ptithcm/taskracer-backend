package ptithcm.itmc.taskracer.service.provider.internal.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.provider.IEmailProvider;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-EMAIL-PROVIDER")
public class DefaultEmailProvider implements IEmailProvider {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    @Override
    public Optional<UserDto> getUserFromOtp(String otp) throws Exception {
        String key = "otp:" + otp;
        if (!redisTemplate.hasKey(key)) throw new Exception("OTP is not found or already used.");
        String getUsername = (String) redisTemplate.opsForValue().get(key);
        var userData = jpaUserRepository.findByUsername(getUsername).orElseThrow(() -> new Exception("User not found."));
        return Optional.of(userServiceMapper.toDto(userData));
    }
}
