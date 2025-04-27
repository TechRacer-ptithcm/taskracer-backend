package ptithcm.itmc.taskracer.service.processor.internal.pomodoro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;
import ptithcm.itmc.taskracer.service.processor.IPomodoroProcessor;
import ptithcm.itmc.taskracer.util.json.ParseObject;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-POMODORO-PROCESSOR")
public class DefaultPomodoroProcessor implements IPomodoroProcessor {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${task-racer.pomodoro.checkpoint}")
    private Integer pomodoroCheckPointMinute;

    private PomodoroDto getRawObjectPomodoroDto(Object rawData) {
        var getPomodoroTime = ParseObject.parse(rawData, PomodoroDto.class);
        if (getPomodoroTime == null) {
            throw new RuntimeException("Pomodoro is not started.");
        }
        if (getPomodoroTime.getStartTime() == null) {
            throw new RuntimeException("Pomodoro is not started.");
        }
        return getPomodoroTime;
    }

    @Override
    public PomodoroDto startPomodoro(UUID userId, Long endTime) {
        String key = "pomodoro::" + userId;
        Long timestamp = Instant.now().getEpochSecond();
        var existPomodoro = redisTemplate.opsForValue().get(key);
        log.info("exist time: {}", existPomodoro);
        if (existPomodoro != null) {
            throw new RuntimeException("Pomodoro is already started.");
        }
        var dataToSave = PomodoroDto.builder()
                .startTime(timestamp)
                .checkpointTime(timestamp)
                .endTime(endTime)
                .point(0)
                .build();
        redisTemplate.opsForValue().set(key,
                dataToSave,
                pomodoroCheckPointMinute + 1,
                TimeUnit.MINUTES);
        log.info("pomodoro:: time start: {} - {}", timestamp, userId);
        return dataToSave;
    }

    @Override
    public PomodoroDto checkpoint(UUID userId) {
        String key = "pomodoro::" + userId;
        Long timestamp = Instant.now().getEpochSecond();
        var rawData = redisTemplate.opsForValue().get(key);
        var getPomodoroTime = getRawObjectPomodoroDto(rawData);
        if (timestamp - getPomodoroTime.getCheckpointTime() < TimeUnit.MINUTES.toSeconds(pomodoroCheckPointMinute)) {
            throw new RuntimeException("Checkpoint time has not been reached yet.");
        }
        getPomodoroTime.setCheckpointTime(timestamp);
        getPomodoroTime.setPoint(getPomodoroTime.getPoint() + 1);
        redisTemplate.opsForValue().setIfPresent(key, getPomodoroTime,
                pomodoroCheckPointMinute + 1,
                TimeUnit.MINUTES);
        log.info("pomodoro:: time checkpoint: {} - {}", timestamp, userId);
        return getPomodoroTime;
    }

    @Override
    @CacheEvict(value = "pomodoro", key = "#p0")
    public PomodoroDto stopPomodoro(UUID userId) {
        String key = "pomodoro::" + userId;
        var rawData = redisTemplate.opsForValue().get(key);
        var getPomodoroTime = getRawObjectPomodoroDto(rawData);
        Long timestamp = Instant.now().getEpochSecond();
        getPomodoroTime.setCheckpointTime(timestamp);
        if (timestamp - getPomodoroTime.getCheckpointTime() >= TimeUnit.MINUTES.toSeconds(pomodoroCheckPointMinute)) {
            getPomodoroTime.setPoint(getPomodoroTime.getPoint() + 1);
        }
        //TODO: add point
        log.info("pomodoro:: time: {} - {}", getPomodoroTime.getStartTime(), getPomodoroTime.getCheckpointTime());
        return getPomodoroTime;
    }

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
