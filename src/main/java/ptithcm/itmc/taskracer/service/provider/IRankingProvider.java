package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.ranking.TopUserDto;

import java.util.List;
import java.util.UUID;

public interface IRankingProvider {
    RankingDto getOne(UUID userId);

    List<RankingDto> getList();

    TopUserDto getCurrentTop(UUID userId);

    TopUserDto getPercentTop(UUID userId);
}
