package ptithcm.itmc.taskracer.controller.dto.pomodoro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PomodoroResponse {
    private Long startTime;
    private Long checkpointTime;
    private Long endTime;
    private Integer point;
}