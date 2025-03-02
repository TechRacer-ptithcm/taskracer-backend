package ptithcm.itmc.taskracer.repository.model;


import jakarta.persistence.*;
import lombok.*;
import ptithcm.itmc.taskracer.repository.model.enumeration.Role;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role", schema = "social")
public class JpaRole extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role name;

    private String description;

    @ManyToMany
    @JoinTable(name = "role_permission",
            schema = "social",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<JpaPermission> permissions;


}
