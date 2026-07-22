package service;

import datastructure.MyLinkedList;
import model.Vehicle;
import model.Booking;
import model.History;
import model.Customer;
import util.FileManager;
import util.TablePrinter;

public class VehicleService {
    private MyLinkedList<Vehicle> vehicleList;

    public VehicleService() {
        this.vehicleList = new MyLinkedList<>();
    }

    public void displayAllVehicles(CustomerService customerService) {
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles found in the system!");
            return;
        }
        printVehicles("Vehicle List", vehicleList, customerService);
    }

    public void displayVehiclesByCustomer(String customerId, CustomerService customerService) {
        MyLinkedList<Vehicle> vehicles = new MyLinkedList<>();
        for (int i = 0; i < vehicleList.size(); i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getCustomerId().equalsIgnoreCase(customerId)) {
                vehicles.addLast(v);
            }
        }
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles associated with this customer!");
            return;
        }
        printVehicles("Vehicle List for Customer " + customerId.toUpperCase(), vehicles, customerService);
    }

    public void addVehicle(String licensePlate, String customerId, CustomerService customerService) {
        // Validate inputs
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            System.out.println("=> Error: License plate cannot be empty!");
            return;
        }
        if (!isValidLicensePlate(licensePlate)) {
            System.out.println("=> Error: License plate must use a format such as 59A-123.45 or 59A1-123.45.");
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

        String normalizedPlate = licensePlate.trim().toUpperCase();
        Vehicle newVehicle = new Vehicle(newId, normalizedPlate, customerId.trim().toUpperCase());
        vehicleList.addLast(newVehicle);
        System.out.println("=> Added vehicle successfully: " + licensePlate + " (Vehicle ID: " + newId + ")");
        
        // Auto-save on change (FR-23)
        FileManager.saveVehicles(vehicleList);
    }

    public Vehicle findVehicleByLicense(String licensePlate) {
        if (licensePlate == null) return null;
        String cleanInput = normalizeLicensePlate(licensePlate);
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate() != null && 
                normalizeLicensePlate(v.getLicensePlate()).equalsIgnoreCase(cleanInput)) {
                return v;
            }
        }
        return null;
    }

    public Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            return null;
        }

        for (int i = 0; i < vehicleList.size(); i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getId().equalsIgnoreCase(vehicleId.trim())) {
                return v;
            }
        }

        return null;
    }

    public Vehicle findVehicleByIdOrLicense(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        Vehicle byId = findVehicleById(input);
        if (byId != null) {
            return byId;
        }

        return findVehicleByLicense(input);
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

        String cleanPlate = normalizeLicensePlate(licensePlate);

        // 1. Kiểm tra ràng buộc liên kết Booking
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            String bVehId = bookings.get(i).getVehicleId();
            boolean sameVehicleId = bVehId != null && bVehId.equalsIgnoreCase(v.getId());
            boolean samePlate = bVehId != null && normalizeLicensePlate(bVehId).equalsIgnoreCase(cleanPlate);
            if (sameVehicleId || samePlate) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated booking(s)!");
                return;
            }
        }

        // 2. Kiểm tra ràng buộc liên kết Lịch sử rửa xe (History)
        for (int i = 0; i < historyList.size(); i++) {
            String hPlate = historyList.get(i).getPlateNumber();
            if (hPlate != null && normalizeLicensePlate(hPlate).equalsIgnoreCase(cleanPlate)) {
                System.out.println("=> Error: Cannot delete vehicle " + licensePlate + " because it has associated history records!");
                return;
            }
        }

        // Tiến hành xóa nếu không có liên kết
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            String listPlate = vehicleList.get(i).getLicensePlate();
            if (listPlate != null && normalizeLicensePlate(listPlate).equalsIgnoreCase(cleanPlate)) {
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

    private boolean isValidLicensePlate(String licensePlate) {
        return licensePlate != null
                && licensePlate.trim().matches("(?i)\\d{2}[A-Z]\\d?[- ]?\\d{3}[.]?\\d{2}");
    }

    private String normalizeLicensePlate(String licensePlate) {
        return licensePlate == null ? "" : licensePlate.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    private void printVehicles(String title, MyLinkedList<Vehicle> vehicles,
            final CustomerService customerService) {
        TablePrinter.printTable(title, vehicles,
                new TablePrinter.Column<Vehicle>("ID", 6, Vehicle::getId),
                new TablePrinter.Column<Vehicle>("LICENSE PLATE", 16, Vehicle::getLicensePlate),
                new TablePrinter.Column<Vehicle>("OWNER ID", 10, Vehicle::getCustomerId),
                new TablePrinter.Column<Vehicle>("OWNER NAME", 24, vehicle -> {
                    Customer owner = customerService.findCustomerById(vehicle.getCustomerId());
                    return owner == null ? "Unknown" : owner.getName();
                }));
    }
}
