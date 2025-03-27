package ptithcm.itmc.taskracer.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

import java.util.Optional;
import java.util.UUID;

public interface JpaTeamRepository extends JpaRepository<JpaTeam, UUID> {
    Optional<JpaTeam> findBySlug(String slug);

    Page<JpaTeam> findAllByVisibility(@NonNull Pageable pageable, Visibility visibility);
}
