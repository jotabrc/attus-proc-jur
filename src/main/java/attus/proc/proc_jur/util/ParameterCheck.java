package attus.proc.proc_jur.util;

public class ParameterCheck {

    private ParameterCheck() {}

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public static <T> boolean isNull(T t) {
        return t == null;
    }
}
