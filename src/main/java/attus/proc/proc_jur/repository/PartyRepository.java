package attus.proc.proc_jur.repository;

import attus.proc.proc_jur.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByLegalEntityId(String legalEntityId);
}
