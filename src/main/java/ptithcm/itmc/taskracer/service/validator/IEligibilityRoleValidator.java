package ptithcm.itmc.taskracer.service.validator;

import java.util.UUID;

//TODO: Stage 2
public interface IEligibilityRoleValidator {
    void validate(UUID userId, Integer teamId);
}
