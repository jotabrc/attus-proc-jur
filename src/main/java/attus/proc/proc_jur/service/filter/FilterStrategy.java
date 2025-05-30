package attus.proc.proc_jur.service.filter;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.repository.ProcessRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public interface FilterStrategy {

    Page<ProcessDto> filter(@NotNull ProcessFilter filter, @NotNull Pageable pageable, @NotNull ProcessRepository processRepository);
}
