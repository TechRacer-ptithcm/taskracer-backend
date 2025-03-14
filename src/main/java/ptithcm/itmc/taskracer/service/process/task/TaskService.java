package ptithcm.itmc.taskracer.service.process.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaTaskRepository;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.task.TaskMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;

    public PageableObject<List<TaskDto>> getAllTask(Pageable pageable) {
        var data = jpaTaskRepository.findAll(pageable);
        return PageableObject.<List<TaskDto>>builder()
                .content(taskMapper.toListTaskDto(data.getContent()))
                .currentPage(data.getNumber())
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .build();
    }

    public TaskDto getTaskById(Integer id) {
        var data = jpaTaskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        return taskMapper.toTaskDto(data);
    }

    public TaskDto createTask(TaskDto taskDto) {
        var data = jpaTaskRepository.save(taskMapper.toJpaTask(taskDto));
        return taskMapper.toTaskDto(data);
    }

    public TaskDto updateTask(TaskDto taskDto) {
        jpaTaskRepository.findById(taskDto.getId()).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        var data = jpaTaskRepository.save(taskMapper.toJpaTask(taskDto));
        return taskMapper.toTaskDto(data);
    }

    public TaskDto deleteTask(Integer id) {
        var foundTask = jpaTaskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFound("Task not found."));
        jpaTaskRepository.delete(foundTask);
        return taskMapper.toTaskDto(foundTask);
    }
}
