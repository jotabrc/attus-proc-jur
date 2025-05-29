package attus.proc.proc_jur.repository;

import attus.proc.proc_jur.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByLegalEntityId(String legalEntityId);
    List<Party> findByLegalEntityIdIn(Set<String> legalEntityIds);
}
