package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;

    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public void attach(final Party party) {
        partyRepository.save(party);
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
