package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTaskAssignees;

import java.util.Optional;
import java.util.UUID;

public interface JpaTaskAssigneesRepository extends JpaRepository<JpaTaskAssignees, UUID> {
    Optional<JpaTaskAssignees> findByUserIdAndTaskId(UUID userId, UUID taskId);

    void deleteByTaskIdAndUserId(UUID taskId, UUID userId);
}
