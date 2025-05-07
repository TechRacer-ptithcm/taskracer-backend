package ptithcm.itmc.taskracer.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTeamContentRepository extends JpaRepository<JpaTeamContent, UUID> {
    Page<JpaTeamContent> findAllByTeamId(Integer teamId, Pageable pageable);

    Optional<JpaTeamContent> findById(UUID id);
}
