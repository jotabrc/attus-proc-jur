package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import attus.proc.proc_jur.util.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PartyServiceImplTest {

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private PartyRepository partyRepository;

    @InjectMocks
    private PartyServiceImpl partyService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExistingParties() {
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .email("carol@email.com")
                .phone("12912345789")
                .build();
        Set<String> set = Set.of("86051398000100", "22943215678");
        PartyDto dto1 = new PartyDto(
                "Carol Fin",
                "86051398000100",
                PartyType.ATTORNEY,
                "carol@email.com",
                "12912345789"
        );
        when(partyRepository.findByLegalEntityId(any())).thenReturn(Optional.of(party));
        Optional<Party> result = partyService.getExistingParties(dto1.getLegalEntityId());
        assert result.isPresent();
        assert result.get().getFullName().equals(party.getFullName());
    }

    @Test
    void getNewParties() {
        PartyDto dto1 = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                "email@email.com",
                "(11)91234-5678"
        );
        Party party = Party
                .builder()
                .fullName(dto1.getFullName())
                .legalEntityId(dto1.getLegalEntityId())
                .type(dto1.getType())
                .email("email@email.com")
                .phone("(11)91234-5678")
                .build();

        PartyDto dto2 = new PartyDto(
                "Ana Doe Inc",
                "86.051.398/0001-00",
                PartyType.DEFENDANT,
                "ana@doeinc.com",
                "22943215678"
        );
        List<PartyDto> list = List.of(dto1, dto2);

        when(entityMapper.toEntity(dto1)).thenReturn(party);
        List<Party> result = partyService.getNewParties(list);
        assert result != null && !result.isEmpty();
        assert result.getFirst().getFullName().equals(dto1.getFullName());
        assert result.getLast().getFullName().equals(dto2.getFullName());
    }
}