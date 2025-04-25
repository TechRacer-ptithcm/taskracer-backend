package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.Permission;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions", schema = "social")
public class JpaPermission extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Permission name;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<JpaRole> roles;

    @Override
    public String toString() {
        return "JpaPermission{" + "id=" + id + ", name=" + name + '}';
    }
}
