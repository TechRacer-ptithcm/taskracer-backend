package ptithcm.itmc.taskracer.controller.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandleUserInTaskRequest {
    private UUID userId;
    private UUID taskId;
}
