package ptithcm.itmc.taskracer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;
import ptithcm.itmc.taskracer.service.processor.ITaskProcessor;
import ptithcm.itmc.taskracer.service.provider.ITaskProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final ITaskProcessor processor;
    private final ITaskProvider provider;
    private final TaskMapper mapper;

    public List<TaskDto> getAllTask(UUID ownerId) {
        var data = provider.getAllTask(ownerId);
        return mapper.toDto(data);
    }

    @Cacheable(value = "task", key = "#p0")
    public TaskDto getTaskById(UUID id, UUID ownerId) {
        var data = provider.getTaskById(id, ownerId);
        return mapper.toDto(data);
    }

    @Transactional
    public TaskDto createTask(TaskDto taskDto, UUID ownerId) {
        taskDto.setOwner(ownerId);
        taskDto.setResourceId(Optional.ofNullable(taskDto.getResourceId()).orElse(ownerId));
        var data = processor.createTask(taskDto);
        return mapper.toDto(data);
    }

    @Transactional
    @CacheEvict(value = "task", key = "#p1")
    public TaskDto updateTask(TaskDto newTaskData, UUID taskId, UUID ownerId) { //Without add user to task -- for user
        var data = processor.updateTask(newTaskData, taskId, ownerId);
        return mapper.toDto(data);
    }

    @Transactional
    @CacheEvict(value = "task", key = "#p0")
    public TaskDto deleteTask(UUID id, UUID ownerId) {
        var data = processor.deleteTask(id, ownerId);
        return mapper.toDto(data);
    }

    @CacheEvict(value = "task", key = "#p0.taskId")
    public TaskDto addUserToTask(HandleUserDto request) {
        var data = processor.addUserToTask(request); //Stage 2
        return mapper.toDto(data);
    }

    @CacheEvict(value = "task", key = "#p0.taskId")
    public TaskDto removeUserFromTask(HandleUserDto request) {
        var data = processor.removeUserFromTask(request); //Stage 2
        return mapper.toDto(data);
    }
}
