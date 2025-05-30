package attus.proc.proc_jur.service.filter;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.repository.ProcessRepository;
import attus.proc.proc_jur.util.DtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class OpeningDateFilterStrategy implements FilterStrategy {
    @Override
    public Page<ProcessDto> filter(final ProcessFilter filter, final Pageable pageable, final ProcessRepository processRepository) {
        return processRepository.findByOpeningDate(filter.getOpeningDate(), pageable)
                .map(DtoMapper::toDto);
    }
}
