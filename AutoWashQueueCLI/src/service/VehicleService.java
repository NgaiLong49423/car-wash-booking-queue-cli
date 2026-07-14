package service;

import datastructure.MyLinkedList;
import model.Vehicle;
import model.Booking;
import model.History;
import model.Customer;
import util.FileManager;

public class VehicleService {
    private MyLinkedList<Vehicle> vehicleList;

    public VehicleService() {
        this.vehicleList = new MyLinkedList<>();
    }

    public void displayAllVehicles(CustomerService customerService) {
        System.out.println("\n--- VEHICLE LIST ---");
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles found in the system!");
            return;
        }
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            Customer c = customerService.findCustomerById(v.getCustomerId());
            String ownerName = (c != null) ? c.getName() : "Unknown";
            System.out.println(v.toString() + " | Owner Name: " + ownerName);
        }
        System.out.println("--------------------");
    }

    public void displayVehiclesByCustomer(String customerId, CustomerService customerService) {
        System.out.println("\n--- VEHICLE LIST FOR CUSTOMER " + customerId + " ---");
        Customer c = customerService.findCustomerById(customerId);
        String ownerName = (c != null) ? c.getName() : "Unknown";
        int size = vehicleList.size();
        boolean found = false;
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getCustomerId().equalsIgnoreCase(customerId)) {
                System.out.println(v.toString() + " | Owner Name: " + ownerName);
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

        // Validate license plate uniqueness (ignoring spaces)
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
        if (licensePlate == null) return null;
        String cleanInput = licensePlate.replace(" ", "");
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate() != null && 
                v.getLicensePlate().replace(" ", "").equalsIgnoreCase(cleanInput)) {
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

        String cleanPlate = licensePlate.replace(" ", "");

        // 1. Kiểm tra ràng buộc liên kết Booking
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            String bVehId = bookings.get(i).getVehicleId();
            if (bVehId != null && bVehId.replace(" ", "").equalsIgnoreCase(cleanPlate)) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated booking(s)!");
                return;
            }
        }

        // 2. Kiểm tra ràng buộc liên kết Lịch sử rửa xe (History)
        for (int i = 0; i < historyList.size(); i++) {
            String hPlate = historyList.get(i).getPlateNumber();
            if (hPlate != null && hPlate.replace(" ", "").equalsIgnoreCase(cleanPlate)) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated history records!");
                return;
            }
        }

        // Tiến hành xóa nếu không có liên kết
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            String listPlate = vehicleList.get(i).getLicensePlate();
            if (listPlate != null && listPlate.replace(" ", "").equalsIgnoreCase(cleanPlate)) {
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
    
    public Vehicle findVehicleByID(String ID) {
        if (ID == null) return null;
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getId() != null && 
                v.getId().equals(ID)) {
                return v;
            }
        }
        return null;
    }
}