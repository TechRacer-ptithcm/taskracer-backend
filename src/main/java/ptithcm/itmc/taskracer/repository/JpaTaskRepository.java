package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTask;
import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTaskRepository extends JpaRepository<JpaTask, UUID> {
    List<JpaTask> findByOwnerId(UUID ownerId);

    Optional<JpaTask> findByIdAndOwnerId(UUID id, UUID ownerId);

    UUID owner(JpaUser owner);
}
