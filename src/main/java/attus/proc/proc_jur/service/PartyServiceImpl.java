package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import attus.proc.proc_jur.util.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyServiceImpl implements PartyService {

    private final EntityMapper entityMapper;
    private final PartyRepository partyRepository;

    public PartyServiceImpl(EntityMapper entityMapper, PartyRepository partyRepository) {
        this.entityMapper = entityMapper;
        this.partyRepository = partyRepository;
    }

    @Override
    public Optional<Party> getExistingParties(final String legalEntityId) {
        return partyRepository.findByLegalEntityId(legalEntityId);
    }

    // ===== PRIVATE METHODS =====

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
                .email(p.getEmail())
                .phone(p.getPhone())
                .build();
    }
}
