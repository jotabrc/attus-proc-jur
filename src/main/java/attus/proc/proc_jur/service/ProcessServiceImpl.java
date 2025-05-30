package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.dto.RequestProcessDto;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.handler.OperationDeniedException;
import attus.proc.proc_jur.handler.ProcessNotFoundException;
import attus.proc.proc_jur.model.Action;
import attus.proc.proc_jur.model.Party;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.service.filter.FilterStrategy;
import attus.proc.proc_jur.service.filter.ProcessFilterSelector;
import attus.proc.proc_jur.util.EntityMapper;
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

    private final ProcessRepository processRepository;

    private final PartyService partyService;
    private final FormattingService formattingService;
    private final EntityMapper entityMapper;

    public ProcessServiceImpl(ProcessRepository processRepository, PartyService partyService, FormattingService formattingService, EntityMapper entityMapper) {
        this.processRepository = processRepository;
        this.partyService = partyService;
        this.formattingService = formattingService;
        this.entityMapper = entityMapper;
    }

    @Transactional
    @Override
    public String create(@NotNull final RequestProcessDto dto) {
        Process process = entityMapper.toEntity(dto);
        createProcess(dto, process);
        formattingService.removeFormatting(process.getParties());
        process = processRepository.save(process);
        return process.getNumber();
    }

    @Transactional
    @Override
    public void update(@NotNull final String processNumber, @NotNull final RequestProcessDto dto) {
        Process process = getProcessByProcessNumber(processNumber);
        checkProcessStatus(process.getStatus());
        updateProcess(dto, process);
        formattingService.removeFormatting(process.getParties());
        processRepository.save(process);
    }

    @Transactional
    @Override
    public void archive(@NotNull final Set<String> processesNumbers) {
        List<Process> processes = getProcessByNumberIn(processesNumbers);
        checkProcessStatus(processes);
        checkNotFoundProcesses(processesNumbers, processes);
        ArchiveCommand processCommand = new ProcessArchiveCommand(processRepository, processes);
        processCommand.execute();
        processRepository.saveAll(processes);
    }

    @Override
    public Page<ProcessDto> get(@NotNull final ProcessFilter filter, @NotNull final Pageable pageable) {
        FilterStrategy filterStrategy = ProcessFilterSelector.select(filter);
        return filterStrategy.filter(filter, pageable, processRepository);
    }

    // ===== PRIVATE METHODS =====

    private void createProcess(@NotNull final RequestProcessDto dto, @NotNull final Process process) {
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

    private Party getOrCreateParty(@NotNull final Process process, @NotNull final PartyDto p) {
        return partyService.getExistingParties(p.getLegalEntityId()).orElseGet(() -> {
            var result = entityMapper.toEntity(p);
            result.setProcesses(List.of(process));
            partyService.attach(result);
            return result;
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

    private static void setProcessToAchieved(@NotNull final List<Process> processes) {
        processes
                .forEach(p -> p
                        .setStatus(Status.ARCHIVED)
                );
    }

    private void checkProcessStatus(@NotNull final List<Process> processes) {
        processes.forEach(p -> checkProcessStatus(p.getStatus()));
    }

    private void checkProcessStatus(@NotNull final Status status) {
        if (status.equals(Status.ARCHIVED) || status.equals(Status.SUSPENDED))
            throw new OperationDeniedException("%s process(es) cannot be updated".formatted(status));
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

    private void updateProcess(@NotNull final RequestProcessDto dto, @NotNull final Process process) {
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
