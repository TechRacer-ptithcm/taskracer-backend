package ptithcm.itmc.taskracer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaRankData;

public interface JpaRankDataRepository extends JpaRepository<JpaRankData, Integer> {
}
