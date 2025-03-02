package ptithcm.itmc.taskracer.repository.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "team_members", schema = "social")
public class JpaTeamMember extends Auditable{
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private JpaTeam team;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private JpaRole role;
}
