package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaTeamInviteHistory;
import ptithcm.itmc.taskracer.repository.model.enumeration.InviteStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTeamInviteHistoryRepository extends JpaRepository<JpaTeamInviteHistory, UUID> {
    Optional<JpaTeamInviteHistory> findByTeamIdAndUserIdAndStatus(Integer teamId, UUID user, InviteStatus status);
}
