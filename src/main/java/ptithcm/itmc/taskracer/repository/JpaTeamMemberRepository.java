package ptithcm.itmc.taskracer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTeamMember;

import java.util.UUID;

public interface JpaTeamMemberRepository extends JpaRepository<JpaTeamMember, UUID> {
    Page<JpaTeamMember> findAllByTeamId(Integer teamId, Pageable pageable);
}
