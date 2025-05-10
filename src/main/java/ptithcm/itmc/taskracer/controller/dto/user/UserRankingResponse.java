package ptithcm.itmc.taskracer.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingResponse {
    private Integer streak;
    private String username;
    private String name;
}
