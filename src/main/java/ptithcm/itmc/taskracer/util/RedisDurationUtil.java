package ptithcm.itmc.taskracer.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RedisDurationUtil {
    public static Duration getDurationNextDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDayAt005 = now.plusDays(1).with(LocalTime.of(0, 5));
        return Duration.between(now, nextDayAt005);
    }
}
