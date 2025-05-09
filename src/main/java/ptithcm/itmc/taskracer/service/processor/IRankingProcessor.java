package ptithcm.itmc.taskracer.service.processor;

import java.util.UUID;

public interface IRankingProcessor {
    void handle(UUID userId, Integer point);
}
