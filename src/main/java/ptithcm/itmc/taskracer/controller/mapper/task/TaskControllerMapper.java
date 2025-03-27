package ptithcm.itmc.taskracer.controller.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.controller.dto.task.CreateTaskRequest;
import ptithcm.itmc.taskracer.controller.dto.task.HandleUserInTaskRequest;
import ptithcm.itmc.taskracer.controller.dto.task.TaskResponse;
import ptithcm.itmc.taskracer.controller.dto.task.UpdateTaskRequest;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {TierMapper.class})
public interface TaskControllerMapper {
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    TaskDto toDto(CreateTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    TaskDto toDto(UpdateTaskRequest request);

    TaskResponse toDomain(TaskDto task);

    HandleUserDto toDto(HandleUserInTaskRequest request);

    default List<TaskResponse> toDomain(List<TaskDto> tasks) {
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        return tasks.stream()
                .map(this::toDomain)
                .toList();
    }
}
