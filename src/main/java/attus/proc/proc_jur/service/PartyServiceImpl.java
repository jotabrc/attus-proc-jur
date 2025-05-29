package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import attus.proc.proc_jur.util.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartyServiceImpl implements PartyService {

    private final EntityMapper entityMapper;
    private final ContactService contactService;
    private final PartyRepository partyRepository;

    public PartyServiceImpl(EntityMapper entityMapper, ContactService contactService, PartyRepository partyRepository) {
        this.entityMapper = entityMapper;
        this.contactService = contactService;
        this.partyRepository = partyRepository;
    }

    @Override
    public List<Party> getExistingParties(final List<PartyDto> dtos) {
        Set<String> ids = getIds(dtos);
        return partyRepository.findByLegalEntityIdIn(ids);
    }

    // ===== PRIVATE METHODS =====

    private static Set<String> getIds(List<PartyDto> dtos) {
        return dtos
                .stream()
                .map(PartyDto::getLegalEntityId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Party> getNewParties(final List<PartyDto> partyDtos) {
        return partyDtos
                .stream()
                .map(this::createParty)
                .toList();
    }

    // ===== PRIVATE METHODS =====

    private Party createParty(final PartyDto p) {
        return Party
                .builder()
                .fullName(p.getFullName())
                .legalEntityId(p.getLegalEntityId())
                .type(p.getType())
                .contact(
                        contactService.getOrCreateContact(p.getContact())
                )
                .build();
    }
}
