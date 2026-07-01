package data;

import datastructure.MyLinkedList;
import model.Customer;
import model.Vehicle;
import model.WashService;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

public class FileManager {

    // --- HÀM ĐỌC DỮ LIỆU TỪ FILE ---
    public static boolean loadData(MyLinkedList<Customer> customers, MyLinkedList<WashService> services, MyLinkedList<Vehicle> vehicles) {
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
            } catch (Exception e) { System.out.println("=> Loi doc customers.txt"); }
        }

        // 2. Đọc Dịch vụ
        File fServ = new File("services.txt");
        if (fServ.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fServ)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split(",");
                    if (p.length == 3) services.addLast(new WashService(p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim())));
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Loi doc services.txt"); }
        }

        // 3. Đọc Xe cộ
        File fVeh = new File("vehicles.txt");
        if (fVeh.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fVeh)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split(",");
                    if (p.length == 3) vehicles.addLast(new Vehicle(p[0].trim(), p[1].trim(), p[2].trim()));
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Loi doc vehicles.txt"); }
        }

        return hasData;
    }

    // --- HÀM GHI DỮ LIỆU XUỐNG FILE ---
    public static void saveData(MyLinkedList<Customer> customers, MyLinkedList<WashService> services, MyLinkedList<Vehicle> vehicles) {
        // 1. Ghi Khách hàng
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/customers.txt")) {
            for (int i = 0; i < customers.size(); i++) {
                Customer c = customers.get(i);
                bw.write(c.getId() + "," + c.getName() + "," + c.getPhone() + "," + c.getMembershipLevel() + "," + c.getPoints());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Loi ghi customers.txt"); }

        // 2. Ghi Dịch vụ
        try (BufferedWriter bw = FileUtil.openUtf8Writer("services.txt")) {
            for (int i = 0; i < services.size(); i++) {
                WashService s = services.get(i);
                bw.write(s.getId() + "," + s.getName() + "," + s.getPrice());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Loi ghi services.txt"); }

        // 3. Ghi Xe cộ
        try (BufferedWriter bw = FileUtil.openUtf8Writer("vehicles.txt")) {
            for (int i = 0; i < vehicles.size(); i++) {
                Vehicle v = vehicles.get(i);
                bw.write(v.getLicensePlate() + "," + v.getCustomerId() + "," + v.getVehicleType());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Loi ghi vehicles.txt"); }

        System.out.println("=> Da luu toan bo du lieu xuong file (.txt) an toan!");
    }
}