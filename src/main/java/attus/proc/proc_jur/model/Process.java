package attus.proc.proc_jur.model;

import attus.proc.proc_jur.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_process")
@Table(indexes = {
        @Index(name = "idx_process_number", columnList = "number", unique = true),
        @Index(name = "idx_status", columnList = "status", unique = true),
        @Index(name = "idx_opening_date", columnList = "opening_date", unique = true)
})
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 36, unique = true, nullable = false)
    private String number;

    @Column(name = "opening_date", nullable = false)
    private ZonedDateTime openingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "processes", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Party> parties;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Action> actions;

    @Version
    private Long version;
}
