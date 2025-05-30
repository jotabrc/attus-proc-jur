package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.validation.ValidDescription;
import attus.proc.proc_jur.validation.ValidLocalDateTime;
import attus.proc.proc_jur.validation.ValidStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class ProcessAbs {

    @ValidLocalDateTime
    private final LocalDateTime openingDate;

    @ValidStatus
    private final Status status;

    @ValidDescription
    private final String description;

    @Valid
    private final List<PartyDto> parties;

    @Valid
    private final List<ActionDto> actions;
}