package util;

import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class ConsoleInputter {

    public static Scanner scanner = new Scanner(System.in);

    public static boolean getBoolean(String prompt) {
        System.out.println(prompt + " Yes/No, True/False, 1/0");
        String data = scanner.nextLine().trim().toLowerCase();
        if (data.isEmpty()) {
            return false; // Trả về false nếu không nhập gì
        }
        char c = data.charAt(0);
        switch (c) {
            case 'y':
            case 't':
            case '1':
                return true;
            default:
                return false;
        }

    }

    public static int getInt(String prompt, int min, int max) {
        // 1. Xử lý trường hợp min > max để đảm bảo logic luôn đúng
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        int result = 0;
        boolean isValid = false;

        do {
            try {
                // Hiển thị gợi ý nhập trong khoảng [min - max]
                System.out.println(prompt + " [ <" + min + "> ->  <" + max + "> ]");

                // Sử dụng biến 'sc' (theo đúng tên khai báo trong ConsoleInputter của bạn)
                String input = scanner.nextLine().trim();
                result = Integer.parseInt(input);

                // 2. Kiểm tra xem số nhập vào có nằm trong khoảng cho phép không
                if (result >= min && result <= max) {
                    isValid = true;
                } else {
                    System.err.println("Lỗi: Số phải nằm trong khoảng từ " + min + " đến " + max + "!");
                }
            } catch (NumberFormatException e) {
                // 3. Bẫy lỗi nếu người dùng nhập chữ hoặc để trống (thay thế cho kiểm tra null)
                System.err.println("Lỗi: Vui lòng chỉ nhập số thực hợp lệ!");
            }
        } while (!isValid);

        return result;
    }

    public static double getDouble(String prompt, double min, double max) {
        // 1. Xử lý trường hợp min > max để đảm bảo logic luôn đúng
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }

        double result = 0;
        boolean isValid = false;

        do {
            try {
                // Hiển thị gợi ý nhập trong khoảng [min - max]
                System.out.println(prompt + " [ <" + min + "> ->  <" + max + "> ]");

                // Sử dụng biến 'sc' (theo đúng tên khai báo trong ConsoleInputter của bạn)
                String input = scanner.nextLine().trim();
                result = Double.parseDouble(input);

                // 2. Kiểm tra xem số nhập vào có nằm trong khoảng cho phép không
                if (result >= min && result <= max) {
                    isValid = true;
                } else {
                    System.err.println("Lỗi: Số phải nằm trong khoảng từ " + min + " đến " + max + "!");
                }
            } catch (NumberFormatException e) {
                // 3. Bẫy lỗi nếu người dùng nhập chữ hoặc để trống (thay thế cho kiểm tra null)
                System.err.println("Lỗi: Vui lòng chỉ nhập số nguyên hợp lệ!");
            }
        } while (!isValid);

        return result;
    }

    // Tác dụng: Nhập chuỗi, chống crash khi mất stream, chống nhập rỗng
// 1) In prompt. 2) Nếu không có dòng tiếp theo return rỗng. 
// 3) Đọc data và trim. 4) Nếu rỗng thì đệ quy nhập lại, không thì return data.
    public static String getStr(String prompt) {
        System.out.print(prompt + ": ");
        if (!scanner.hasNextLine()) {
            return ""; // Check stream tồn tại 
        }
        String data = scanner.nextLine().trim();
        return data.isEmpty() ? getStr(prompt) : data;
    }

    public static String getStr(String prompt, String pattern, String errorMsg) {
        String data;
        while (true) {
            System.out.println(prompt + ": ");
            data = scanner.nextLine().trim();

            // Bước 1: chặn lỗi nhập lỗi trống hoặc toàn dấu cách
            if (data.isEmpty()) {
                System.out.println("Lỗi: Dữ liệu không được để trống. Vui lòng nhập lại!");
                continue; // Quay lại vòng lặp để nhập lại
            }

            // Bước 2: Kiểm tra định dang (Pattern)
            if (data.matches(pattern)) {
                return data; // Hợp lệ, trả về kết quả
            } else {
                System.out.println(errorMsg); // Không hợp lệ, in thông báo lỗi
            }

        }
    }

    public static Date getDate(String prompt, String dataFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);

        // Thiết lập chế độ nghiêm ngặt để tránh việc tự động hiệu chỉnh ngày tháng nhập
        // vào
        // ví dụ: 30/02/2020 sẽ tự động chuyển thành 01/03/2020
        // Chặn lỗi: Ép Java phải kiểm tra ngày chính xác (Ví dụ: 31/02 sẽ bị báo lỗi
        // ngay)
        sdf.setLenient(false);

        // Vòng lặp nhập ngày tháng
        while (true) {
            System.out.print(prompt + " (format: " + dataFormat + "): ");
            String dateStr = scanner.nextLine().trim();

            // Bước 1: Chặn lỗi nhập rỗng
            if (dateStr.isEmpty()) {
                System.out.println("Lỗi: Dữ liệu không được để trống. Vui lòng nhập lại!");
                continue; // Quay lại vòng lặp để nhập lại
            }

            try {
                // Bước 2: Kiểm tra định dạng hợp lý của ngày
                return sdf.parse(dateStr); // Trả về kết quả nếu hợp lệ
            } catch (ParseException e) {
                // Bước 3: Báo lỗi nếu nhập sai định dạng hoặc ngày không tồn tại
                System.out
                        .println("Lỗi: Ngày không hợp lệ hoặc không đúng định dạng. Vui lòng nhập lại! Theo định dạng "
                                + dataFormat);
            }
        }
    }

    public static String dateStr(Date date, String dateFormat) {
        // Nếu date rỗng thì trả về null ngay, nếu không thì mới tạo formatter và chuyển
        // đổi
        // biểu_thức_điều_kiện ? giá_trị_khi_đúng : giá_trị_khi_sai;
        return (date == null) ? null : new SimpleDateFormat(dateFormat).format(date);
    }

    /*
     * Lấy 1 thành phần trong Date. Lớp Calender giúp truy xuất các thành phần
     * Cách dùng: int year = getPart(aDate, Calendar.YEAR);
     * Chú ý: Thành phần tháng cho kết quả : Tháng 1 --> 0
     */
    public static int getPart(Date d, int calendarPart) {
        // GregorianCalendar cal = new GregorianCalendar(); // tạo calandar
        Calendar cal = Calendar.getInstance(); // Cách lấy bộ lịch hệ thống nhanh hơn
        cal.setTime(d); // cho calendar mang ngày d
        return cal.get(calendarPart); // lấy ra thành phần thời gian này
    }

    // Lấy tuổi với ngày sinh đã biết
    public int getAge(Date birthDate) {
        Calendar now = Calendar.getInstance(); // Lấy lịch hiện tại
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate); // Gán ngày sinh vào lịch

        // Tính tuổi thô dựa trên năm
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        // Kiểm tra xem đã đến sinh nhật năm nay chưa
        // Nếu tháng hiện tại < tháng sinh HOẶC (tháng bằng nhau nhưng ngày hiện tại <
        // ngày sinh)
        if (now.get(Calendar.MONTH) < birth.get(Calendar.MONTH)
                || (now.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                && now.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
            age--; // Trừ đi 1 tuổi vì chưa tới sinh nhật
        }

        return age;
    }

    /*
     * Thay vì xây dựng lớp cho Menu, hàm menu được xây dựng. Tham số của
     * các hàm menu này có thể là một mảng tĩnh, mảng động hoặc một danh sách
     * Cú pháp tham số là mảng động (tham số với số phần tử thay đổi tùy ý)
     * DataType... Hàm có số đối số thay đổi). Trình biên dịch cư xử với
     * tham số có số lượng thay đổi như mảng 1 chiều.
     */

 /*
     * Menu trả về số int mà người dùng chọn.
     * Cách dùng: int choice = intMenu("Add", "Search", "Remove");
     */
    public static int intMenu(String title, Object... options) {
        if (options == null || options.length == 0) {
            System.out.println("Lỗi: Menu không có lựa chọn nào!");
            return -1;
        }

        // 1. Tính toán độ rộng tổng thể (Box Width)
        int maxLen = title.length();
        for (Object opt : options) {
            if (opt.toString().length() > maxLen) {
                maxLen = opt.toString().length();
            }
        }
        int totalWidth = maxLen + 12; // 12 là khoảng trống dự phòng cho viền và số thứ tự

        // 2. Vẽ Header
        System.out.println();
        printLine('=', totalWidth);
        printCenter(title.toUpperCase(), totalWidth);
        printLine('=', totalWidth);

        // 3. In Options (Căn lề chuẩn xác)
        // Giải thích: %- (totalWidth - 8) s giúp nội dung text luôn khớp với viền bên phải
        for (int i = 0; i < options.length; i++) {
            System.out.printf("| %2d. %-" + (totalWidth - 8) + "s |\n", (i + 1), options[i]);
        }

        // 4. Vẽ Footer
        printLine('=', totalWidth);

        return getInt("Lựa chọn của bạn", 1, options.length);
    }

    // Thêm <?> để nói với Java: "Tôi chấp nhận danh sách của bất cứ kiểu dữ liệu
    // nào"
    public static int intMenu(String title, List<?> options) {
        if (options == null || options.isEmpty()) {
            System.out.println("Lỗi: Danh sách menu trống!");
            return -1;
        }
        // Chuyển List thành mảng Object và gọi hàm intMenu (phiên bản 2 tham số)
        return intMenu(title, options.toArray());
    }

    /*
     * Menu trả về object mà người dùng chọn Cách dùng:
     * String objChoice = (String)objMenu("Add", "Search", "Remove");
     */
    public static Object objMenu(String title, Object... options) {
        // Truyền cả title và options vào hàm intMenu mới
        int choice = intMenu(title, options);

        if (choice == -1) {
            return null;
        }
        return options[choice - 1];
    }

    // Menu trả về đối tượng mà người dùng chọn từ một danh sách (List)
    public static Object objMenu(String title, List<?> options) {
        int choice = intMenu(title, options);
        return (choice == -1) ? null : options.get(choice - 1);
    }

    /*
     * Hàm dateKeyGen trong lớp ConsoleInputter có tác dụng tự động tạo ra
     * một mã duy nhất (ID) dựa trên thời gian thực của hệ thống. Đây là một
     * kỹ thuật rất phổ biến để tạo mã đơn hàng hoặc mã giao dịch mà không lo bị
     * trùng lặp,
     * vì mỗi giây trôi qua sẽ tạo ra một dãy số khác nhau.
     */
    // Hàm sinh mã tự động dựa trên ngày tháng năm và giờ phút giây hiện tại
    public static String dateKeyGen() {
        // Tối ưu 1: Dùng "HH" để lấy giờ từ 00-23, tránh trùng lặp giữa sáng và tối
        // Tối ưu 2: Viết gộp để tiết kiệm bộ nhớ khi không cần tạo biến trung gian
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static String updateStr(String prompt, String oldVal) {
        System.out.printf("%s (Old: %s, Enter to skip): ", prompt, oldVal);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? oldVal : input;
    }

    // Hàm nhập số có tùy chọn giữ cũ
    public static int updateInt(String prompt, int oldVal) {
        while (true) {
            System.out.printf("%s (Old: %d, Enter to skip): ", prompt, oldVal);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return oldVal;
            }
            try {
                int val = Integer.parseInt(input);
                if (val > 0) {
                    return val;
                }
                System.err.println("Lỗi: Số phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Vui lòng nhập số hợp lệ!");
            }
        }
    }

    public static String getStrOptional(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim(); // Trả về chuỗi rỗng nếu chỉ nhấn Enter
    }

    public static Date updateDate(String prompt, Date oldDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        System.out.printf("%s (Old: %s, Enter to skip): ", prompt, sdf.format(oldDate));
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return oldDate;
        }
        try {
            return sdf.parse(input);
        } catch (ParseException e) {
            System.out.println("Lỗi: Định dạng không đúng. Giữ lại giá trị cũ.");
            return oldDate;
        }
    }

    // Hàm in ra một dòng kẻ dài với ký tự và độ dài tùy chọn, giúp phân tách các phần trong giao diện console
    public static void printLine(char character, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(character);
        }
        System.out.println();
    }

    // Hàm in ra một dòng kẻ dài mặc định với ký tự '-' và độ dài 50, giúp phân tách các phần trong giao diện console
    public static void printCenter(String text, int width) {
        if (text == null || width <= text.length()) {
            System.out.println(text);
            return;
        }

        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();

        // Tạo khoảng trống bên trái
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }

        // Thêm nội dung chữ
        sb.append(text);

        // In ra màn hình
        System.out.println(sb.toString());
    }
}