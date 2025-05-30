package attus.proc.proc_jur.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActionType {

    PETITION,
    HEARING,
    SENTENCE;

    @JsonCreator
    public static ActionType fromString(String value) {
        return value == null ? null : ActionType.valueOf(value.toUpperCase());
    }
}
