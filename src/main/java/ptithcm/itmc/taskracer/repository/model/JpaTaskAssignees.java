package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "task_assignees", schema = "content")
public class JpaTaskAssignees {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private JpaTask taskId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
