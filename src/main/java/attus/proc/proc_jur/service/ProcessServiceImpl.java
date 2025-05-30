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
import attus.proc.proc_jur.util.DtoMapper;
import attus.proc.proc_jur.util.EntityMapper;
import attus.proc.proc_jur.util.RemoveFormatting;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
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
        createProcess(dto, process);
        removeFormatting(process.getParties());
        process = processRepository.save(process);
        return process.getNumber();
    }

    @Override
    public void update(final String processNumber, final ProcessDto dto) {
        Process process = getProcessByProcessNumber(processNumber);
        checkProcessStatus(process.getStatus());
        updateProcess(dto, process);
        removeFormatting(process.getParties());
        processRepository.save(process);
    }

    @Transactional
    @Override
    public void archive(final Set<String> processesNumbers) {
        List<Process> processes = getProcessByNumberIn(processesNumbers);
        checkProcessStatus(processes);
        checkNotFoundProcesses(processesNumbers, processes);
        setProcessToAchieved(processes);
        processRepository.saveAll(processes);
    }

    @Override
    public Page<ProcessDto> get(final ProcessFilter filter, final Pageable pageable) {
        return  Optional.ofNullable(filter.getStatus())
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

    private void createProcess(ProcessDto dto, Process process) {
        process.setNumber(UUID.randomUUID().toString());
        List<Party> parties = Optional.ofNullable(dto.getParties())
                .orElse(List.of())
                .stream()
                .map(p -> getOrCreateParty(process, p))
                .toList();
        Optional.ofNullable(process.getActions())
                .orElse(List.of())
                .forEach(a -> a.setProcess(process));
        process.setParties(parties);
    }

    private Party getOrCreateParty(Process process, PartyDto p) {
        return partyService.getExistingParties(p.getLegalEntityId()).orElseGet(() -> {
            var result = entityMapper.toEntity(p);
            result.setProcesses(List.of(process));
            return result;
        });
    }

    public void removeFormatting(@NotNull final List<Party> parties) {
        parties.forEach(p -> {
            p.setLegalEntityId(RemoveFormatting.remove(p.getLegalEntityId()));
            p.setPhone(RemoveFormatting.remove(p.getPhone()));
        });
    }

    private void checkNotFoundProcesses(@NotNull final Set<String> processesNumbers, @NotNull final List<Process> processes) {
        Map<String, Process> processMap = processes
                .stream()
                .collect(Collectors.toMap(Process::getNumber, p -> p));
        String notFound = processesNumbers.stream()
                .filter(s -> !processMap.containsKey(s))
                .collect(Collectors.joining(",", "[", "]"));
        if (!notFound.isBlank() && !notFound.equals("[]"))
            throw new ProcessNotFoundException("Process(es) with number %s not found".formatted(notFound));
    }

    private static void setProcessToAchieved(final List<Process> processes) {
        processes
                .forEach(p -> p
                        .setStatus(Status.ARCHIVED)
                );
    }

    private void checkProcessStatus(@NotNull final List<Process> processes) {
        processes.forEach(p -> checkProcessStatus(p.getStatus()));
    }

    private void checkProcessStatus(@NotNull final Status status) {
        if (status.equals(Status.ARCHIVED))
            throw new OperationDeniedException("An Archived process(es) cannot be updated");
        else if (status.equals(Status.SUSPENDED))
            throw new OperationDeniedException("A Suspended process(es) cannot be updated");
    }

    private List<Process> getProcessByNumberIn(@NotNull final Set<String> processesNumbers) {
        List<Process> processes = processRepository.findByNumberIn(processesNumbers);
        if (processes == null || processes.isEmpty())
            throw new ProcessNotFoundException("No Process(es) found with number('s) %s"
                    .formatted(Optional.ofNullable(processesNumbers)
                            .orElse(Collections.emptySet())
                            .stream()
                            .collect(Collectors
                                    .joining(",", "[", "]"))));
        return processes;
    }

    private void updateProcess(@NotNull final ProcessDto dto, @NotNull final Process process) {
//        List<Action> newActionList = new ArrayList<>(Optional.ofNullable(process.getActions()).orElse(List.of()));
//        newActionList.addAll(Optional.ofNullable(dto.getActions())
//                .orElse(List.of())
//                .stream()
//                .map(e -> {
//                    Action action = entityMapper.toEntity(e);
//                    action.setProcess(process);
//                    return action;
//                })
//                .toList());


        process
                .setStatus(dto.getStatus())
                .setDescription(dto.getDescription())
                .setActions(Stream
                        .concat(Optional.ofNullable(dto.getActions())
                                .orElse(List.of())
                                .stream()
                                .map(e -> {
                                    Action action = entityMapper.toEntity(e);
                                    action.setProcess(process);
                                    return action;
                                }), process.getActions().stream())
                        .toList())
                .setParties(Optional.ofNullable(dto.getParties())
                        .orElse(List.of())
                        .stream()
                        .map(e -> getOrCreateParty(process, e))
                        .toList());
    }

    private Process getProcessByProcessNumber(@NotNull final String processNumber) {
        return processRepository.findByNumber(processNumber)
                .orElseThrow(() ->
                        new ProcessNotFoundException("Process with number %s not found".formatted(processNumber))
                );
    }
}
