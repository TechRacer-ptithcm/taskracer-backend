package ptithcm.itmc.taskracer.service.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.repository.model.JpaTaskAssignees;
import ptithcm.itmc.taskracer.service.dto.task.HandleUserDto;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserServiceMapper.class, TierMapper.class})
public interface TaskMapper {
    @Mapping(source = "parent.id", target = "parent")
    @Mapping(target = "users", expression = "java(toListUserId(jpaTask.getAssignees()))")
    TaskDto toDto(JpaTask jpaTask);

    @Mapping(source = "parent", target = "parent.id")
    JpaTask toJpa(TaskDto taskDto);

    default Set<UUID> toListUserId(Set<JpaTaskAssignees> taskAssignees)
    {
        return taskAssignees.stream()
                .map(data -> data.getId())
                .collect(Collectors.toSet());
    }

    default List<TaskDto> toDto(List<JpaTask> jpaTasks) {
        if (jpaTasks.isEmpty()) {
            return Collections.emptyList();
        }
        return jpaTasks
                .stream()
                .map(this::toDto)
                .toList();
    }

    default JpaTask merge(JpaTask jpaTask, TaskDto taskDto) {
        if (taskDto.getParent() != null && !taskDto.getParent().equals(jpaTask.getId())) {
            jpaTask.setParent(JpaTask.builder().id(taskDto.getParent()).build());
        }

        if (taskDto.getPriority() != null) {
            jpaTask.setPriority(taskDto.getPriority());
        }
        if (StringUtils.hasText(taskDto.getContent())) {
            jpaTask.setContent(taskDto.getContent());
        }
        if (StringUtils.hasText(taskDto.getDescription())) {
            jpaTask.setDescription(taskDto.getDescription());
        }
        if (taskDto.getDueAt() != null) {
            jpaTask.setDueAt(taskDto.getDueAt());
        }
        if (taskDto.getStartAt() != null) {
            jpaTask.setStartAt(taskDto.getStartAt());
        }
        if (taskDto.getStatus() != null) {
            jpaTask.setStatus(taskDto.getStatus());
        }

        return jpaTask;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId.id", source = "taskId")
    JpaTaskAssignees toJpa(HandleUserDto request);

    TaskDto toDto(JpaTaskAssignees request);
}
