package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public sealed interface ProcessService permits ProcessServiceImpl {

    String create(ProcessDto dto);
    void update(String processNumber, ProcessDto dto);
    void archive(Set<String> processesNumbers);
    Page<ProcessDto> get(ProcessFilter filter, Pageable pageable);
}
