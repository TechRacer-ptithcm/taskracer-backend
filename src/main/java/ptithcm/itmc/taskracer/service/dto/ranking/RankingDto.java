package ptithcm.itmc.taskracer.service.dto.ranking;

import lombok.*;
import ptithcm.itmc.taskracer.common.object.RankingData;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RankingDto {
    private UserDto user;

    private Rank rank;

    private Integer score;

    private RankingData rankData;
}
