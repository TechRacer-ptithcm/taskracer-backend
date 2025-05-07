package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;
import ptithcm.itmc.taskracer.service.processor.IPomodoroProcessor;
import ptithcm.itmc.taskracer.service.provider.IPomodoroProvider;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PomodoroService {
    private final IPomodoroProcessor processor;
    private final IPomodoroProvider provider;

    public PomodoroDto startPomodoro(UUID userId, PomodoroDto pomodoroDto) {
        return processor.startPomodoro(userId, pomodoroDto);
    }

    public PomodoroDto checkpoint(UUID userId) { //TODO: increase point to ranking
        return processor.checkpoint(userId);
    }

    public PomodoroDto stopPomodoro(UUID userId) {
        return processor.stopPomodoro(userId);
    }

    public PomodoroDto getStartTime(UUID userId) {
        return provider.getStartTime(userId);
    }
}
