package attus.proc.proc_jur.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PartyType {

    AUTHOR,
    DEFENDANT,
    ATTORNEY;

    @JsonCreator
    public static PartyType fromString(String value) {
        return value == null ? null : PartyType.valueOf(value.toUpperCase());
    }
}
