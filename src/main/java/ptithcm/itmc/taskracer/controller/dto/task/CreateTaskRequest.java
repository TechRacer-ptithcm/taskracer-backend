package ptithcm.itmc.taskracer.controller.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Priority;
import ptithcm.itmc.taskracer.repository.model.enumeration.ResourceType;
import ptithcm.itmc.taskracer.repository.model.enumeration.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {
    private UUID parent;

    private ResourceType type;

    private UUID resourceId; // user_id or team_id

    private String content;

    private Priority priority;

    private String description;

    private LocalDateTime startAt;

    private LocalDateTime dueAt;

    private TaskStatus status;
}
