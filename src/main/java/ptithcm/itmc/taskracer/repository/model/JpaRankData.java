package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.Rank;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "rank_data", schema = "social")
public class JpaRankData extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Rank name;

    @NotNull
    private Long score;
}
