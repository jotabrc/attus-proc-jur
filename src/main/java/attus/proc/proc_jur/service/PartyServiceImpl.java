package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import attus.proc.proc_jur.util.EntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final EntityMapper entityMapper;

    public PartyServiceImpl(PartyRepository partyRepository, EntityMapper entityMapper) {
        this.partyRepository = partyRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public void attach(@NotNull final Party party) {
        partyRepository.save(party);
    }

    @Override
    public Optional<Party> getExistingParties(@NotNull final String legalEntityId) {
        return partyRepository.findByLegalEntityId(legalEntityId);
    }

    // ===== PRIVATE METHODS =====

    @Override
    public List<Party> getNewParties(@NotNull final List<PartyDto> partyDtos) {
        return partyDtos
                .stream()
                .map(this::createParty)
                .toList();
    }

    // ===== PRIVATE METHODS =====

    private Party createParty(@NotNull final PartyDto p) {
        return entityMapper.toEntity(p);
    }
}
