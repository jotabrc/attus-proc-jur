package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProcessDto {

    private final String number;
    private final LocalDateTime openingDate;
    private final Status status;
    private final String description;
    private final List<PartyDto> parties;
    private final List<ActionDto> actions;
}
