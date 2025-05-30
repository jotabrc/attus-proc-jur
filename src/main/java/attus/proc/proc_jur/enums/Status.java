package attus.proc.proc_jur.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

    ACTIVE,
    SUSPENDED,
    ARCHIVED;

    @JsonCreator
    public static Status fromString(String value) {
        return value == null ? null : Status.valueOf(value.toUpperCase());
    }
}
