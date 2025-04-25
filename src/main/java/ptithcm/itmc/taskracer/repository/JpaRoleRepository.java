package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaRole;
import ptithcm.itmc.taskracer.repository.model.enumeration.Role;

public interface JpaRoleRepository extends JpaRepository<JpaRole, Integer> {
    JpaRole findByName(Role name);
}
