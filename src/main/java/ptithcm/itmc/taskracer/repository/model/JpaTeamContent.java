package ptithcm.itmc.taskracer.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
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
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name="team_id", nullable = false)
    private JpaTeam team;

    @Column(nullable = false)
    private String content;

    @Column(name="like_count", nullable = false)
    private Integer likeCount;

    @OneToMany(mappedBy = "contentId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JpaFileAttachment> fileAttachment;

}
