package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;

import java.util.UUID;

public interface IPomodoroProcessor {
    PomodoroDto startPomodoro(UUID userId, Long endTime);

    PomodoroDto checkpoint(UUID userId);

    PomodoroDto stopPomodoro(UUID userId);

    PomodoroDto getStartTime(UUID userId);
}
