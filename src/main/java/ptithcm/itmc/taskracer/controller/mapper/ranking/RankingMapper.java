package ptithcm.itmc.taskracer.controller.mapper.ranking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.controller.dto.ranking.RankingResponse;
import ptithcm.itmc.taskracer.controller.mapper.user.UserControllerMapper;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.ranking.TopUserDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserControllerMapper.class})
public interface RankingMapper {
    @Mapping(target = "top", ignore = true)
    RankingResponse<Integer> toDomain(RankingDto request);

    @Mapping(target = "score", ignore = true)
    @Mapping(target = "rankData", ignore = true)
    RankingResponse<Integer> toDomainInteger(TopUserDto<Integer> request);

    @Mapping(target = "score", ignore = true)
    @Mapping(target = "rankData", ignore = true)
    RankingResponse<Double> toDomainDouble(TopUserDto<Double> request);

    default List<RankingResponse<Integer>> toDomain(List<RankingDto> requests) {
        if (requests == null) return null;
        return requests.stream()
                .map(this::toDomain)
                .toList();
    }
}
