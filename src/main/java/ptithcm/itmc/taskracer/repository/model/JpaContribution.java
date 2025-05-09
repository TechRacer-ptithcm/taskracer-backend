package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contribution", schema = "social")
public class JpaContribution extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private JpaUser user;

    @Column(name = "minutes", nullable = false)
    private Integer minute;
}
