package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "rank_data", schema = "social")
public class JpaRankData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Rank name;

    @NotNull
    private Integer order;

    @NotNull
    @Column(name = "star_per_tier")
    private Integer starPerTier;

    @NotNull
    @Column(name = "point_per_star")
    private Integer pointPerStar;
}
