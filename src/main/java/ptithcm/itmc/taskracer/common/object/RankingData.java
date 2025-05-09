package ptithcm.itmc.taskracer.common.object;

import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingData {
    private Rank rank;
    private Integer tier;
    private Integer star;
}
