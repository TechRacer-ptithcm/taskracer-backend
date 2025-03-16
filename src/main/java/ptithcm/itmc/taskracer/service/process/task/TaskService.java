package ptithcm.itmc.taskracer.service.process.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.exception.TierInsufficientException;
import ptithcm.itmc.taskracer.repository.JpaTaskRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;
    private final UserServiceMapper userServiceMapper;

    public List<TaskDto> getAllTask(UUID ownerId) {
        var data = jpaTaskRepository.findByOwnerId(ownerId);
        return taskMapper.toListTaskDto(data);
    }

    public TaskDto getTaskById(UUID id, UUID ownerId) {
        var data = jpaTaskRepository.findByIdAndOwnerId(id, ownerId).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        return taskMapper.toTaskDto(data);
    }

    @Transactional
    public TaskDto createTask(TaskDto taskDto, UUID ownerId) {
        var foundUser = jpaUserRepository.findById(ownerId).orElseThrow(() ->
                new ResourceNotFound("User not found."));
        taskDto.setOwner(userServiceMapper.toUserDto(foundUser));
        var saveData = taskMapper.toJpaTask(taskDto);
        log.info("create task: {}", saveData);
        var data = jpaTaskRepository.save(saveData);
        return taskMapper.toTaskDto(data);
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDto, UUID ownerId) { //Without add user to task
        var foundTask = jpaTaskRepository.findById(taskDto.getId()).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        if (!foundTask.getOwner().getId().equals(ownerId)) {
            throw new TierInsufficientException("You don't have permission to update this task");
        }
        var data = jpaTaskRepository.save(taskMapper.toJpaTask(taskDto));
        return taskMapper.toTaskDto(data);
    }

    @Transactional
    public TaskDto deleteTask(UUID id, UUID ownerId) {
        var foundTask = jpaTaskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        if (foundTask.getOwner().getId().equals(ownerId)) {
            throw new TierInsufficientException("You don't have permission to delete this task");
        }
        jpaTaskRepository.delete(foundTask);
        return taskMapper.toTaskDto(foundTask);
    }

    public TaskDto addUserToTask(HandleUserDto request) {
        var foundTask = jpaTaskRepository.findById(request.getTaskId()).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        var foundUser = jpaUserRepository.findById(request.getUserId()).orElseThrow(() ->
                new ResourceNotFound("User not found."));
        foundTask.getUsers().add(foundUser);
        var data = jpaTaskRepository.save(foundTask);
        return taskMapper.toTaskDto(data);
    }

    public TaskDto removeUserFromTask(HandleUserDto request) {
        var foundTask = jpaTaskRepository.findById(request.getTaskId()).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        var foundUser = jpaUserRepository.findById(request.getUserId()).orElseThrow(() ->
                new ResourceNotFound("User not found."));
        foundTask.getUsers().remove(foundUser);
        var data = jpaTaskRepository.save(foundTask);
        return taskMapper.toTaskDto(data);
    }
}
