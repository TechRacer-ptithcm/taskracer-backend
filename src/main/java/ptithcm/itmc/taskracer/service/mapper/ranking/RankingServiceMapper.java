package ptithcm.itmc.taskracer.service.mapper.ranking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaRanking;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserServiceMapper.class})
public interface RankingServiceMapper {
    @Mapping(target = "rankData.name", source = "rank")
    @Mapping(target = "id", ignore = true)
    JpaRanking toJpa(RankingDto request);


    @Mapping(target = "rank", source = "rankData.name")
    RankingDto toDto(JpaRanking request);

    default List<RankingDto> toDto(List<JpaRanking> listRanking) {
        return listRanking
                .stream()
                .map(data -> toDto(data))
                .toList();
    }
}
