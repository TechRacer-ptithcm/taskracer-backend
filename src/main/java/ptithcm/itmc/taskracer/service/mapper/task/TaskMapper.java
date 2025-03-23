package ptithcm.itmc.taskracer.service.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.service.dto.task.TaskDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserServiceMapper.class, TierMapper.class})
public interface TaskMapper {
    @Mapping(target = "users", ignore = true)
    @Mapping(source = "parent.id", target = "parent")
    TaskDto toTaskDto(JpaTask jpaTask);

    @Mapping(source = "parent", target = "parent.id")
    JpaTask toJpaTask(TaskDto taskDto);


    default List<TaskDto> toListTaskDto(List<JpaTask> jpaTasks) {
        if (jpaTasks.isEmpty()) {
            return Collections.emptyList();
        }
        return jpaTasks
                .stream()
                .map(this::toTaskDto)
                .toList();
    }

    default JpaTask mergeToJpaTask(JpaTask jpaTask, TaskDto taskDto) {
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
}
