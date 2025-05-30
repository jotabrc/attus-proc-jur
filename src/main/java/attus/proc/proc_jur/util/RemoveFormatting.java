package attus.proc.proc_jur.util;

public class RemoveFormatting {

    public static String remove(String id) {
        return id.replaceAll("[.()/-]", "");
    }
}
