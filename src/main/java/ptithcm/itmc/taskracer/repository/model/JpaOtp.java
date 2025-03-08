package ptithcm.itmc.taskracer.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otp", schema = "social")
@Deprecated
public class JpaOtp extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String otp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private JpaUser user;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;
}
