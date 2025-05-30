package attus.proc.proc_jur.service;

import attus.proc.proc_jur.model.Party;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Component
public class FormattingServiceImpl implements FormattingService {

    @Value("${validation.pattern.formatting.remove}")
    private String REMOVE_PATTERN;

    @Override
    public String removeFormatting(@NotNull final String s) {
        return s.replaceAll(REMOVE_PATTERN, "")
                .replaceAll("\\s+", "");
    }

    @Override
    public <T> void removeFormatting(@NotNull final List<T> t) {
        t
                .parallelStream()
                .forEach(e -> {
                    if (t instanceof Party p) {
                        p.setLegalEntityId(removeFormatting(p.getLegalEntityId()));
                        p.setPhone(removeFormatting(p.getPhone()));
                    }
                });
    }
}
