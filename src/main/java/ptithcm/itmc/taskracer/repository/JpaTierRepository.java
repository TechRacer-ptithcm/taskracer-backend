package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTier;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

public interface JpaTierRepository extends JpaRepository<JpaTier, Integer> {
    JpaTier getById(Tier tier);
}
