package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaTier;

@Repository
public interface JpaTierRepository extends JpaRepository<JpaTier, Integer> {
    JpaTier getById(Integer id);
}
