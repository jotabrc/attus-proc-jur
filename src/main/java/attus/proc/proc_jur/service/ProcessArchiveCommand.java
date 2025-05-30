package attus.proc.proc_jur.service;

import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.model.Process;
import attus.proc.proc_jur.repository.ProcessRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessArchiveCommand implements ArchiveCommand {

    private final ProcessRepository processRepository;
    private final List<Process> processes;

    public ProcessArchiveCommand(ProcessRepository processRepository, List<Process> processes) {
        this.processRepository = processRepository;
        this.processes = processes;
    }

    @Override
    public void execute() {
        processes
                .forEach(p -> p
                        .setStatus(Status.ARCHIVED)
                );
        processRepository.saveAll(processes);
    }
}
