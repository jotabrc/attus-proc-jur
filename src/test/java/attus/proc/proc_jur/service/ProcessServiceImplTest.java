package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.*;
import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.handler.ProcessNotFound;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ContactRepository;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.util.Transform;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProcessServiceImplTest {

    @Mock
    private PartyServiceImpl partyService;

    @Mock
    private ActionServiceImpl actionService;

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ProcessServiceImpl processService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Contact contact = Contact
                .builder()
                .id(0)
                .email("email@email.com")
                .phone("11912345789")
                .build();
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .contact(contact)
                .build();
        Action action = Action
                .builder()
                .id(0)
                .type(ActionType.HEARING)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("Lorem Ipsum")
                .build();
        String number1 = UUID.randomUUID().toString();
        Process process = Process
                .builder()
                .id(0)
                .number(number1)
                .openingDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .status(Status.ACTIVE)
                .description("Lorem Ipsum")
                .parties(List.of(party))
                .actions(List.of(action))
                .build();

        ProcessDto dto = new ProcessDto(
                "null",
                null,
                Status.ACTIVE,
                "Description",
                null,
                null
        );

        when(contactRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.of(contact));
        when(processRepository.save(any())).thenReturn(process);
        String number = processService.create(dto);
        assert number.equals(number1);

        ActionDto actionDto = new ActionDto(
                ActionType.HEARING,
                "Hearing short description"
        );

        ContactDto contactDto = new ContactDto(
                "email@email.com",
                "(11)91234-5678"
        );
        PartyDto partyDto = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                contactDto
        );

        dto = new ProcessDto(
                "null",
                null,
                Status.ACTIVE,
                "Description",
                List.of(partyDto),
                List.of(actionDto)
        );

        when(partyService.getExistingParties(any())).thenReturn(new ArrayList<>(List.of(party)));
        when(actionService.createAction(actionDto)).thenReturn(action);
        number = processService.create(dto);
        assert number.equals(number1);
    }

    @Test
    void update() {
        Contact contact = Contact
                .builder()
                .id(0)
                .email("email@email.com")
                .phone("11912345789")
                .build();
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .contact(contact)
                .build();
        Action action = Action
                .builder()
                .id(0)
                .type(ActionType.HEARING)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("Lorem Ipsum")
                .build();
        Action action2 = Action
                .builder()
                .id(0)
                .type(ActionType.PETITION)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("PETITION short description")
                .build();
        String number1 = UUID.randomUUID().toString();
        ZonedDateTime date = ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo"));
        Process process = Process
                .builder()
                .id(0)
                .number(number1)
                .openingDate(date)
                .status(Status.ACTIVE)
                .description("Lorem Ipsum")
                .parties(List.of(party))
                .actions(List.of(action))
                .build();
        ActionDto actionDto = new ActionDto(
                ActionType.PETITION,
                "PETITION short description"
        );

        ContactDto contactDto = new ContactDto(
                "email@email.com",
                "(11)91234-5678"
        );
        PartyDto partyDto = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                contactDto
        );

        ProcessDto dto = new ProcessDto(
                "null",
                null,
                Status.ACTIVE,
                "Description",
                List.of(partyDto),
                List.of(actionDto)
        );

        Contact contact2 = Contact
                .builder()
                .id(0)
                .email("john@email.com")
                .phone("12912345789")
                .build();
        Party party2 = Party
                .builder()
                .fullName(partyDto.getFullName())
                .legalEntityId(partyDto.getLegalEntityId())
                .type(PartyType.AUTHOR)
                .contact(contact2)
                .build();
        when(processRepository.findByNumber(any())).thenReturn(Optional.of(process));
        when(processRepository.save(any())).thenReturn(process);
        when(actionService.createAction(actionDto)).thenReturn(action2);
        when(partyService.getNewParties(dto.getParties())).thenReturn(List.of(party2));

        processService.update(number1, dto);
        assert process.getOpeningDate().equals(date);
        assert process.getStatus().equals(dto.getStatus());
        assert process.getDescription().equals(dto.getDescription());
        assert process.getParties()
                .stream()
                .allMatch(p -> process.getParties()
                        .stream()
                        .anyMatch(p1 -> p.getLegalEntityId().equals(p1.getLegalEntityId())));
        assert process.getActions()
                .stream()
                .allMatch(a -> process.getActions()
                        .stream()
                        .anyMatch(a::equals));

        actionDto = new ActionDto(
                ActionType.SENTENCE,
                "SENTENCE short description"
        );
        Action action3 = Action
                .builder()
                .id(0)
                .type(ActionType.SENTENCE)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("SENTENCE short description")
                .build();
        dto = new ProcessDto(
                "null",
                null,
                Status.SUSPENDED,
                "Description",
                List.of(),
                List.of(actionDto)
        );

        when(actionService.createAction(actionDto)).thenReturn(action2);
        processService.update(number1, dto);
        assert process.getOpeningDate().equals(date);
        assert process.getStatus().equals(dto.getStatus());
        assert process.getDescription().equals(dto.getDescription());
        assert process.getParties()
                .stream()
                .allMatch(p -> process.getParties()
                        .stream()
                        .anyMatch(p1 -> p.getLegalEntityId().equals(p1.getLegalEntityId())));
        assert process.getActions()
                .stream()
                .allMatch(a -> process.getActions()
                        .stream()
                        .anyMatch(a::equals));
    }

    @Test
    void archive() {
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

        Action action = Action
                .builder()
                .id(0)
                .type(ActionType.HEARING)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("Lorem Ipsum")
                .build();

        String number1 = UUID.randomUUID().toString();
        String number2 = UUID.randomUUID().toString();
        Set<String> list = Set.of(number1, number2);

        Process process = Process
                .builder()
                .id(0)
                .number(number1)
                .openingDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .status(Status.ACTIVE)
                .description("Lorem Ipsum")
                .parties(List.of(party))
                .actions(List.of(action))
                .build();
        when(processRepository.findByNumberIn(any())).thenReturn(List.of(process));
        when(processRepository.saveAll(List.of(process))).thenReturn(List.of(process));
        assertThrows(ProcessNotFound.class, () -> processService.archive(list));

        Process process2 = Process
                .builder()
                .id(0)
                .number(number2)
                .openingDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .status(Status.ACTIVE)
                .description("Lorem Ipsum")
                .parties(List.of(party))
                .actions(List.of(action))
                .build();
        when(processRepository.findByNumberIn(any())).thenReturn(List.of(process, process2));
        when(processRepository.saveAll(List.of(process))).thenReturn(List.of(process, process2));
        processService.archive(list);
    }

    @Test
    void get() {
        ProcessFilter filterStatus = new ProcessFilter(
            Status.ACTIVE,
                null,
                null
        );
        ProcessFilter filterDate = new ProcessFilter(
                null,
                LocalDate.now(),
                null
        );
        ProcessFilter filterId = new ProcessFilter(
                null,
                null,
                "86051398000100"
        );

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

        Action action = Action
                .builder()
                .id(0)
                .type(ActionType.HEARING)
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description("Lorem Ipsum")
                .build();
        Process process = Process
                .builder()
                .id(0)
                .number(UUID.randomUUID().toString())
                .openingDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .status(Status.ACTIVE)
                .description("Lorem Ipsum")
                .parties(List.of(party))
                .actions(List.of(action))
                .build();

        Pageable pageable = PageRequest.of(0, 5);
        Page<Process> page = new PageImpl<>(List.of(process), pageable, 1);

        when(processRepository.findByStatus(any(), any())).thenReturn(page);
        when(processRepository.findByOpeningDate(any(), any())).thenReturn(page);
        when(processRepository.findByPartyLegalEntityId(any(), any())).thenReturn(page);

        var result = processService.get(filterStatus, pageable);
        assert result.hasContent();
        assert result.get().allMatch(p -> filterStatus.getStatus().equals(process.getStatus()));


        result = processService.get(filterDate, pageable);
        assert result.hasContent();
        assert result.get().anyMatch(p -> filterDate.getOpeningDate().equals(process.getOpeningDate().toLocalDate()));

        result = processService.get(filterId, pageable);
        assert result.hasContent();
        assert result.get().anyMatch(p -> filterId.getLegalEntityId().equals(process.getParties().getFirst().getLegalEntityId()));
    }
}