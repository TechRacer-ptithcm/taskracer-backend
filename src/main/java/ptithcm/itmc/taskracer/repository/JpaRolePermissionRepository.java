package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaRole;

@Repository
public interface JpaRolePermissionRepository extends JpaRepository<JpaRole, Integer> {
}
