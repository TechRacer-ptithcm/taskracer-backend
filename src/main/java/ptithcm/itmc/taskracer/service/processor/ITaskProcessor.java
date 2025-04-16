package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;

import java.util.UUID;

public interface ITaskProcessor {
    TaskDto createTask(TaskDto taskDto, UUID ownerId);

    TaskDto updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId);

    TaskDto deleteTask(UUID id, UUID ownerId);

    TaskDto addUserToTask(HandleUserDto request);

    TaskDto removeUserFromTask(HandleUserDto request);
}
