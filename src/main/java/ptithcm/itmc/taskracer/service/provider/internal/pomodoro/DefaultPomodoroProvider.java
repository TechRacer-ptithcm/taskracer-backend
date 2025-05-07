package ptithcm.itmc.taskracer.service.provider.internal.pomodoro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;
import ptithcm.itmc.taskracer.service.provider.IPomodoroProvider;
import ptithcm.itmc.taskracer.util.json.ParseObject;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-POMODORO-PROVIDER")
public class DefaultPomodoroProvider implements IPomodoroProvider {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PomodoroDto getStartTime(UUID userId) {
        String key = "pomodoro::" + userId;
        var rawData = redisTemplate.opsForValue().get(key);
        if (rawData == null) {
            log.info("No active pomodoro session found for user: {}", userId);
            throw new RuntimeException("Pomodoro session not found");
        }
        var pomodoroDto = ParseObject.parse(rawData, PomodoroDto.class);
        return pomodoroDto;
    }
}
