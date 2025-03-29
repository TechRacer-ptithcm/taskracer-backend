package ptithcm.itmc.taskracer.service.process.pomodoro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface IPomodoroService {
    Long startPomodoro(UUID userId);

    Long checkPoint(UUID userId);

    Long endPomodoro(UUID userId);
}

@Slf4j
@Service
@RequiredArgsConstructor
class PomodoroServiceProcessor implements IPomodoroService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${task-racer.pomodoro.checkpoint}")
    private Integer pomodoroCheckPointMinute;

    @Override
    public Long startPomodoro(UUID userId) {
        String key = "pomodoro:" + userId;
        Long timestamp = Instant.now().getEpochSecond();
        var existTime = (Integer) redisTemplate.opsForValue().get(key);
        log.info("exist time: {}", existTime);
        if (existTime != null) {
            throw new RuntimeException("Pomodoro is already started.");
        }
        redisTemplate.opsForValue().set(key,
                timestamp,
                pomodoroCheckPointMinute + 1,
                TimeUnit.MINUTES);
        log.info("pomodoro:: time start: {} - {}", timestamp, userId);
        return timestamp;
    }

    @Override
    @CachePut(value = "pomodoro", key = "#p0")
    public Long checkPoint(UUID userId) { //TODO: increase point to ranking
        String key = "pomodoro:" + userId;
        Long timestamp = Instant.now().getEpochSecond();
        var getStartTime = (Long) redisTemplate.opsForValue().get(key);
        Long getDuration = redisTemplate.getExpire(key);
        if (getStartTime == null) {
            throw new RuntimeException("Pomodoro is not started.");
        }
        if (timestamp - getStartTime < TimeUnit.SECONDS.toSeconds(pomodoroCheckPointMinute)) {
            throw new RuntimeException("Checkpoint time has not been reached yet.");
        }
        redisTemplate.opsForValue().setIfPresent(key, timestamp, getDuration, TimeUnit.SECONDS);
        log.info("pomodoro:: time checkpoint: {} - {}", timestamp, userId);
        return timestamp;
    }

    @Override
    @CacheEvict(value = "pomodoro", key = "#p0")
    public Long endPomodoro(UUID userId) {
        Long timestamp = Instant.now().getEpochSecond();
        log.info("pomodoro:: time end: {} - {}", timestamp, userId);
        return timestamp;
    }
}
