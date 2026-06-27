package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utilities for dd/MM/yyyy.
 */
public final class DateUtil {

    public static final String DEFAULT_PATTERN = "dd/MM/yyyy";

    /**
     * Tác dụng: Tạo ThreadLocal chứa SimpleDateFormat (thread-safe) để format/parse ngày.
     *
     * Cấu trúc xử lý:
     * 1) Khai báo static final ThreadLocal: `private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() -> { ... });`.
     * 2) Trong lambda initializer, tạo SimpleDateFormat: `SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);`.
     * 3) Thiết lập chế độ nghiêm ngặt: `sdf.setLenient(false);`.
     * 4) Trả về: `return sdf;`.
     * 5) Mỗi thread sẽ có một instance riêng của SimpleDateFormat.
     * 6) Khi sử dụng, gọi: `SDF.get()` để lấy instance của thread hiện tại.
     *
     * Câu lệnh dùng:
     * - `ThreadLocal.withInitial(...)`.
     * - Lambda expression `() -> { ... }`.
     * - `new SimpleDateFormat(...)`.
     * - `setLenient(false)`.
     * - `SDF.get()` - Gọi trong các hàm format/parse.
     *
     * Ưu điểm:
     * - Thread-safe: Không cần synchronized vì mỗi thread có instance riêng.
     * - SimpleDateFormat là stateful nên không thread-safe - ThreadLocal giải quyết.
     * - Hiệu suất cao: Tái sử dụng instance thay vì tạo mới mỗi lần.
     */
    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() -> {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        sdf.setLenient(false);
        return sdf;
    });

    private DateUtil() {
    }

    /**
     * Tác dụng: Định dạng ngày thành chuỗi theo DEFAULT_PATTERN (dd/MM/yyyy).
     *
     * Cấu trúc xử lý:
     * 1) Kiểm tra null: `return date == null ? "" : SDF.get().format(date);`.
     *
     * Câu lệnh dùng:
     * - Ternary operator `? :`.
     * - `SDF.get()`, `format(...)`.
     */
    public static String format(Date date) {
        return date == null ? "" : SDF.get().format(date);
    }

    /**
     * Tác dụng: Parse chuỗi ngày an toàn (trả về null nếu lỗi).
     *
     * Cấu trúc xử lý:
     * 1) Làm sạch input: `String s = TextUtil.cleanToken(raw);`.
     * 2) Kiểm tra rỗng: `if (s.isEmpty()) return null;`.
     * 3) Parse: `try { return SDF.get().parse(s); }`.
     * 4) Catch Exception: `return null;`.
     *
     * Câu lệnh dùng:
     * - `TextUtil.cleanToken(...)`.
     * - `isEmpty()`, `return null`.
     * - `try/catch (Exception)`.
     * - `SDF.get()`, `parse(...)`.
     */
    public static Date parseSafe(String raw) {
        String s = TextUtil.cleanToken(raw);
        if (s.isEmpty()) {
            return null;
        }
        try {
            return SDF.get().parse(s);
        } catch (Exception e) {
            return null;
        }
    }
}