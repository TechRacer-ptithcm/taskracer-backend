package ptithcm.itmc.taskracer.service.provider.internal.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaRankingRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.ranking.TopUserDto;
import ptithcm.itmc.taskracer.service.mapper.ranking.RankingServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.provider.IRankingProvider;

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

    @Override
    public RankingDto getOne(UUID userId) {
        var getData = jpaRankingRepository.findByUserId(userId);
        if (getData.isEmpty()) {
            throw new ResourceNotFound("UserId not found in leaderboard");
        }
        return rankingServiceMapper.toDto(getData.get());
    }

    @Override
    public List<RankingDto> getList() {
        var page = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "score"));
        var getData = jpaRankingRepository.getListTop(page);
        return rankingServiceMapper.toDto(getData);
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
