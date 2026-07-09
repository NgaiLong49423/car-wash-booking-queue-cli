package util;

import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;
import model.Booking;
import model.Period;
import model.History;
import datastructure.MyLinkedList;
import datastructure.MyQueue; // Đã thêm thư viện MyQueue

public class DataSeeder {
    
    public static void seed(CustomerService customerService, WashServiceManager washService, VehicleService vehicleService) {
        
        // 1. Mock Customers (4 tiers as requested: PLATINUM, GOLD, SILVER, MEMBER)
        customerService.addCustomer("Nguyen Van A", "0901234567", "PLATINUM", 100);
        customerService.addCustomer("Tran Thi B", "0912345678", "GOLD", 50);
        customerService.addCustomer("Le Van C", "0988777666", "SILVER", 20);
        customerService.addCustomer("Pham Van D", "0999888777", "MEMBER", 0);

        // 2. Mock Services (Translated to English)
        washService.addService("S001", "Standard Wash", 50000);
        washService.addService("S002", "Premium Wash + Vacuum", 100000);
        washService.addService("S003", "Wash + Polish", 250000);

        // 3. Mock Vehicles (Translated to English)
        vehicleService.addVehicle("59A-123.45", "C001", "Sedan 4-seater");
        vehicleService.addVehicle("60B-678.90", "C002", "SUV 7-seater");
        vehicleService.addVehicle("51C-999.99", "C003", "Pickup Truck");
        vehicleService.addVehicle("51D-111.11", "C004", "Minivan");
        
        // 4. Generate extra mock data files
        seedMissingFiles();

        System.out.println("\n[System] -> Data loaded from .txt files successfully!");
    }

    private static void seedMissingFiles() {
        // Mock Bookings (Waiting status translated)
        // Đã đổi sang MyQueue để đồng bộ với FileManager
        MyQueue<Booking> mockBookings = new MyQueue<>();
        mockBookings.enqueue(new Booking("B001", "59A-123.45", "S001", "Waiting"));
        mockBookings.enqueue(new Booking("B002", "60B-678.90", "S002", "Waiting"));

        // Mock Periods (Morning, Afternoon, Evening translated)
        MyLinkedList<Period> mockPeriods = new MyLinkedList<>();
        mockPeriods.addLast(new Period("P1", "Morning", true));
        mockPeriods.addLast(new Period("P2", "Afternoon", false));
        mockPeriods.addLast(new Period("P3", "Evening", false));

        // Mock History
        MyLinkedList<History> mockHistory = new MyLinkedList<>();
        mockHistory.addLast(new History("H001", "B000", "51C-999.99", "S003", "2026-07-09 10:00"));

        // Save to files
        FileManager.saveExtraData(mockBookings, mockPeriods, mockHistory);
    }
}