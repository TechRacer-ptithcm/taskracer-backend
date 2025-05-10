package ptithcm.itmc.taskracer.controller.dto.ranking;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.common.object.RankingData;
import ptithcm.itmc.taskracer.controller.dto.user.UserResponse;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class RankingResponse<T> {
    UserResponse user;
    @Enumerated(EnumType.STRING)
    private Integer score;
    private T top;
    private RankingData rankData;

}
