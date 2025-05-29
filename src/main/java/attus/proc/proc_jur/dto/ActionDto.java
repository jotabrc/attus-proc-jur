package attus.proc.proc_jur.dto;

import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.validation.ValidActionType;
import attus.proc.proc_jur.validation.ValidStatus;
import attus.proc.proc_jur.validation.ValidDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionDto {

//    private final String processNumber;

    @ValidActionType
    private final ActionType type;

    @ValidDescription
    private final String description;
}
