package util;

import datastructure.MyLinkedList;
import java.util.List;
import java.util.function.Function;

public class TablePrinter {

    /**
     * In bảng với các cột tùy chỉnh
     * @param title Tiêu đề bảng
     * @param data Danh sách dữ liệu
     * @param columns Mảng các cột (header, width, extractor)
     * @param <T> Kiểu dữ liệu của đối tượng
     */
    public static <T> void printTable(String title, List<T> data, Column<T>[] columns) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        // In tiêu đề
        System.out.println(title.toUpperCase());

        // Tính tổng chiều dài bảng
        int totalWidth = 1; // Bắt đầu với "|"
        for (Column<T> col : columns) {
            totalWidth += col.width + 3; // " value |"
        }
        String line = repeat('-', totalWidth);

        // In đường kẻ trên
        System.out.println(line);

        // In header
        System.out.print("|");
        for (Column<T> col : columns) {
            System.out.printf(" %-" + col.width + "s |", col.header);
        }
        System.out.println();
        System.out.println(line);

        // In dữ liệu
        for (T item : data) {
            System.out.print("|");
            for (Column<T> col : columns) {
                String value = col.extractor.apply(item);
                if (value == null) value = "";
                // Cắt chuỗi nếu quá dài
                if (value.length() > col.width) {
                    value = value.substring(0, col.width - 3) + "...";
                }
                System.out.printf(" %-" + col.width + "s |", value);
            }
            System.out.println();
        }

        // In đường kẻ dưới
        System.out.println(line);
    }

    /** Prints a project MyLinkedList using the same readable table layout. */
    public static <T> void printTable(String title, MyLinkedList<T> data, Column<T>... columns) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        System.out.println("\n" + title.toUpperCase());
        int totalWidth = 1;
        for (Column<T> col : columns) {
            totalWidth += col.width + 3;
        }
        String line = repeat('-', totalWidth);
        System.out.println(line);
        System.out.print("|");
        for (Column<T> col : columns) {
            System.out.printf(" %-" + col.width + "s |", col.header);
        }
        System.out.println();
        System.out.println(line);

        for (int i = 0; i < data.size(); i++) {
            printRow(data.get(i), columns);
        }
        System.out.println(line);
    }

    public static <T> void printRows(String title, MyLinkedList<T> data, Column<T>... columns) {
        printTable(title, data, columns);
    }

    private static <T> void printRow(T item, Column<T>[] columns) {
        System.out.print("|");
        for (Column<T> col : columns) {
            String value = col.extractor.apply(item);
            if (value == null) value = "";
            if (value.length() > col.width) {
                value = col.width <= 3 ? value.substring(0, col.width) : value.substring(0, col.width - 3) + "...";
            }
            System.out.printf(" %-" + col.width + "s |", value);
        }
        System.out.println();
    }

    /**
     * Lớp định nghĩa cột
     * @param <T> Kiểu dữ liệu
     */
    public static class Column<T> {
        final String header;
        final int width;
        final Function<T, String> extractor;

        public Column(String header, int width, Function<T, String> extractor) {
            this.header = header;
            this.width = width;
            this.extractor = extractor;
        }
    }

    private static String repeat(char ch, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
