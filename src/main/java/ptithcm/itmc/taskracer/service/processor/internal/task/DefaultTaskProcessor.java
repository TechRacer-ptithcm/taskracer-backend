package ptithcm.itmc.taskracer.service.processor.internal.task;

import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.processor.ITaskProcessor;

import java.util.UUID;

public class DefaultTaskProcessor implements ITaskProcessor {
    @Override
    public TaskDto createTask(TaskDto taskDto, UUID ownerId) {
        return null;
    }

    @Override
    public TaskDto updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId) {
        return null;
    }

    @Override
    public TaskDto deleteTask(UUID id, UUID ownerId) {
        return null;
    }

    @Override
    public TaskDto addUserToTask(HandleUserDto request) {
        return null;
    }

    @Override
    public TaskDto removeUserFromTask(HandleUserDto request) {
        return null;
    }
}
