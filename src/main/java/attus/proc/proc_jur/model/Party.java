package attus.proc.proc_jur.model;

import attus.proc.proc_jur.enums.PartyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_party")
@Table(indexes = {
        @Index(name = "idx_legal_entity_id", columnList = "legal_entity_id", unique = true)
})
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "legal_entity_id", length = 20, nullable = false)
    private String legalEntityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyType type;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "tb_party_process",
            joinColumns = @JoinColumn(name = "party_id"),
            inverseJoinColumns = @JoinColumn(name = "process_id")
    )
    private List<Process> processes;

    @Version
    private Long version;
}
