package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.repository.PartyRepository;
import attus.proc.proc_jur.util.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PartyServiceImplTest {

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private ContactServiceImpl contactService;

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
                .contact(
                        Contact
                                .builder()
                                .email("carol@attorney.com")
                                .phone("34123458765")
                                .build()
                )
                .build();
        Set<String> set = Set.of("86051398000100", "22943215678");
        PartyDto dto1 = new PartyDto(
                "Carol Fin",
                "86051398000100",
                PartyType.ATTORNEY,
                null
        );
        when(partyRepository.findByLegalEntityIdIn(any())).thenReturn(List.of(party));
        List<Party> list = partyService.getExistingParties(List.of(dto1));
        assert list != null && !list.isEmpty();
        assert list.getFirst().getFullName().equals(party.getFullName());
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

        Contact contact = Contact
                .builder()
                .id(0)
                .email("email@email.com")
                .phone("(11)91234-5678")
                .build();
        Party party = Party
                .builder()
                .fullName(dto1.getFullName())
                .legalEntityId(dto1.getLegalEntityId())
                .type(dto1.getType())
                .contact(contact)
                .build();

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
        List<PartyDto> list = List.of(dto1, dto2);

        when(entityMapper.toEntity(contactDto1)).thenReturn(contact);
        when(entityMapper.toEntity(dto1)).thenReturn(party);
        when(contactService.getOrCreateContact(contactDto1)).thenReturn(contact);
        List<Party> result = partyService.getNewParties(list);
        assert result != null && !result.isEmpty();
        assert result.getFirst().getFullName().equals(dto1.getFullName());
        assert result.getLast().getFullName().equals(dto2.getFullName());
    }
}