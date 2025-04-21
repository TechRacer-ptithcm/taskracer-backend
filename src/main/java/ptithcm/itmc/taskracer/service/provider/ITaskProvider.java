package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.repository.model.JpaTask;

import java.util.List;
import java.util.UUID;

public interface ITaskProvider {
    List<JpaTask> getAllTask(UUID ownerId);

    JpaTask getTaskById(UUID id, UUID ownerId);
}
