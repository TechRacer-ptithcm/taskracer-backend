package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTier;

public interface JpaTierRepository extends JpaRepository<JpaTier, Integer> {
    JpaTier getById(Integer id);
}
