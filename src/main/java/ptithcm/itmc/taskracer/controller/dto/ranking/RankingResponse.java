package ptithcm.itmc.taskracer.controller.dto.ranking;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.controller.dto.user.UserResponse;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class RankingResponse<T> {
    UserResponse user;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    private Integer score;
    private T top;

}
