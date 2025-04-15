package ptithcm.itmc.taskracer.repository.model;


import jakarta.persistence.*;
import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.Role;

import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles", schema = "social")
public class JpaRole extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role name;

    private String description;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            schema = "social",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<JpaPermission> permissions;

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "JpaRole{" + "id=" + id + ", name=" + name + '}';
    }
}
