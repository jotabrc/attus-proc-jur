package attus.proc.proc_jur.repository;

import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.model.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProcessRepository extends JpaRepository<Process, Long> {

    Optional<Process> findByNumber(String number);
    List<Process> findByNumberIn(Set<String> number);
    Page<Process> findByOpeningDate(LocalDate date, Pageable pageable);
    Page<Process> findByStatus(Status status, Pageable pageable);

    @Query("SELECT p FROM tb_process p JOIN p.parties party WHERE party.legalEntityId = :legalEntityId ORDER BY p.number")
    Page<Process> findByPartyLegalEntityId(@Param("legalEntityId") String legalEntityId, Pageable pageable);

}
