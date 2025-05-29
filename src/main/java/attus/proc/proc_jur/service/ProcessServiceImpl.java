package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.handler.ProcessNotFound;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.util.Transform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@Service
public class ProcessServiceImpl implements ProcessService {

    private final PartyService partyService;
    private final ActionService actionService;
    private final ProcessRepository processRepository;

    public ProcessServiceImpl(PartyService partyService, ActionService actionService, ProcessRepository processRepository) {
        this.partyService = partyService;
        this.actionService = actionService;
        this.processRepository = processRepository;
    }

    @Override
    public String create(ProcessDto dto) {
        Process process = buildProcess(dto);
        processRepository.save(process);
        return process.getNumber();
    }

    @Override
    public void update(String processNumber, ProcessDto dto) {
        Process process = processRepository.findByNumber(processNumber)
                .orElseThrow(() ->
                        new ProcessNotFound("Process with number %s not found".formatted(processNumber))
                );
        process
                .setStatus(dto.getStatus())
                .setDescription(dto.getDescription())
                .setParties(getParties(dto.getParties()));
    }

    @Transactional
    @Override
    public void archive(Set<String> processesNumbers) {
        List<Process> processes = processRepository.findByNumberIn(processesNumbers);
        checkNotFoundProcesses(processesNumbers, processes);
        setProcessToAchieved(processes);
        processRepository.saveAll(processes);
    }

    @Override
    public Page<ProcessDto> get(ProcessFilter filter, Pageable pageable) {
        return Optional.ofNullable(filter.getStatus())
                .map(status -> processRepository.findByStatus(filter.getStatus(), pageable))
                .orElseGet(() -> Optional.ofNullable(filter.getOpeningDate())
                        .map(date -> processRepository.findByOpeningDate(filter.getOpeningDate(), pageable))
                        .orElseGet(() -> Optional.ofNullable(filter.getLegalEntityId())
                                .map(id -> processRepository.findByPartyLegalEntityId(filter.getLegalEntityId(), pageable))
                                .orElseThrow(() -> new IllegalArgumentException("No filter matched the required arguments")))
                )
                .map(Transform::toDto);
    }

    // ===== PRIVATE METHODS =====

    private Process buildProcess(final ProcessDto dto) {
        Process process = Process
                .builder()
                .number(UUID.randomUUID().toString())
                .openingDate(dto.getOpeningDate().atZone(ZoneId.of("America/Sao_Paulo")))
                .status(dto.getStatus())
                .description(dto.getDescription())
                .parties(getParties(Optional.ofNullable(dto.getParties())
                        .orElse(List.of())))
                .actions(Optional.ofNullable(dto.getActions())
                        .orElse(List.of())
                        .stream()
                        .map(actionService::createAction)
                        .toList())
                .build();
        if (process.getParties() != null)
            process.getParties().forEach(p -> p.setProcess(process));
        if (process.getActions() != null)
            process.getActions().forEach(a -> a.setProcess(process));
        return process;
    }

    private List<Party> getParties(final List<PartyDto> partyDtos) {
        if (partyDtos.isEmpty()) return List.of();

        List<Party> parties = getExistingParties(partyDtos);
        Map<String, Party> partyMap = getPartyMap(parties);
        List<PartyDto> newPartyDtos = getNewPartyDtos(partyDtos, partyMap);

        parties.addAll(partyService.getNewParties(newPartyDtos));
        parties.forEach(p -> p.getContact().setParty(p));
        return parties;
    }

    private List<Party> getExistingParties(final List<PartyDto> partyDtos) {
        Set<String> ids = partyDtos
                .stream()
                .map(PartyDto::getLegalEntityId)
                .collect(Collectors.toSet());

        return partyService.getExistingParties(ids);
    }

    private static Map<String, Party> getPartyMap(final List<Party> parties) {
        return parties
                .stream()
                .collect(Collectors.toMap(Party::getLegalEntityId, p -> p));
    }

    private static List<PartyDto> getNewPartyDtos(final List<PartyDto> partyDtos, final Map<String, Party> partyMap) {
        return partyDtos.stream()
                .filter(p -> !partyMap.containsKey(p.getLegalEntityId()))
                .toList();
    }

    private void checkNotFoundProcesses(final Set<String> processesNumbers, final List<Process> processes) {
        Map<String, Process> processMap = processes
                .stream()
                .collect(Collectors.toMap(Process::getNumber, p -> p));
        String notFound = processesNumbers.stream()
                .filter(s -> !processMap.containsKey(s))
                .collect(Collectors.joining(",", "[", "]"));
        if (!notFound.isBlank()) throw new ProcessNotFound("Processes with number %s not found".formatted(notFound));
    }

    private static void setProcessToAchieved(final List<Process> processes) {
        processes
                .forEach(p -> p
                        .setStatus(Status.ARCHIVED)
                );
    }
}
