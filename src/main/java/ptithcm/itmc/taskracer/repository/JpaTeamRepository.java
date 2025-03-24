package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeam;

import java.util.UUID;

public interface JpaTeamRepository extends JpaRepository<JpaTeam, UUID> {
}
