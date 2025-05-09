package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;
import ptithcm.itmc.taskracer.service.provider.IContributionProvider;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IContributionProvider contributionProvider;

    public List<ContributionDto> getContributionTable(UUID userId) {
        return contributionProvider.getAll(userId);
    }
}
