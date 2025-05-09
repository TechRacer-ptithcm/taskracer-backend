package ptithcm.itmc.taskracer.service.processor.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaRankDataRepository;
import ptithcm.itmc.taskracer.repository.JpaRankingRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.ranking.RankingServiceMapper;
import ptithcm.itmc.taskracer.service.processor.IRankingProcessor;
import ptithcm.itmc.taskracer.util.ranking.RankingUtil;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-RANKING-PROCESSOR")
public class DefaultRankingProcessor implements IRankingProcessor {
    private final JpaRankingRepository jpaRankingRepository;
    private final JpaRankDataRepository jpaRankDataRepository;
    private final RankingServiceMapper rankingServiceMapper;

    @Override
    public void handle(UUID userId, Integer point) {
        var getUserRanking = jpaRankingRepository.findByUserId(userId);
        var getListRanking = jpaRankDataRepository.findAll();
        if (getUserRanking.isPresent()) {
            var existing = getUserRanking.get();
            existing.setScore(existing.getScore() + point);
            log.info("Updating ranking score for user {}: new score = {}", existing.getUser().getId(), existing.getScore());
            if (getListRanking.isEmpty()) {
                throw new ResourceNotFound("Rank data is empty. Cannot proceed.");
            }
            var currentPoint = existing.getScore();
            for (var rankData : getListRanking) {
                var totalPoints = RankingUtil.getTotalPoint(rankData.getStarPerTier(), rankData.getPointPerStar());
                if (currentPoint < totalPoints) {
                    existing.setRankData(rankData);
                    break;
                }
                currentPoint -= totalPoints;
            }
            jpaRankingRepository.save(existing);
        } else {
            var dataToSave = RankingDto.builder()
                    .rank(Rank.BRONZER)
                    .score(point)
                    .user(UserDto.builder()
                            .id(userId)
                            .build())
                    .build();
            var formatData = rankingServiceMapper.toJpa(dataToSave);
            formatData.setRankData(getListRanking
                    .stream()
                    .filter(data -> data
                            .getName()
                            .equals(Rank.BRONZER))
                    .findFirst()
                    .get());
            log.info("Saving new ranking data for user {}: {}", userId, formatData);
            jpaRankingRepository.save(formatData);
        }
    }
}
