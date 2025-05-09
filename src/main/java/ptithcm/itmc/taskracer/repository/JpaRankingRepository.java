package ptithcm.itmc.taskracer.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.itmc.taskracer.repository.model.JpaRanking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaRankingRepository extends JpaRepository<JpaRanking, UUID> {
    Optional<JpaRanking> findByUserId(UUID userId);

    @Query("SELECT r from JpaRanking r ORDER BY r.score DESC")
    List<JpaRanking> getListTop(Pageable pageable);

    @Query(value = """
                    WITH current_score AS (
                            SELECT score FROM social.rankings WHERE user_id = :userId
                        ),
                        rank_position AS (
                            SELECT COUNT(*) + 1 AS position
                            FROM social.rankings r, current_score cs
                            WHERE r.score > cs.score
                        ),
                        total_count AS (
                            SELECT COUNT(*) AS total FROM social.rankings
                        )
                        SELECT 100.0 * (total - position + 1) / total
                        FROM rank_position, total_count
            """, nativeQuery = true)
    Double getPercentCurrentTop(@Param("userId") UUID userId);

    @Query(value = """
            SELECT COUNT(*) + 1
            FROM social.rankings r
            WHERE r.score > (
                SELECT score FROM social.rankings WHERE user_id = :userId
            )
            """, nativeQuery = true)
    Integer getCurrentTop(UUID userId);
}
