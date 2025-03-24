package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaRole;

public interface JpaRolePermissionRepository extends JpaRepository<JpaRole, Integer> {
}
