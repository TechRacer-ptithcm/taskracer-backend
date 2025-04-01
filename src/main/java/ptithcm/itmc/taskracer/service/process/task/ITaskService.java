package ptithcm.itmc.taskracer.service.process.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTaskRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskService {
    List<TaskDto> getAllTask(UUID ownerId);

    TaskDto getTaskById(UUID id, UUID ownerId);

    TaskDto createTask(TaskDto taskDto, UUID ownerId);

    TaskDto updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId);

    TaskDto deleteTask(UUID id, UUID ownerId);

    TaskDto addUserToTask(HandleUserDto request);

    TaskDto removeUserFromTask(HandleUserDto request);
}

@Service
@Slf4j
@RequiredArgsConstructor
class TaskServiceProcessor implements ITaskService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;
    private final UserServiceMapper userServiceMapper;

    @Override
    public List<TaskDto> getAllTask(UUID ownerId) {
        var data = jpaTaskRepository.findByOwner(ownerId);
        return taskMapper.toDto(data);
    }

    @Override
    @Cacheable(value = "task", key = "#p0")
    public TaskDto getTaskById(UUID id, UUID ownerId) {
        var data = jpaTaskRepository.findByIdAndOwner(id, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        return taskMapper.toDto(data);
    }

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto, UUID ownerId) {
        var foundUser = jpaUserRepository.findById(ownerId).orElseThrow(() ->
                new ResourceNotFound("User not found."));
        taskDto.setOwner(userServiceMapper.toDto(foundUser).getId());
        taskDto.setResourceId(Optional.ofNullable(taskDto.getResourceId()).orElse(ownerId));
        var saveData = taskMapper.toJpa(taskDto);
        var data = jpaTaskRepository.saveCustom(saveData);
        log.info("create task: {}", data);
        return taskMapper.toDto(data);
    }

    @Override
    @Transactional
    @CacheEvict(value = "task", key = "#p1")
    public TaskDto updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId) { //Without add user to task -- for user
        var foundTask = jpaTaskRepository.findByIdAndOwner(taskId, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        if (newTaskData.getParent() != null) {
            foundTask.setParent(jpaTaskRepository.findById(newTaskData.getParent()).orElseThrow(() ->
                    new ResourceNotFound("Parent task not found.")));
        }
        var saveData = taskMapper.merge(foundTask, newTaskData);
        log.info("update task: {}", saveData);
        var data = jpaTaskRepository.saveCustom(saveData);
        return taskMapper.toDto(data);
    }

    @Override
    @Transactional
    @CacheEvict(value = "task", key = "#p0")
    public TaskDto deleteTask(UUID id, UUID ownerId) {
        var foundTask = jpaTaskRepository.findByIdAndOwner(id, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        jpaTaskRepository.delete(foundTask);
        return taskMapper.toDto(foundTask);
    }

    @Override
    @CacheEvict(value = "task", key = "#p0.taskId")
    public TaskDto addUserToTask(HandleUserDto request) {
        return null; //Stage 2
    }

    @Override
    @CacheEvict(value = "task", key = "#p0.taskId")
    public TaskDto removeUserFromTask(HandleUserDto request) {
        return null; //Stage 2
    }
}
