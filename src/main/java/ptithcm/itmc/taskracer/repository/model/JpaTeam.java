package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Visibility;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams", schema = "social")
@Entity
public class JpaTeam extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    private String name;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private JpaUser owner;

    @Column(nullable = false)
    private Visibility visibility;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "team_members",
            schema = "social",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<JpaUser> users;
}
