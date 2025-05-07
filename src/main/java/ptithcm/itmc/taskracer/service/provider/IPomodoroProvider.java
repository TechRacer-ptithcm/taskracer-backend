package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;

import java.util.UUID;

public interface IPomodoroProvider {
    PomodoroDto getStartTime(UUID userId);
}
