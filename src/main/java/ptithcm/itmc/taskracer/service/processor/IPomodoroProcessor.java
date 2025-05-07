package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;

import java.util.UUID;

public interface IPomodoroProcessor {
    PomodoroDto startPomodoro(UUID userId, PomodoroDto pomodoroDto);

    PomodoroDto checkpoint(UUID userId);

    PomodoroDto stopPomodoro(UUID userId);
}
