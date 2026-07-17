package app;

import util.DataSeeder;
import util.FileManager;
import service.BookingService;
import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;
import service.HistoryService;
import util.ConsoleInputter;
import datastructure.MyLinkedList;
import model.Period;
import model.History;
import model.Booking;
import model.Customer;
import model.Vehicle;
import service.SimulationService;

public class Main {
    public static void main(String[] args) {
        // 1. System initialization
        CustomerService customerService = new CustomerService();
        WashServiceManager washService = new WashServiceManager();
        VehicleService vehicleService = new VehicleService();
        BookingService bookingService = new BookingService();
        HistoryService historyService = new HistoryService();

        // Temporary lists for Period and History
        MyLinkedList<Period> periodsList = new MyLinkedList<>();
        MyLinkedList<History> historyList = new MyLinkedList<>();
        
        SimulationService simulationService = new SimulationService(periodsList);

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
        FileManager.loadExtraData(bookingService.getBookingList(), periodsList, historyList);

        // Populate active bookingQueue from loaded bookingList for compatibility
        MyLinkedList<Booking> list = bookingService.getBookingList();
        for (int i = 0; i < list.size(); i++) {
            Booking b = list.get(i);
            if ("WAITING".equalsIgnoreCase(b.getBookingStatus()) || "Dang cho".equalsIgnoreCase(b.getBookingStatus()) || "SERVING".equalsIgnoreCase(b.getBookingStatus()) || "Dang rua".equalsIgnoreCase(b.getBookingStatus())) {
                bookingService.getBookingQueue().enqueue(b);
            }
        }

        // 3. Main Menu Loop
        while (true) {
            int choice = ConsoleInputter.intMenu("CAR WASH MANAGEMENT SYSTEM",
                    "Customer Management",
                    "Service Management",
                    "Vehicle Management",
                    "Queue Management (Booking)",
                    "Simulation Time Settings",
                    "History Reports",
                    "Customer Portal",
                    "Exit & Save Data");

            switch (choice) {
                case 1:
                    // Customer Management Submenu
                    boolean backToMain = false;
                    while (!backToMain) {
                        int subChoice = ConsoleInputter.intMenu("CUSTOMER MANAGEMENT",
                                "Display all customers",
                                "Add new customer",
                                "Search customer",
                                "Update customer information",
                                "Delete customer",
                                "Back to main menu");
                        switch (subChoice) {
                            case 1:
                                customerService.displayAllCustomers();
                                break;
                            case 2:
                                String name = ConsoleInputter.getStr("Enter customer name");
                                String phone = ConsoleInputter.getStr("Enter phone number");
                                customerService.addCustomer(name, phone, "MEMBER", 0);
                                break;
                            case 3:
                                String query = ConsoleInputter.getStr("Enter search query (ID, Name, or Phone)");
                                customerService.searchCustomers(query);
                                break;
                            case 4:
                                String updateId = ConsoleInputter.getStr("Enter customer ID to update");
                                Customer c = customerService.findCustomerById(updateId);
                                if (c != null) {
                                    String newName = ConsoleInputter.updateStr("Enter new name", c.getName());
                                    String newPhone = ConsoleInputter.updateStr("Enter new phone", c.getPhone());
                                    customerService.updateCustomer(updateId, newName, newPhone, c.getMembershipLevel(), c.getPoints());
                                } else {
                                    System.out.println("=> Error: Customer not found with ID: " + updateId);
                                }
                                break;
                            case 5:
                                String deleteId = ConsoleInputter.getStr("Enter customer ID to delete");
                                customerService.deleteCustomer(deleteId, vehicleService, bookingService, historyList);
                                break;
                            case 6:
                                backToMain = true;
                                break;
                        }
                    }
                    break;
                case 2:
                    // Service Management Submenu
                    boolean backToMainServ = false;
                    while (!backToMainServ) {
                        int subChoice = ConsoleInputter.intMenu("SERVICE MANAGEMENT",
                                "Display all services",
                                "Add new service",
                                "Search service",
                                "Update service details",
                                "Delete service",
                                "Sort services by Price",
                                "Sort services by Duration",
                                "Back to main menu");
                        switch (subChoice) {
                            case 1:
                                washService.displayAllServices();
                                break;
                            case 2:
                                String name = ConsoleInputter.getStr("Enter service name");
                                double price = ConsoleInputter.getDouble("Enter price (VND)", 0.01, 100000000.0);
                                int duration = ConsoleInputter.getInt("Enter duration (minutes)", 1, 1440);
                                String status = ConsoleInputter.getStr("Enter status (ACTIVE/INACTIVE)");
                                washService.addService(name, price, duration, status);
                                break;
                            case 3:
                                String query = ConsoleInputter.getStr("Enter search query (ID or Name)");
                                washService.searchServices(query);
                                break;
                            case 4:
                                String updateId = ConsoleInputter.getStr("Enter service ID to update");
                                model.WashPackage ws = washService.findServiceById(updateId);
                                if (ws != null) {
                                    String newName = ConsoleInputter.updateStr("Enter new name", ws.getName());
                                    
                                    double newPriceVal = ws.getPrice();
                                    while (true) {
                                        System.out.printf("Enter new price (Old: %.1f, Enter to skip): ", ws.getPrice());
                                        String prStr = ConsoleInputter.scanner.nextLine().trim();
                                        if (prStr.isEmpty()) {
                                            break;
                                        }
                                        try {
                                            double tempPrice = Double.parseDouble(prStr);
                                            if (tempPrice > 0) {
                                                newPriceVal = tempPrice;
                                                break;
                                            } else {
                                                System.out.println("=> Error: Service price must be greater than 0!");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("=> Error: Please enter a valid number!");
                                        }
                                    }
                                    
                                    int newDuration = ConsoleInputter.updateInt("Enter new duration", ws.getDuration());
                                    String newStatus = ConsoleInputter.updateStr("Enter new status (ACTIVE/INACTIVE)", ws.getStatus());
                                    washService.updateService(updateId, newName, newPriceVal, newDuration, newStatus);
                                } else {
                                    System.out.println("=> Error: Service not found with ID: " + updateId);
                                }
                                break;
                            case 5:
                                String deleteId = ConsoleInputter.getStr("Enter service ID to delete");
                                washService.deleteService(deleteId, bookingService, historyList);
                                break;
                            case 6:
                                int priceSortChoice = ConsoleInputter.intMenu("SORT BY PRICE", "Ascending", "Descending");
                                washService.sortServicesByPrice(priceSortChoice == 1);
                                washService.displayAllServices();
                                break;
                            case 7:
                                int durSortChoice = ConsoleInputter.intMenu("SORT BY DURATION", "Ascending", "Descending");
                                washService.sortServicesByDuration(durSortChoice == 1);
                                washService.displayAllServices();
                                break;
                            case 8:
                                backToMainServ = true;
                                break;
                        }
                    }
                    break;
                case 3:
                    // Vehicle Management Submenu
                    boolean backToMainVeh = false;
                    while (!backToMainVeh) {
                        int subChoice = ConsoleInputter.intMenu("VEHICLE MANAGEMENT",
                                "Display all vehicles",
                                "Display vehicles of a customer",
                                "Add new vehicle",
                                "Search vehicle",
                                "Update vehicle owner",
                                "Delete vehicle",
                                "Back to main menu");
                        switch (subChoice) {
                            case 1:
                                vehicleService.displayAllVehicles(customerService);
                                break;
                            case 2:
                                String custId = ConsoleInputter.getStr("Enter Customer ID to filter");
                                vehicleService.displayVehiclesByCustomer(custId, customerService);
                                break;
                            case 3:
                                String plate = ConsoleInputter.getStr("Enter license plate");
                                String ownerId = ConsoleInputter.getStr("Enter owner Customer ID");
                                vehicleService.addVehicle(plate, ownerId, customerService);
                                break;
                            case 4:
                                String searchPlate = ConsoleInputter.getStr("Enter license plate to search");
                                Vehicle v = vehicleService.findVehicleByLicense(searchPlate);
                                if (v != null) {
                                    System.out.println("\nFound vehicle: " + v.toString());
                                } else {
                                    System.out.println("=> Vehicle not found with license plate: " + searchPlate);
                                }
                                break;
                            case 5:
                                String updatePlate = ConsoleInputter.getStr("Enter license plate to update");
                                Vehicle veh = vehicleService.findVehicleByLicense(updatePlate);
                                if (veh != null) {
                                    String newOwnerId = ConsoleInputter.getStr("Enter new owner Customer ID");
                                    vehicleService.updateVehicle(updatePlate, newOwnerId, customerService);
                                } else {
                                    System.out.println("=> Vehicle not found with license plate: " + updatePlate);
                                }
                                break;
                            case 6:
                                String deletePlate = ConsoleInputter.getStr("Enter license plate to delete");
                                vehicleService.deleteVehicle(deletePlate, bookingService, historyList);
                                break;
                            case 7:
                                backToMainVeh = true;
                                break;
                        }
                    }
                    break;
                case 4:
                    bookingService.displayQueue();
                    break;
                case 5:
                    // Simulation Time Settings Submenu
                    boolean backToMainSim = false;
                    while (!backToMainSim) {
                        int subChoice = ConsoleInputter.intMenu("SIMULATION TIME SETTINGS",
                                "View current simulation time",
                                "Set current date",
                                "Set current period",
                                "Back to main menu");
                        switch (subChoice) {
                            case 1:
                                simulationService.displayCurrentTime();
                                break;
                            case 2:
                                String newDate = ConsoleInputter.getStr("Enter new date (YYYY-MM-DD)");
                                simulationService.setCurrentDate(newDate);
                                break;
                            case 3:
                                int periodChoice = ConsoleInputter.intMenu("SELECT PERIOD",
                                        "MORNING",
                                        "AFTERNOON",
                                        "EVENING");
                                simulationService.setCurrentPeriod(periodChoice);
                                break;
                            case 4:
                                backToMainSim = true;
                                break;
                        }
                    }
                    break;
                case 6:
                    // History Reports Submenu
                    boolean backToMainHist = false;
                    while (!backToMainHist) {
                        int subChoice = ConsoleInputter.intMenu("HISTORY REPORTS",
                                "Global History Report",
                                "Customer History Report",
                                "Back to main menu");
                        switch (subChoice) {
                            case 1:
                                historyService.displayGlobalHistory(historyList);
                                break;
                            case 2:
                                String custId = ConsoleInputter.getStr("Enter customer ID");
                                historyService.displayCustomerHistory(historyList, custId);
                                break;
                            case 3:
                                backToMainHist = true;
                                break;
                            default:
                                System.out.println("Please select a valid option!");
                        }
                    }
                    break;
                case 7:
                    // Customer Portal
                    String loginId = ConsoleInputter.getStr("Enter your Customer ID to login");
                    Customer currentCustomer = customerService.findCustomerById(loginId);
                    if (currentCustomer == null) {
                        System.out.println("=> Login failed. Customer not found.");
                        break;
                    }
                    System.out.println("=> Welcome back, " + currentCustomer.getName() + "!");
                    boolean backToMainCust = false;
                    while (!backToMainCust) {
                        int subChoice = ConsoleInputter.intMenu("CUSTOMER PORTAL",
                                "View my wash history",
                                "Logout");
                        switch (subChoice) {
                            case 1:
                                historyService.displayCustomerHistory(historyList, currentCustomer.getCustomerId());
                                break;
                            case 2:
                                backToMainCust = true;
                                System.out.println("=> Logged out successfully.");
                                break;
                            default:
                                System.out.println("Please select a valid option!");
                        }
                    }
                    break;
                case 8:
                    // Auto-save data before exiting
                    FileManager.saveData(
                            customerService.getCustomerList(), 
                            washService.getServiceList(), 
                            vehicleService.getVehicleList()
                    );
                    FileManager.saveExtraData(bookingService.getBookingList(), periodsList, historyList);
                    System.out.println("=> System closed. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a valid option!");
            }
        }
    }
}