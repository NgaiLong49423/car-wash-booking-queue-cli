package util;

import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;
import model.Customer;
import model.Vehicle;
import model.WashPackage;
import model.Booking;
import model.Period;
import model.History;
import datastructure.MyLinkedList;
import java.io.File;

public class DataSeeder {
    
    public static void seed(CustomerService customerService, WashServiceManager washService, VehicleService vehicleService) {
        // Clear lists
        customerService.getCustomerList().clear();
        washService.getServiceList().clear();
        vehicleService.getVehicleList().clear();

        // 1. Mock Customers (7 fields: id|name|phone|membershipLevel|points|totalSpent|visitCount)
        customerService.getCustomerList().addLast(new Customer("C001", "Nguyen Van A", "0901234567", "PLATINUM", 100, 1500000.0, 5));
        customerService.getCustomerList().addLast(new Customer("C002", "Tran Thi B", "0912345678", "GOLD", 50, 800000.0, 3));
        customerService.getCustomerList().addLast(new Customer("C003", "Le Van C", "0988777666", "SILVER", 20, 300000.0, 1));
        customerService.getCustomerList().addLast(new Customer("C004", "Pham Van D", "0999999999", "MEMBER", 0, 0.0, 0));

        // 2. Mock Services (5 fields: id|name|price|duration|status)
        washService.getServiceList().addLast(new WashPackage("S001", "Standard Wash", 50000.0, 30, "ACTIVE"));
        washService.getServiceList().addLast(new WashPackage("S002", "Premium Wash + Vacuum", 100000.0, 45, "ACTIVE"));
        washService.getServiceList().addLast(new WashPackage("S003", "Wash + Polish", 250000.0, 60, "ACTIVE"));

        // 3. Mock Vehicles (3 fields: id|licensePlate|customerId)
        vehicleService.getVehicleList().addLast(new Vehicle("V001", "59A-123.45", "C001"));
        vehicleService.getVehicleList().addLast(new Vehicle("V002", "60B-678.90", "C002"));
        vehicleService.getVehicleList().addLast(new Vehicle("V003", "51C-999.99", "C003"));
        vehicleService.getVehicleList().addLast(new Vehicle("V004", "51D-111.11", "C004"));

        // Ensure data directory exists
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Save core files
        FileManager.saveData(customerService.getCustomerList(), washService.getServiceList(), vehicleService.getVehicleList());

        // 4. Mock extra files (Bookings, Periods, Histories)
        MyLinkedList<Booking> mockBookings = new MyLinkedList<>();
        mockBookings.addLast(new Booking("B001", "C001", "V001", "S001", "2026-07-10", "MORNING", "WAITING", "UNPAID", "NONE", System.currentTimeMillis()));
        mockBookings.addLast(new Booking("B002", "C002", "V002", "S002", "2026-07-10", "MORNING", "WAITING", "UNPAID", "NONE", System.currentTimeMillis() + 1000));

        // Mock Periods (date|periodName|status)
        MyLinkedList<Period> mockPeriods = new MyLinkedList<>();
        mockPeriods.addLast(new Period("2026-07-10", "MORNING", "ACTIVATED"));
        mockPeriods.addLast(new Period("2026-07-10", "AFTERNOON", "NOT_ACTIVATED"));
        mockPeriods.addLast(new Period("2026-07-10", "EVENING", "NOT_ACTIVATED"));

        // Mock History (8 fields: bookingId|customerId|customerName|plateNumber|serviceName|completedTime|amountPaid|loyaltyPointsEarned)
        MyLinkedList<History> mockHistory = new MyLinkedList<>();
        mockHistory.addLast(new History("B003", "C003", "Le Van C", "51C-999.99", "Wash + Polish", "2026-07-09 10:00", 250000.0, 20));

        // Save extra files
        FileManager.saveExtraData(mockBookings, mockPeriods, mockHistory);

        System.out.println("\n[System] -> Seed data generated and saved successfully!");
    }
}