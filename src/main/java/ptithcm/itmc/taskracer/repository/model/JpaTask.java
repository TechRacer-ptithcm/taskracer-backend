package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Priority;
import ptithcm.itmc.taskracer.repository.model.enumeration.ResourceType;
import ptithcm.itmc.taskracer.repository.model.enumeration.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks", schema = "content")
public class JpaTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    private JpaTask parent;

    @Column(name = "resource_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(name = "resource_id", nullable = false)
    private UUID resourceId; // user_id or team_id

    @Column(name = "owner_id", nullable = false)
    private UUID owner;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
