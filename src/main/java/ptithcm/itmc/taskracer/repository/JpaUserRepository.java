package ptithcm.itmc.taskracer.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, UUID> {
    Optional<JpaUser> findByUsername(String username);

    Optional<JpaUser> findByEmail(String email);

    Page<JpaUser> findAll(@NonNull Pageable pageable);

}
