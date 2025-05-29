package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionDto {

    private final String processNumber;
    private final ActionType type;
//    private ZonedDateTime registrationDate;
    private final String description;
}
