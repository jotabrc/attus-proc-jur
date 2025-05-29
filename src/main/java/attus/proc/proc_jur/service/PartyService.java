package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;

import java.util.List;
import java.util.Set;

public interface PartyService {

    List<Party> getExistingParties(List<PartyDto> dtos);
    List<Party> getNewParties(List<PartyDto> partyDtos);
}
