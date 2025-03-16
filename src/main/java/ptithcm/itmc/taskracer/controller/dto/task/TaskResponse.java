package ptithcm.itmc.taskracer.controller.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.repository.model.enumeration.Priority;
import ptithcm.itmc.taskracer.repository.model.enumeration.ResourceType;
import ptithcm.itmc.taskracer.repository.model.enumeration.TaskStatus;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private UUID id;

    private JpaTask parent;

    private ResourceType type;

    private UUID resourceId; // user_id or team_id

    private JpaUser owner;

    private String content;

    private Priority priority;

    private String description;

    private LocalDateTime startAt;

    private LocalDateTime dueAt;

    private TaskStatus status;

    private Set<UserDto> users;
}
