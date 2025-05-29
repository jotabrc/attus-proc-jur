package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final ContactService contactService;

    public PartyServiceImpl(PartyRepository partyRepository, ContactService contactService) {
        this.partyRepository = partyRepository;
        this.contactService = contactService;
    }

    @Override
    public List<Party> getExistingParties(Set<String> partyDtos) {
        return partyRepository.findByLegalEntityIdIn(partyDtos);
    }

    @Override
    public List<Party> getNewParties(List<PartyDto> partyDtos) {
        return partyDtos
                .stream()
                .map(this::createParty)
                .toList();
    }

    // ===== PRIVATE METHODS =====

    private Party createParty(PartyDto p) {
        return Party
                .builder()
                .fullName(p.getFullName())
                .legalEntityId(p.getLegalEntityId())
                .type(p.getType())
                .contact(
                        contactService.getOrCreateContact(p)
                )
                .build();
    }
}
