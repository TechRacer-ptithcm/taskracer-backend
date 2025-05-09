package ptithcm.itmc.taskracer.service.processor;

import java.util.UUID;

public interface IContributionProcessor {
    void handle(UUID userId, Long from, Long to);
}
