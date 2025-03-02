package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.InviteStatus;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "team_invite_history", schema = "social")
@EqualsAndHashCode(callSuper = true)
public class JpaTeamInviteHistory extends Auditable{
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private JpaTeam team;

    @Enumerated(EnumType.STRING)
    private InviteStatus status;
}
