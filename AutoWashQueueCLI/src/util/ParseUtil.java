package util;

public class ParseUtil {
    private ParseUtil() {
    }

    public static int parseIntSafe(String raw, int defaultValue) {
        String s = TextUtil.cleanToken(raw);
        if (s.isEmpty()) {
            return defaultValue;
        }

        // Handle legacy numeric like "300.0"
        int dot = s.indexOf('.');
        if (dot >= 0) {
            s = s.substring(0, dot);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || (c == '-' && sb.length() == 0)) {
                sb.append(c);
            }
        }

        if (sb.length() == 0) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean parseBooleanSafe(String raw, boolean defaultValue) {
        String s = TextUtil.cleanToken(raw).toLowerCase();
        if (s.isEmpty()) {
            return defaultValue;
        }
        if (s.equals("true") || s.equals("1") || s.equals("yes") || s.equals("y")) {
            return true;
        }
        if (s.equals("false") || s.equals("0") || s.equals("no") || s.equals("n")) {
            return false;
        }
        return defaultValue;
    }
}