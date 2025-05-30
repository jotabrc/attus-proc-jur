package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;

import java.util.List;
import java.util.Optional;

public interface PartyService {

    void attach(Party party);
    Optional<Party> getExistingParties(String legalEntityId);
    List<Party> getNewParties(List<PartyDto> partyDtos);
}
