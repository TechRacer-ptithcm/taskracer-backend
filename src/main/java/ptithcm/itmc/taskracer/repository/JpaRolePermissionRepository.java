package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaRole;

@Repository
public interface JpaRolePermissionRepository extends JpaRepository<JpaRole, Integer> {
    @Query(value = "SELECT EXISTS(" +
            "SELECT 1 " +
            "FROM role_permission rp " +
            "WHERE rp.role_id = :roleId " +
            "AND rp.permission_id = :permissionId)",
            nativeQuery = true)
    Boolean existsByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}
