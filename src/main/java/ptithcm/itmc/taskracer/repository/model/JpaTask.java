package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Priority;
import ptithcm.itmc.taskracer.repository.model.enumeration.ResourceType;
import ptithcm.itmc.taskracer.repository.model.enumeration.TaskStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task", schema = "content")
public class JpaTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private JpaTask parent;

    @Column(name = "resource_type", nullable = false)
    private ResourceType type;

    @Column(name = "resource_id", nullable = false)
    private UUID resourceId; // user_id or team_id

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private JpaUser owner;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Priority priority;

    private String description;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startAt;

    @Column(name = "due_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dueAt;

    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToMany
    @JoinTable(name = "task_assignees",
            schema = "content",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<JpaUser> users;

}
