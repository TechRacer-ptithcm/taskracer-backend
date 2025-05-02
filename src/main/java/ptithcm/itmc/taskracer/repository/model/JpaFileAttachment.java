package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_attachment", schema = "content")
public class JpaFileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private  String fileUrl;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private JpaTeamContent contentId;
}
