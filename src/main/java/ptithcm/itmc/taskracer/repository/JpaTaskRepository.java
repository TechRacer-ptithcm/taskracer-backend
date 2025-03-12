package ptithcm.itmc.taskracer.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTask;

public interface JpaTaskRepository extends JpaRepository<JpaTask, Integer> {
    Page<JpaTask> findAll(@NonNull Pageable pageable);
}
