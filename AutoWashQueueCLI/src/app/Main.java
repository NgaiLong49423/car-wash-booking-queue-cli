package app;

import util.DataSeeder;
import util.FileManager;
import service.BookingService;
import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;
import util.ConsoleInputter;
import datastructure.MyLinkedList;
import model.Period;
import model.History;

public class Main {
    public static void main(String[] args) {
        // 1. System initialization
        CustomerService customerService = new CustomerService();
        WashServiceManager washService = new WashServiceManager();
        VehicleService vehicleService = new VehicleService();
        BookingService bookingService = new BookingService();

        // Temporary lists for Period and History
        MyLinkedList<Period> periodsList = new MyLinkedList<>();
        MyLinkedList<History> historyList = new MyLinkedList<>();

        // 2. Load data from files
        boolean hasSavedData = FileManager.loadData(
                customerService.getCustomerList(), 
                washService.getServiceList(), 
                vehicleService.getVehicleList()
        );
        
        if (!hasSavedData) {
            DataSeeder.seed(customerService, washService, vehicleService);
        } else {
            System.out.println("\n[System] -> Data loaded from .txt files successfully!");
        }

        // Load extra data (Bookings, Periods, History)
        FileManager.loadExtraData(bookingService.getBookingQueue(), periodsList, historyList);

        // 3. Main Menu Loop
        while (true) {
            int choice = ConsoleInputter.intMenu("CAR WASH MANAGEMENT SYSTEM",
                    "Customer Management",
                    "Service Management",
                    "Vehicle Management",
                    "Queue Management (Booking)",
                    "Exit & Save Data");

            switch (choice) {
                case 1:
                    customerService.displayAllCustomers();
                    break;
                case 2:
                    washService.displayAllServices();
                    break;
                case 3:
                    vehicleService.displayAllVehicles();
                    break;
                case 4:
                    bookingService.displayQueue();
                    break;
                case 5:
                    // Auto-save data before exiting
                    FileManager.saveData(
                            customerService.getCustomerList(), 
                            washService.getServiceList(), 
                            vehicleService.getVehicleList()
                    );
                    FileManager.saveExtraData(bookingService.getBookingQueue(), periodsList, historyList);
                    System.out.println("=> System closed. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a valid option!");
            }
        }
    }
}