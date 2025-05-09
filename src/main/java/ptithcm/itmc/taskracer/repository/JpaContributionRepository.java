package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaContribution;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaContributionRepository extends JpaRepository<JpaContribution, UUID> {
    List<JpaContribution> findAllByUserIdAndDateBetween(UUID userId, LocalDateTime from, LocalDateTime to);

    Optional<JpaContribution> findByDate(LocalDateTime date);
}
