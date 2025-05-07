package ptithcm.itmc.taskracer.service.processor.eligibility.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.RoleInsufficientException;
import ptithcm.itmc.taskracer.repository.JpaRolePermissionRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamMemberRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.Permission;
import ptithcm.itmc.taskracer.service.validator.IEligibilityRoleValidator;

import java.util.UUID;

//TODO: Stage 2
@Component
@Slf4j(topic = "ROLE-VALIDATOR")
@RequiredArgsConstructor
public class DefaultRoleValidator implements IEligibilityRoleValidator {
    private final JpaRolePermissionRepository jpaRolePermissionRepository;
    private final JpaTeamMemberRepository jpaTeamMemberRepository;
    @Override
    public void validate(UUID userId, Integer teamId, Permission permission) {
        log.info("Validate user {} with team {}", userId, teamId);
        var getUser = jpaTeamMemberRepository.findByUserIdAndTeamId(userId, teamId);
        log.info("Get permission user: {}", getUser.get().getRole().getPermissions());
        if(getUser.isEmpty()) {
            throw new RoleInsufficientException("User: "+ userId +" does not have permission "+  permission );
        }
        getUser.get()
                .getRole()
                .getPermissions()
                .stream()
                .filter(getPermission -> getPermission.getName().equals(permission))
                .findFirst()
                .orElseThrow(() -> new RoleInsufficientException("User: "+ userId +" does not have permission "+  permission ));
    }
}