package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.dto.RequestProcessDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public interface ProcessService {

    String create(@NotNull RequestProcessDto dto);
    void update(@NotNull String processNumber, @NotNull RequestProcessDto dto);
    void archive(@NotNull Set<String> processesNumbers);
    Page<ProcessDto> get(@NotNull ProcessFilter filter, @NotNull Pageable pageable);
}
