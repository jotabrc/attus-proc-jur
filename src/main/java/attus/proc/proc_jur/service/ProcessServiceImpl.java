package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.handler.OperationDeniedException;
import attus.proc.proc_jur.handler.ProcessNotFoundException;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.util.EntityMapper;
import attus.proc.proc_jur.util.DtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl implements ProcessService {

    private final PartyService partyService;
    private final EntityMapper entityMapper;
    private final ProcessRepository processRepository;

    public ProcessServiceImpl(PartyService partyService, EntityMapper entityMapper, ProcessRepository processRepository) {
        this.partyService = partyService;
        this.entityMapper = entityMapper;
        this.processRepository = processRepository;
    }

    @Override
    public String create(final ProcessDto dto) {
        Process process = entityMapper.toEntity(dto);
        process = processRepository.save(process);
        return process.getNumber();
    }

    @Override
    public void update(final String processNumber, final ProcessDto dto) {
        Process process = getProcessByProcessNumber(processNumber);
        checkProcessStatus(process.getStatus());
        updateProcess(dto, process);
        processRepository.save(process);
    }

    @Transactional
    @Override
    public void archive(final Set<String> processesNumbers) {
        List<Process> processes = getProcessByNumberIn(processesNumbers);
        checkNotFoundProcesses(processesNumbers, processes);
        setProcessToAchieved(processes);
        processRepository.saveAll(processes);
    }

    @Override
    public Page<ProcessDto> get(final ProcessFilter filter, final Pageable pageable) {
        return Optional.ofNullable(filter.getStatus())
                .map(status -> processRepository.findByStatus(filter.getStatus(), pageable))
                .orElseGet(() -> Optional.ofNullable(filter.getOpeningDate())
                        .map(date -> processRepository.findByOpeningDate(filter.getOpeningDate(), pageable))
                        .orElseGet(() -> Optional.ofNullable(filter.getLegalEntityId())
                                .map(id -> processRepository.findByPartyLegalEntityId(filter.getLegalEntityId(), pageable))
                                .orElseThrow(() -> new IllegalArgumentException("No filter matched the required arguments")))
                )
                .map(DtoMapper::toDto);
    }

    // ===== PRIVATE METHODS =====

    @Deprecated(forRemoval = true)
    private Process buildProcess(final ProcessDto dto) {
        Process process = Process
                .builder()
                .number(UUID.randomUUID().toString())
                .openingDate(Optional.ofNullable(dto.getOpeningDate())
                        .orElse(LocalDateTime.now())
                        .atZone(ZoneId.of("America/Sao_Paulo")))
                .status(dto.getStatus())
                .description(dto.getDescription())
                .parties(getParties(Optional.ofNullable(dto.getParties())
                        .orElse(List.of())))
                .actions(Optional.ofNullable(dto.getActions())
                        .orElse(List.of())
                        .stream()
                        .map(entityMapper::toEntity)
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

        List<Party> parties = partyService.getExistingParties(partyDtos);
        Map<String, Party> partyMap = getPartyMap(parties);
        List<PartyDto> newPartyDtos = getNewPartyDtos(partyDtos, partyMap);

        parties.addAll(partyService.getNewParties(newPartyDtos));
        parties.forEach(p -> p.getContact().setParty(p));
        return parties;
    }

    private static Map<String, Party> getPartyMap(final List<Party> parties) {
        if (parties == null || parties.isEmpty()) return Collections.emptyMap();
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
        if (!notFound.isBlank() && !notFound.equals("[]")) throw new ProcessNotFoundException("Processes with number %s not found".formatted(notFound));
    }

    private static void setProcessToAchieved(final List<Process> processes) {
        processes
                .forEach(p -> p
                        .setStatus(Status.ARCHIVED)
                );
    }

    private void checkProcessStatus(final Status status) {
        if (status.equals(Status.ARCHIVED)) throw new OperationDeniedException("An Archived process(es) cannot be updated");
        else if (status.equals(Status.SUSPENDED)) throw new OperationDeniedException("A Suspended process(es) cannot be updated");
    }

    private List<Process> getProcessByNumberIn(final Set<String> processesNumbers) {
        List<Process> processes = processRepository.findByNumberIn(processesNumbers);
        if (processes == null || processes.isEmpty()) throw new ProcessNotFoundException("No Process(es) found with number('s) %s"
                .formatted(Optional.ofNullable(processesNumbers)
                        .orElse(Collections.emptySet())
                        .stream()
                        .collect(Collectors
                                .joining(",", "[", "]"))));
        return processes;
    }

    private void updateProcess(final ProcessDto dto, final Process process) {
        List<Action> newActionList = new ArrayList<>(Optional.ofNullable(process.getActions()).orElse(List.of()));
        newActionList.addAll(Optional.ofNullable(dto.getActions())
                .orElse(List.of())
                .stream()
                .map(entityMapper::toEntity)
                .toList());

        List<Party> newPartyList = new ArrayList<>(Optional.ofNullable(process.getParties()).orElse(List.of()));
        newPartyList.addAll(getParties(dto.getParties()));
        process
                .setStatus(dto.getStatus())
                .setDescription(dto.getDescription())
                .setParties(newPartyList)
                .setActions(newActionList);
    }

    private Process getProcessByProcessNumber(final String processNumber) {
        return processRepository.findByNumber(processNumber)
                .orElseThrow(() ->
                        new ProcessNotFoundException("Process with number %s not found".formatted(processNumber))
                );
    }
}
