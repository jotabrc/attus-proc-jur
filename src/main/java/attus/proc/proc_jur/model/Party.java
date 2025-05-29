package attus.proc.proc_jur.model;

import attus.proc.proc_jur.enums.PartyType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_party")
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

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @Version
    private Long version;
}
