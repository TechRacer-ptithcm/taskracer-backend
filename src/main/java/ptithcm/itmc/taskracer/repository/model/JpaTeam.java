package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams", schema = "social")
@Entity
public class JpaTeam extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private JpaUser userId;

    @Column(nullable = false)
    private Visibility visibility;

    @ManyToMany
    @JoinTable(name="team_members",
                schema = "social",
                joinColumns = @JoinColumn(name= "team_id"),
                inverseJoinColumns = @JoinColumn(name="user_id"))
    private Set<JpaUser> users;
}
