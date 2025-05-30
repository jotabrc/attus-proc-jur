package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.*;
import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.handler.ProcessNotFoundException;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.service.filter.ProcessFilterSelector;
import attus.proc.proc_jur.util.EntityMapper;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ProcessServiceImplTest {

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private FormattingService formattingService;

    @Mock
    private PartyServiceImpl partyService;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private ProcessServiceImpl processService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .email("email@email.com")
                .phone("11912345789")
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

        RequestProcessDto dto = new RequestProcessDto(
                null,
                Status.ACTIVE,
                "Description",
                null,
                null
        );

        doNothing().when(formattingService).removeFormatting(anyList());
        when(processRepository.save(any())).thenReturn(process);
        when(entityMapper.toEntity(dto)).thenReturn(process);
        String number = processService.create(dto);
        assert number != null;

        ActionDto actionDto = new ActionDto(
                ActionType.HEARING,
                "Hearing short description"
        );
        PartyDto partyDto = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                "email@email.com",
                "(11)91234-5678"
        );

        dto = new RequestProcessDto(
                null,
                Status.ACTIVE,
                null,
                List.of(partyDto),
                List.of(actionDto)
        );

        when(processRepository.save(any())).thenReturn(process);
        when(partyService.getExistingParties(any())).thenReturn(Optional.of(party));
        when(entityMapper.toEntity(dto)).thenReturn(process);
        number = processService.create(dto);
        assert number != null;
    }

    @Test
    void update() {
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .email("email@email.com")
                .phone("11912345789")
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
        PartyDto partyDto = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                "email@email.com",
                "(11)91234-5678"
        );

        RequestProcessDto dto = new RequestProcessDto(
                null,
                Status.ACTIVE,
                "Description",
                List.of(partyDto),
                List.of(actionDto)
        );
        Party party2 = Party
                .builder()
                .fullName(partyDto.getFullName())
                .legalEntityId(partyDto.getLegalEntityId())
                .type(PartyType.AUTHOR)
                .email("john@email.com")
                .phone("12912345789")
                .build();
        doNothing().when(formattingService).removeFormatting(anyList());
        when(processRepository.findByNumber(any())).thenReturn(Optional.of(process));
        when(processRepository.save(any())).thenReturn(process);
        when(entityMapper.toEntity(actionDto)).thenReturn(action2);
        when(partyService.getNewParties(dto.getParties())).thenReturn(List.of(party2));
        when(partyService.getExistingParties(anyString())).thenReturn(Optional.of(party2));

        processService.update(number1, dto);
        assert process.getOpeningDate().equals(date);
        assert process.getStatus().equals(dto.getStatus());
        assert process.getDescription().equals(dto.getDescription());

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
        dto = new RequestProcessDto(
                null,
                Status.SUSPENDED,
                "Description",
                List.of(),
                List.of(actionDto)
        );

        when(entityMapper.toEntity(actionDto)).thenReturn(action2);
        processService.update(number1, dto);
        assert process.getOpeningDate().equals(date);
        assert process.getStatus().equals(dto.getStatus());
        assert process.getDescription().equals(dto.getDescription());
    }

    @Test
    void archive() {
        Party party = Party
                .builder()
                .fullName("Carol Fin")
                .legalEntityId("86051398000100")
                .type(PartyType.ATTORNEY)
                .email("john@email.com")
                .phone("12912345789")
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
        assertThrows(ProcessNotFoundException.class, () -> processService.archive(list));

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
                .email("john@email.com")
                .phone("12912345789")
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