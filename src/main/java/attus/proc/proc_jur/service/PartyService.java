package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface PartyService {

    void attach(@NotNull Party party);
    Optional<Party> getExistingParties(@NotNull String legalEntityId);
    List<Party> getNewParties(@NotNull List<PartyDto> partyDtos);
}
