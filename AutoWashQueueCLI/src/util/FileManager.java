package util;

import datastructure.MyLinkedList;
import model.Customer;
import model.Vehicle;
import model.WashPackage;
import model.Booking;
import model.Period;
import model.History;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

public class FileManager {

    // --- HÀM ĐỌC DỮ LIỆU TỪ FILE ---
    public static boolean loadData(MyLinkedList<Customer> customers, MyLinkedList<WashPackage> services, MyLinkedList<Vehicle> vehicles) {
        boolean hasData = false;

        // 1. Đọc Khách hàng (7 trường)
        File fCust = new File("data/customers.txt");
        if (fCust.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fCust)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 5) {
                        double totalSpent = p.length >= 6 ? Double.parseDouble(p[5].trim()) : 0.0;
                        int visitCount = p.length >= 7 ? Integer.parseInt(p[6].trim()) : 0;
                        customers.addLast(new Customer(
                                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), 
                                Integer.parseInt(p[4].trim()), totalSpent, visitCount
                        ));
                    }
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading customers.txt"); }
        }

        // 2. Đọc Dịch vụ (5 trường)
        File fServ = new File("data/services.txt");
        if (fServ.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fServ)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 3) {
                        int duration = p.length >= 4 ? Integer.parseInt(p[3].trim()) : 30;
                        String status = p.length >= 5 ? p[4].trim() : "ACTIVE";
                        services.addLast(new WashPackage(
                                p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim()), duration, status
                        ));
                    }
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading services.txt"); }
        }

        // 3. Đọc Xe cộ (3 trường)
        File fVeh = new File("data/vehicles.txt");
        if (fVeh.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fVeh)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 3) {
                        vehicles.addLast(new Vehicle(p[0].trim(), p[1].trim(), p[2].trim()));
                    }
                }
                hasData = true;
            } catch (Exception e) { System.out.println("=> Error reading vehicles.txt"); }
        }

        return hasData;
    }

    // --- HÀM GHI DỮ LIỆU XUỐNG FILE ---
    public static void saveData(MyLinkedList<Customer> customers, MyLinkedList<WashPackage> services, MyLinkedList<Vehicle> vehicles) {
        saveCustomers(customers);
        saveServices(services);
        saveVehicles(vehicles);
        System.out.println("[System] -> All data saved to (.txt) files safely!");
    }

    public static void saveCustomers(MyLinkedList<Customer> customers) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/customers.txt")) {
            for (int i = 0; i < customers.size(); i++) {
                bw.write(customers.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing customers.txt"); }
    }

    public static void saveServices(MyLinkedList<WashPackage> services) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/services.txt")) {
            for (int i = 0; i < services.size(); i++) {
                bw.write(services.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing services.txt"); }
    }

    public static void saveVehicles(MyLinkedList<Vehicle> vehicles) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/vehicles.txt")) {
            for (int i = 0; i < vehicles.size(); i++) {
                bw.write(vehicles.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing vehicles.txt"); }
    }

    // === CÁC HÀM ĐỌC GHI DỮ LIỆU BỔ SUNG ===
    public static void loadExtraData(MyLinkedList<Booking> bookings, MyLinkedList<Period> periods, MyLinkedList<History> histories) {
        File fB = new File("data/bookings.txt");
        if (fB.exists()) {
            try (BufferedReader br = FileUtil.openReaderWithBom(fB)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split("\\|");
                    if (p.length >= 10) {
                        bookings.addLast(new Booking(
                                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim(), p[5].trim(),
                                p[6].trim(), p[7].trim(), p[8].trim(), Long.parseLong(p[9].trim())
                        ));
                    } else if (p.length >= 4) {
                        bookings.addLast(new Booking(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                    }
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
                    if (p.length >= 3) {
                        periods.addLast(new Period(p[0].trim(), p[1].trim(), p[2].trim()));
                    }
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
                    if (p.length >= 8) {
                        histories.addLast(new History(
                                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim(), p[5].trim(),
                                Double.parseDouble(p[6].trim()), Integer.parseInt(p[7].trim())
                        ));
                    } else if (p.length >= 5) {
                        histories.addLast(new History(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                    }
                }
            } catch (Exception e) { System.out.println("=> Error reading history.txt"); }
        }
    }

    public static void saveExtraData(MyLinkedList<Booking> bookings, MyLinkedList<Period> periods, MyLinkedList<History> histories) {
        saveBookings(bookings);
        savePeriods(periods);
        saveHistories(histories);
    }

    public static void saveBookings(MyLinkedList<Booking> bookings) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/bookings.txt")) {
            for (int i = 0; i < bookings.size(); i++) {
                bw.write(bookings.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing bookings.txt"); }
    }

    public static void savePeriods(MyLinkedList<Period> periods) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/periods.txt")) {
            for (int i = 0; i < periods.size(); i++) {
                bw.write(periods.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing periods.txt"); }
    }

    public static void saveHistories(MyLinkedList<History> histories) {
        try (BufferedWriter bw = FileUtil.openUtf8Writer("data/history.txt")) {
            for (int i = 0; i < histories.size(); i++) {
                bw.write(histories.get(i).toString());
                bw.newLine();
            }
        } catch (Exception e) { System.out.println("=> Error writing history.txt"); }
    }
}