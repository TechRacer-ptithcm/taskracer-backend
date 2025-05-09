package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.service.dto.ranking.RankingDto;
import ptithcm.itmc.taskracer.service.dto.ranking.TopUserDto;
import ptithcm.itmc.taskracer.service.processor.IRankingProcessor;
import ptithcm.itmc.taskracer.service.provider.IRankingProvider;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final IRankingProcessor processor;
    private final IRankingProvider provider;

    public List<RankingDto> getList() {
        return provider.getList();
    }

    public TopUserDto getCurrentTop(UUID userId) {
        return provider.getCurrentTop(userId);
    }

    public TopUserDto getPercentTop(UUID userId) {
        return provider.getPercentTop(userId);
    }

    public RankingDto getOne(UUID userId) {
        return provider.getOne(userId);
    }
}
