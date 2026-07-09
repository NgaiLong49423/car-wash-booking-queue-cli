package util;

import datastructure.MyLinkedList;
import datastructure.MyQueue;
import model.Customer;
import model.Vehicle;
import model.WashPackage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import model.Booking;
import model.Period;
import model.History;

public class FileManager {

    // --- HÀM ĐỌC DỮ LIỆU TỪ FILE ---
    public static boolean loadData(MyLinkedList<Customer> customers, MyLinkedList<WashPackage> services, MyLinkedList<Vehicle> vehicles) {
        boolean hasData = false;

        // 1. Đọc Khách hàng
        File fCust = new File("data/customers.txt");
        if (fCust.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fCust)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length == 5) customers.addLast(new Customer(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), Integer.parseInt(p[4].trim())));
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading customers.txt"); }
        }

        // 2. Đọc Dịch vụ (Đã chuyển vào data/ và sửa dấu phẩy thành |)
        File fServ = new File("data/services.txt");
        if (fServ.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fServ)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 3) services.addLast(new WashPackage(p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim())));
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading services.txt"); }
        }

        // 3. Đọc Xe cộ (Đã chuyển vào data/ và sửa dấu phẩy thành |)
        File fVeh = new File("data/vehicles.txt");
        if (fVeh.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fVeh)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 3) vehicles.addLast(new Vehicle(p[0].trim(), p[1].trim(), p[2].trim()));
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading vehicles.txt"); }
        }

        return hasData;
    }

    // --- HÀM GHI DỮ LIỆU XUỐNG FILE ---
    public static void saveData(MyLinkedList<Customer> customers, MyLinkedList<WashPackage> services, MyLinkedList<Vehicle> vehicles) {
        // Áp dụng Best Practice: Dùng .toString() thay vì ghép chuỗi thủ công theo lời khuyên của Leader
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/customers.txt")) {
            for (int i = 0; i < customers.size(); i++) {
                bw.write(customers.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing customers.txt"); }

        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/services.txt")) {
            for (int i = 0; i < services.size(); i++) {
                bw.write(services.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing services.txt"); }

        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/vehicles.txt")) {
            for (int i = 0; i < vehicles.size(); i++) {
                bw.write(vehicles.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing vehicles.txt"); }

        System.out.println("[System] -> All data saved to (.txt) files safely!");
    }

    // === CÁC HÀM ĐỌC GHI DỮ LIỆU BỔ SUNG ===

    // Đã đổi MyLinkedList<Booking> thành MyQueue<Booking> để tương thích với Service
    public static void loadExtraData(MyQueue<Booking> bookings, MyLinkedList<Period> periods, MyLinkedList<History> histories) {
        File fB = new File("data/bookings.txt");
        if (fB.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fB)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    // Queue dùng lệnh enqueue để nạp vào cuối
                    if (p.length >= 4) bookings.enqueue(new Booking(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                }
            } catch (Exception e) { System.out.println("=> Error reading bookings.txt"); }
        }

        File fP = new File("data/periods.txt");
        if (fP.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fP)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 3) periods.addLast(new Period(p[0].trim(), p[1].trim(), Boolean.parseBoolean(p[2].trim())));
                }
            } catch (Exception e) { System.out.println("=> Error reading periods.txt"); }
        }

        File fH = new File("data/history.txt");
        if (fH.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fH)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 5) histories.addLast(new History(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                }
            } catch (Exception e) { System.out.println("=> Error reading history.txt"); }
        }
    }

    public static void saveExtraData(MyQueue<Booking> bookings, MyLinkedList<Period> periods, MyLinkedList<History> histories) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/bookings.txt")) {
            // Xử lý ghi hàng đợi: Rút ra ghi rồi đẩy lại vào hàng đợi tạm
            MyQueue<Booking> tempQueue = new MyQueue<>();
            while (!bookings.isEmpty()) {
                Booking b = bookings.dequeue();
                bw.write(b.toString());
                bw.newLine();
                tempQueue.enqueue(b);
            }
            // Trả lại dữ liệu cho hàng đợi gốc
            while (!tempQueue.isEmpty()) {
                bookings.enqueue(tempQueue.dequeue());
            }
        } catch (Exception e) { }

        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/periods.txt")) {
            for (int i = 0; i < periods.size(); i++) {
                bw.write(periods.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { }

        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/history.txt")) {
            for (int i = 0; i < histories.size(); i++) {
                bw.write(histories.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { }
    }
}