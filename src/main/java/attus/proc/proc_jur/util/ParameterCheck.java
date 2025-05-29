package attus.proc.proc_jur.util;

import attus.proc.proc_jur.enums.Status;

public class ParameterCheck {

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public static <T> boolean isNull(T t) {
        return t == null;
    }
}
