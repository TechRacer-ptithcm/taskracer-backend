package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.service.dto.task.TaskDto;

import java.util.List;
import java.util.UUID;

public interface ITaskProvider {
    List<TaskDto> getAllTask(UUID ownerId);

    TaskDto getTaskById(UUID id, UUID ownerId);
}
