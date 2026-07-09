package service;

import datastructure.MyLinkedList;
import model.Vehicle;
import model.Booking;
import model.History;
import util.FileManager;

public class VehicleService {
    private MyLinkedList<Vehicle> vehicleList;

    public VehicleService() {
        this.vehicleList = new MyLinkedList<>();
    }

    public void displayAllVehicles() {
        System.out.println("\n--- VEHICLE LIST ---");
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles found in the system!");
            return;
        }
        vehicleList.display();
        System.out.println("--------------------");
    }

    public void displayVehiclesByCustomer(String customerId) {
        System.out.println("\n--- VEHICLE LIST FOR CUSTOMER " + customerId + " ---");
        int size = vehicleList.size();
        boolean found = false;
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getCustomerId().equalsIgnoreCase(customerId)) {
                System.out.println(v.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No vehicles associated with this customer!");
        }
        System.out.println("----------------------------------------");
    }

    public void addVehicle(String licensePlate, String customerId, CustomerService customerService) {
        // Validate inputs
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            System.out.println("=> Error: License plate cannot be empty!");
            return;
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println("=> Error: Customer ID cannot be empty!");
            return;
        }

        // Validate customer existence
        if (customerService.findCustomerById(customerId) == null) {
            System.out.println("=> Error: Customer ID " + customerId + " does not exist in the system!");
            return;
        }

        // Validate license plate uniqueness
        if (findVehicleByLicense(licensePlate) != null) {
            System.out.println("=> Error: License plate " + licensePlate + " already exists!");
            return;
        }

        // Generate unique Vehicle ID
        int size = vehicleList.size();
        int maxIdNum = 0;
        for (int i = 0; i < size; i++) {
            String currentId = vehicleList.get(i).getId();
            if (currentId != null && currentId.startsWith("V")) {
                try {
                    int num = Integer.parseInt(currentId.substring(1));
                    if (num > maxIdNum) {
                        maxIdNum = num;
                    }
                } catch (Exception e) {}
            }
        }
        String newId = String.format("V%03d", maxIdNum + 1);

        Vehicle newVehicle = new Vehicle(newId, licensePlate, customerId);
        vehicleList.addLast(newVehicle);
        System.out.println("=> Added vehicle successfully: " + licensePlate + " (Vehicle ID: " + newId + ")");
        
        // Auto-save on change (FR-23)
        FileManager.saveVehicles(vehicleList);
    }

    public Vehicle findVehicleByLicense(String licensePlate) {
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return v;
            }
        }
        return null;
    }

    public void updateVehicle(String licensePlate, String newCustomerId, CustomerService customerService) {
        Vehicle v = findVehicleByLicense(licensePlate);
        if (v == null) {
            System.out.println("=> Error: Vehicle not found with license plate: " + licensePlate);
            return;
        }

        // Validate new customer existence
        if (customerService.findCustomerById(newCustomerId) == null) {
            System.out.println("=> Error: New owner Customer ID " + newCustomerId + " does not exist!");
            return;
        }

        v.setCustomerId(newCustomerId);
        System.out.println("=> Updated vehicle successfully: " + licensePlate);
        
        // Auto-save on change (FR-23)
        FileManager.saveVehicles(vehicleList);
    }

    public void deleteVehicle(String licensePlate, BookingService bookingService, MyLinkedList<History> historyList) {
        Vehicle v = findVehicleByLicense(licensePlate);
        if (v == null) {
            System.out.println("=> Error: Vehicle not found with license plate: " + licensePlate);
            return;
        }

        // 1. Kiểm tra ràng buộc liên kết Booking
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            // Biển số xe được dùng làm vehicleId trong Booking (Mã xe / Biển số xe)
            if (bookings.get(i).getVehicleId().equalsIgnoreCase(licensePlate)) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated booking(s)!");
                return;
            }
        }

        // 2. Kiểm tra ràng buộc liên kết Lịch sử rửa xe (History)
        for (int i = 0; i < historyList.size(); i++) {
            if (historyList.get(i).getPlateNumber().equalsIgnoreCase(licensePlate)) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated history records!");
                return;
            }
        }

        // Tiến hành xóa nếu không có liên kết
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            if (vehicleList.get(i).getLicensePlate().equalsIgnoreCase(licensePlate)) {
                vehicleList.remove(i);
                System.out.println("=> Deleted vehicle successfully: " + licensePlate);
                
                // Auto-save on change (FR-23)
                FileManager.saveVehicles(vehicleList);
                return;
            }
        }
    }

    public MyLinkedList<Vehicle> getVehicleList() {
        return vehicleList;
    }
}