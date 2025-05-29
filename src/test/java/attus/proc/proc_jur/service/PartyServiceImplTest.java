package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PartyServiceImplTest {

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private ContactServiceImpl contactService;

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
                .contact(
                        Contact
                                .builder()
                                .email("carol@attorney.com")
                                .phone("34123458765")
                                .build()
                )
                .build();
        Set<String> set = Set.of("86051398000100", "22943215678");
        when(partyRepository.findByLegalEntityIdIn(any())).thenReturn(List.of(party));
        List<Party> list = partyService.getExistingParties(set);
        assert list != null && !list.isEmpty();
        assert list.get(0).getFullName().equals(party.getFullName());
    }

    @Test
    void getNewParties() {
        ContactDto contactDto1 = new ContactDto(
                "email@email.com",
                "(11)91234-5678"
        );
        PartyDto dto1 = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                contactDto1
        );

        ContactDto contactDto2 = new ContactDto(
                "ana@doeinc.com",
                "22943215678"
        );
        PartyDto dto2 = new PartyDto(
                "Ana Doe Inc",
                "86.051.398/0001-00",
                PartyType.DEFENDANT,
                contactDto2
        );
        List<PartyDto> party = List.of(dto2, dto1);

        List<Party> list = partyService.getNewParties(party);
        assert list != null && !list.isEmpty();
        assert list.getFirst().getFullName().equals(dto2.getFullName());
        assert list.getLast().getFullName().equals(dto1.getFullName());
    }
}