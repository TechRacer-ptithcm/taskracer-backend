package ptithcm.itmc.taskracer.service.dto.pomodoro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PomodoroDto {
    Long startTime;
    Long checkpointTime;
    Long endTime;
    Integer point;
}
