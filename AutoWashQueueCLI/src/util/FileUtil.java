package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    private FileUtil() {
    }

    /**
     * Tác dụng: Mở BufferedReader cho file có hỗ trợ BOM (UTF-8/UTF-16).
     *
     * Cấu trúc xử lý:
     * 1) Mở stream: `FileInputStream fis = new FileInputStream(file);`.
     * 2) Wrap với PushbackInputStream: `PushbackInputStream pb = new PushbackInputStream(fis, 3);`.
     * 3) Đọc BOM: `byte[] bom = new byte[3]; int n = pb.read(bom, 0, bom.length);`.
     * 4) Xác định charset mặc định: `String charset = StandardCharsets.UTF_8.name();`.
     * 5) Kiểm tra UTF-16LE: `if (n >= 2 && (bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE))`.
     * 6) Kiểm tra UTF-16BE: `else if (n >= 2 && (bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF))`.
     * 7) Kiểm tra UTF-8: `else if (n >= 3 && (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF))`.
     * 8) Hoàn trả các byte không phải BOM: `pb.unread(...)`.
     * 9) Trả về reader: `return new BufferedReader(new InputStreamReader(pb, charset));`.
     *
     * Câu lệnh dùng:
     * - `new FileInputStream(...)`, `new PushbackInputStream(...)`.
     * - `read(...)`, `unread(...)`.
     * - `new BufferedReader(...)`, `new InputStreamReader(...)`.
     * - `StandardCharsets.UTF_8.name()`.
     */
    public static BufferedReader openReaderWithBom(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        PushbackInputStream pb = new PushbackInputStream(fis, 3);

        byte[] bom = new byte[3];
        int n = pb.read(bom, 0, bom.length);

        String charset = StandardCharsets.UTF_8.name();
        if (n >= 2 && (bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE)) {
            charset = "UTF-16LE";
            if (n > 2) {
                pb.unread(bom, 2, n - 2);
            }
        } else if (n >= 2 && (bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF)) {
            charset = "UTF-16BE";
            if (n > 2) {
                pb.unread(bom, 2, n - 2);
            }
        } else if (n >= 3 && (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
            charset = StandardCharsets.UTF_8.name();
            if (n > 3) {
                pb.unread(bom, 3, n - 3);
            }
        } else {
            if (n > 0) {
                pb.unread(bom, 0, n);
            }
        }

        return new BufferedReader(new InputStreamReader(pb, charset));
    }

    /**
     * Tác dụng: Mở BufferedWriter để ghi file UTF-8.
     *
     * Cấu trúc xử lý:
     * 1) `return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));`.
     *
     * Câu lệnh dùng:
     * - `new BufferedWriter(...)`, `new OutputStreamWriter(...)`, `new FileOutputStream(...)`.
     * - `StandardCharsets.UTF_8`.
     */
    public static BufferedWriter openUtf8Writer(String fileName) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
    }
}