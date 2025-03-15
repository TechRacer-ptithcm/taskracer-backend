package ptithcm.itmc.taskracer.service.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandleUserDto {
    private UUID userId;
    private Integer taskId;
}
