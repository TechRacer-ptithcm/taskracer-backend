package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaFileAttachment;

import java.util.Optional;
import java.util.UUID;

public interface JpaFileAttachmentRepository extends JpaRepository<JpaFileAttachment, UUID> {
    Optional<JpaFileAttachment> findByName(String name);
}
