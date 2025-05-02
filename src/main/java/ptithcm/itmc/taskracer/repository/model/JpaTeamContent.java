package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "team_contents", schema = "content")
public class JpaTeamContent extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private JpaUser userId;

    @ManyToOne
    @JoinColumn(name="team_id", nullable = false)
    private JpaTeam teamId;

    @Column(nullable = false)
    private String content;

    @Column(name="like_count", nullable = false)
    private Integer likeCount;

    @OneToMany(mappedBy = "contentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaFileAttachment> fileAttachment;

}
