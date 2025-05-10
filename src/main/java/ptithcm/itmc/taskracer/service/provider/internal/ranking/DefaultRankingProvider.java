package ptithcm.itmc.taskracer.service.provider.internal.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaRankDataRepository;
import ptithcm.itmc.taskracer.repository.JpaRankingRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.ranking.TopUserDto;
import ptithcm.itmc.taskracer.service.mapper.ranking.RankingServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.provider.IRankingProvider;
import ptithcm.itmc.taskracer.util.ranking.RankingUtil;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j(topic = "SERVICE-RANKING-PROVIDER")
@RequiredArgsConstructor
public class DefaultRankingProvider implements IRankingProvider {
    private final JpaRankingRepository jpaRankingRepository;
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    private final RankingServiceMapper rankingServiceMapper;
    private final JpaRankDataRepository jpaRankDataRepository;

    @Override
    public RankingDto getOne(UUID userId) {
        var getListRanking = jpaRankDataRepository.findAll();
        var getData = jpaRankingRepository.findByUserId(userId);
        if (getData.isEmpty()) {
            throw new ResourceNotFound("UserId not found in leaderboard");
        }
        var result = rankingServiceMapper.toDto(getData.get());
        var getCurrentRankData = getListRanking.stream().filter(data -> data
                        .getName()
                        .equals(result.getRank()))
                .findFirst()
                .get();
        result.setRankData(RankingUtil.calculateTierAndStar(
                result.getScore(),
                getCurrentRankData.getStarPerTier(),
                getCurrentRankData.getPointPerStar(),
                result.getRank()

        ));
        return result;
    }

    @Override
    public List<RankingDto> getList() {
        var page = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "score"));
        var getData = jpaRankingRepository.getListTop(page);
        var getListRanking = jpaRankDataRepository.findAll();
        var result = rankingServiceMapper.toDto(getData);
        result = result.stream().map(
                        currentData -> {
                            var getCurrentRankData = getListRanking.stream().filter(data -> data
                                            .getName()
                                            .equals(currentData.getRank()))
                                    .findFirst()
                                    .get();
                            currentData.setRankData(RankingUtil.calculateTierAndStar(
                                    currentData.getScore(),
                                    getCurrentRankData.getStarPerTier(),
                                    getCurrentRankData.getPointPerStar(),
                                    currentData.getRank()
                            ));
                            return currentData;
                        }
                )
                .toList();
        return result;
    }

    @Override
    public TopUserDto<Integer> getCurrentTop(UUID userId) {
        if (jpaRankingRepository.findByUserId(userId).isEmpty()) {
            throw new ResourceNotFound("User not found in leaderboard");
        }
        var currentPosition = jpaRankingRepository.getCurrentTop(userId);
        var userData = jpaUserRepository.findById(userId);
        return TopUserDto.<Integer>builder()
                .user(userServiceMapper.toDto(userData.get()))
                .top(currentPosition)
                .build();
    }

    @Override
    public TopUserDto<Double> getPercentTop(UUID userId) {
        if (jpaRankingRepository.findByUserId(userId).isEmpty()) {
            throw new ResourceNotFound("User not found in leaderboard");
        }
        var currentPosition = jpaRankingRepository.getPercentCurrentTop(userId);
        var userData = jpaUserRepository.findById(userId);
        return TopUserDto.<Double>builder()
                .user(userServiceMapper.toDto(userData.get()))
                .top(currentPosition)
                .build();
    }
}
