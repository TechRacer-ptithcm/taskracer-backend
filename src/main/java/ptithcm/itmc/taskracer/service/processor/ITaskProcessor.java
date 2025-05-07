package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;

import java.util.UUID;

public interface ITaskProcessor {
    JpaTask createTask(TaskDto taskDto);

    JpaTask updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId);

    JpaTask deleteTask(UUID id, UUID ownerId);

    void addUserToTask(HandleUserDto request, UUID userId);

    void removeUserFromTask(HandleUserDto request, UUID userId);
}
