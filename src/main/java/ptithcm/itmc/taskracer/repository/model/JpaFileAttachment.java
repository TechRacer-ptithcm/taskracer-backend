package ptithcm.itmc.taskracer.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "file_attachment", schema = "content")
public class JpaFileAttachment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    @JsonIgnore
    private JpaTeamContent contentId;

    @Column(name = "file_name")
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private long size;

    @Column(name = "md5_checksum")
    private String md5Checksum;
}
