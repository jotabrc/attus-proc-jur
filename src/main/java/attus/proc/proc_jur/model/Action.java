package attus.proc.proc_jur.model;

import attus.proc.proc_jur.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType type;

    @Column(name = "registration_date", nullable = false)
    private ZonedDateTime registrationDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @Version
    private Long version;
}
