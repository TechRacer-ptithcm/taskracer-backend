package ptithcm.itmc.taskracer.service.provider.internal.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTaskRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;
import ptithcm.itmc.taskracer.service.provider.ITaskProvider;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TASK-PROVIDER")
public class DefaultTaskProvider implements ITaskProvider {
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<JpaTask> getAllTask(UUID ownerId) {
        var data = jpaTaskRepository.findByOwner(ownerId);
        return data;
    }

    @Override
    public JpaTask getTaskById(UUID id, UUID ownerId) {
        return jpaTaskRepository.findByIdAndOwner(id, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
    }
}
