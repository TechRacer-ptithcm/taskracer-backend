package ptithcm.itmc.taskracer.service.processor.internal.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTaskRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;
import ptithcm.itmc.taskracer.service.processor.ITaskProcessor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-TASK-PROCESSOR")
public class DefaultTaskProcessor implements ITaskProcessor {
    private final JpaUserRepository jpaUserRepository;
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;
    @Override
    public JpaTask createTask(TaskDto taskDto) {
        if(jpaUserRepository.findById(taskDto.getOwner()).isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        var saveData = taskMapper.toJpa(taskDto);
        var data = jpaTaskRepository.saveCustom(saveData);
        log.info("create task: {}", data);
        return data;
    }

    @Override
    public JpaTask updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId) {
        var foundTask = jpaTaskRepository.findByIdAndOwner(taskId, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        if (newTaskData.getParent() != null) {
            foundTask.setParent(jpaTaskRepository.findById(newTaskData.getParent()).orElseThrow(() ->
                    new ResourceNotFound("Parent task not found.")));
        }
        var saveData = taskMapper.merge(foundTask, newTaskData);
        log.info("update task: {}", saveData);
        var data = jpaTaskRepository.saveCustom(saveData);
        return data;
    }

    @Override
    public JpaTask deleteTask(UUID id, UUID ownerId) {
        var task = jpaTaskRepository.findByIdAndOwner(id, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        jpaTaskRepository.delete(task);
        return task;
    }

    @Override
    public JpaTask addUserToTask(HandleUserDto request) {
        return null;
    }

    @Override
    public JpaTask removeUserFromTask(HandleUserDto request) {
        return null;
    }
}
