package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.validation.ValidDescription;
import attus.proc.proc_jur.validation.ValidLocalDateTime;
import attus.proc.proc_jur.validation.ValidProcessNumber;
import attus.proc.proc_jur.validation.ValidStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProcessDto extends ProcessAbs {

    private final String number;

    public ProcessDto(LocalDateTime openingDate, Status status, String description, List<PartyDto> parties, List<ActionDto> actions, String number) {
        super(openingDate, status, description, parties, actions);
        this.number = number;
    }
}
