package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;

import java.util.List;
import java.util.UUID;

public interface IContributionProvider {
    List<ContributionDto> getAll(UUID userId);
}
