package ptithcm.itmc.taskracer.controller.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ListRankingResponse<T> {
    List<RankingResponse<T>> users;

}
