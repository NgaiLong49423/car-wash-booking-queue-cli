package util;

/**
 * Text utilities used across the project.
 */
public final class TextUtil {

    private TextUtil() {
    }

    /**
     * Tác dụng: Làm sạch chuỗi bằng cách xóa BOM, ký tự vô hình, khoảng trắng, và ngoặc kép.
     *
     * Cấu trúc xử lý:
     * 1) Kiểm tra null: `if (raw == null) return "";`.
     * 2) Gán input: `String s = raw;`.
     * 3) Xóa BOM (Byte Order Mark): `s = s.replace("\uFEFF", "");`.
     * 4) Xóa ký tự thay thế: `s = s.replace("\uFFFD", "");`.
     * 5) Xóa ký tự null: `s = s.replace("\u0000", "");`.
     * 6) Xóa khoảng trắng không nhìn thấy: `s = s.replace("\u200B", "");`.
     * 7) Chuyển non-breaking space thành space: `s = s.replace("\u00A0", " ");`.
     * 8) Trim khoảng trắng 2 đầu: `s = s.trim();`.
     * 9) Xóa ngoặc kép cả 2 bên (ngoặc kép thường hoặc smart): `while (s.length() >= 2 && isQuoteChar(s.charAt(0)) && isQuoteChar(s.charAt(s.length() - 1))) s = s.substring(1, s.length() - 1).trim();`.
     * 10) Xóa ngoặc kép đơn ở đầu: `if (!s.isEmpty() && isQuoteChar(s.charAt(0))) s = s.substring(1).trim();`.
     * 11) Xóa ngoặc kép đơn ở cuối: `if (!s.isEmpty() && isQuoteChar(s.charAt(s.length() - 1))) s = s.substring(0, s.length() - 1).trim();`.
     * 12) Trả về kết quả: `return s;`.
     *
     * Câu lệnh dùng:
     * - `null`, `return`.
     * - `replace(...)` - Xóa ký tự đặc biệt.
     * - `trim()` - Xóa khoảng trắng.
     * - `while`, `if` - Điều kiện.
     * - `substring(...)` - Cắt chuỗi.
     * - `charAt(...)` - Lấy ký tự tại vị trí.
     * - `length()`, `isEmpty()` - Kiểm tra độ dài.
     * - `isQuoteChar(...)` - Kiểm tra ngoặc kép.
     */
    public static String cleanToken(String raw) {
        if (raw == null) {
            return "";
        }
        String s = raw;
        // Remove BOM, replacement char, nulls and invisible spaces
        s = s.replace("\uFEFF", "");
        s = s.replace("\uFFFD", "");
        s = s.replace("\u0000", "");
        s = s.replace("\u200B", "");
        s = s.replace("\u00A0", " ");
        s = s.trim();

        // Strip wrapping quotes (handles straight and “smart” quotes)
        while (s.length() >= 2 && isQuoteChar(s.charAt(0)) && isQuoteChar(s.charAt(s.length() - 1))) {
            s = s.substring(1, s.length() - 1).trim();
        }
        if (!s.isEmpty() && isQuoteChar(s.charAt(0))) {
            s = s.substring(1).trim();
        }
        if (!s.isEmpty() && isQuoteChar(s.charAt(s.length() - 1))) {
            s = s.substring(0, s.length() - 1).trim();
        }

        return s;
    }

    /**
     * Tác dụng: Kiểm tra chuỗi có trống (null hoặc toàn khoảng trắng).
     *
     * Cấu trúc xử lý:
     * 1) `return s == null || s.trim().isEmpty();`.
     *
     * Câu lệnh dùng:
     * - `null`, `trim()`, `isEmpty()`.
     * - `||` (hoặc logic).
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Tác dụng: Kiểm tra ký tự có phải dấu ngoặc kép không (hỗ trợ ngoặc thường và "smart").
     *
     * Cấu trúc xử lý:
     * 1) `return c == '"' || c == '\'' || c == '\u201C' || c == '\u201D' || c == '\u2018' || c == '\u2019';`.
     *
     * Câu lệnh dùng:
     * - `==`, `||` (hoặc logic).
     * - Unicode escape sequences.
     */
    private static boolean isQuoteChar(char c) {
        return c == '"' || c == '\'' || c == '\u201C' || c == '\u201D' || c == '\u2018' || c == '\u2019';
    }
}