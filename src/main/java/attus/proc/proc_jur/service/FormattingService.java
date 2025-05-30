package attus.proc.proc_jur.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface FormattingService {

    String removeFormatting(@NotNull String s);
    <T> void removeFormatting(@NotNull List<T> t);
}
