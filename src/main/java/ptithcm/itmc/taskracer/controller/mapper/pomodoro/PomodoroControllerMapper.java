package ptithcm.itmc.taskracer.controller.mapper.pomodoro;

import org.mapstruct.Mapper;
import ptithcm.itmc.taskracer.controller.dto.pomodoro.PomodoroResponse;
import ptithcm.itmc.taskracer.controller.dto.pomodoro.PomodoroStartRequest;
import ptithcm.itmc.taskracer.service.dto.pomodoro.PomodoroDto;

@Mapper(componentModel = "spring")
public interface PomodoroControllerMapper {
    PomodoroResponse toDomain(PomodoroDto pomodoro);
    PomodoroDto toDto(PomodoroStartRequest request);
}