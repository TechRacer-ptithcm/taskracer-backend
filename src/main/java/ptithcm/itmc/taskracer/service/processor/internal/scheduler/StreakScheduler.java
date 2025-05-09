package ptithcm.itmc.taskracer.service.processor.internal.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "STREAK-SCHEDULER")
public class StreakScheduler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JpaUserRepository jpaUserRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void checkStreak() {
        log.info("Run check streak");
        var getData = jpaUserRepository.findAll();
        getData.forEach(
                data -> {
                    log.info("check user: {}", data);
                    String streakKey = "streak::" + data.getId();
                    if (redisTemplate.opsForValue().get(streakKey) == null) {
                        if (data.getStreak() != 0) {
                            data.setStreak(0);
                            jpaUserRepository.save(data);
                        }
                    } else {
                        data.setStreak(data.getStreak() + 1);
                        jpaUserRepository.save(data);
                        redisTemplate.delete(streakKey);
                    }
                }
        );
    }
}
