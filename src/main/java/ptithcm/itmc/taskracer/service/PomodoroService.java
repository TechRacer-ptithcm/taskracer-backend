package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;
import ptithcm.itmc.taskracer.service.processor.IPomodoroProcessor;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PomodoroService {
    private final IPomodoroProcessor processor;

    public PomodoroDto startPomodoro(UUID userId, Long endTime) {
        return processor.startPomodoro(userId, endTime);
    }

    public PomodoroDto checkpoint(UUID userId) { //TODO: increase point to ranking
        return processor.checkpoint(userId);
    }

    public PomodoroDto stopPomodoro(UUID userId) {
        return processor.stopPomodoro(userId);
    }

    public PomodoroDto getStartTime(UUID userId) {
        return processor.getStartTime(userId);
    }
}
