package app;

import datastructure.MyLinkedList;
import model.Booking;
import model.BookingActionResult;
import model.CancellationResult;
import model.CompletionResult;
import model.Customer;
import model.History;
import model.Period;
import model.PeriodActivationResult;
import model.UndoResult;
import model.Vehicle;
import model.WashPackage;
import service.BookingService;
import service.CancellationService;
import service.CompletionService;
import service.CustomerService;
import service.HistoryService;
import service.SimulationService;
import service.UndoService;
import service.VehicleService;
import service.WashServiceManager;
import util.ConsoleInputter;
import util.DataSeeder;
import util.FileManager;

public class Main {
    private static final String PHONE_PATTERN = "0\\d{9}";
    private static final String LICENSE_PLATE_PATTERN = "(?i)\\d{2}[A-Z]\\d?[- ]?\\d{3}[.]?\\d{2}";

    public static void main(String[] args) {
        AppContext context = new AppContext();
        boolean running = true;

        while (running) {
            int choice = ConsoleInputter.intMenuWithZero("MAIN MENU", "Exit",
                    "Customer Menu",
                    "Admin Menu");
            switch (choice) {
                case 1:
                    runCustomerSession(context);
                    break;
                case 2:
                    runAdminMenu(context);
                    break;
                case 0:
                    context.saveAll();
                    System.out.println("=> System closed. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void runCustomerSession(AppContext context) {
        Customer currentCustomer = loginCustomer(context.customerService);
        if (currentCustomer == null) {
            return;
        }

        System.out.println("=> Welcome, " + currentCustomer.getName() + "!");
        boolean loggedIn = true;
        while (loggedIn) {
            int choice = ConsoleInputter.intMenuWithZero("CUSTOMER MENU", "Log out",
                    "View service list",
                    "View my profile",
                    "Create my booking",
                    "View my active bookings",
                    "Cancel my booking",
                    "View my wash history");
            switch (choice) {
                case 1:
                    context.washService.displayAllServices();
                    break;
                case 2:
                    displayCustomerProfile(currentCustomer);
                    break;
                case 3:
                    createBookingForCustomer(context, currentCustomer.getId());
                    break;
                case 4:
                    context.bookingService.displayCustomerActiveBookings(currentCustomer.getId(),
                            context.customerService, context.vehicleService, context.washService);
                    break;
                case 5:
                    context.bookingService.displayCustomerActiveBookings(currentCustomer.getId(),
                            context.customerService, context.vehicleService, context.washService);
                    String bookingId = ConsoleInputter.getStr("Enter booking ID to cancel");
                    printCancellationResult(context.cancellationService.cancelAsCustomer(
                            currentCustomer.getId(), bookingId));
                    break;
                case 6:
                    context.historyService.displayCustomerHistory(context.historyList, currentCustomer.getId());
                    break;
                case 0:
                    System.out.println("=> Customer session ended.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static Customer loginCustomer(CustomerService customerService) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            String customerId = ConsoleInputter.getStr("Enter customer ID");
            Customer customer = customerService.findCustomerById(customerId);
            if (customer != null) {
                return customer;
            }

            int remainingAttempts = 3 - attempt;
            if (remainingAttempts > 0) {
                System.out.println("=> Error: Customer ID was not found. Attempts remaining: "
                        + remainingAttempts + ".");
            }
        }
        System.out.println("=> Login failed after 3 attempts. Returning to Main Menu.");
        return null;
    }

    private static void displayCustomerProfile(Customer customer) {
        System.out.println("\n--- MY PROFILE ---");
        System.out.println("Customer ID    : " + customer.getId());
        System.out.println("Name           : " + customer.getName());
        System.out.println("Phone          : " + customer.getPhone());
        System.out.println("Membership tier: " + customer.getMembershipLevel());
        System.out.println("Loyalty points : " + customer.getPoints());
        System.out.printf("Total spent    : %.0f VND%n", customer.getTotalSpent());
        System.out.println("Visit count    : " + customer.getVisitCount());
        System.out.println("Booking window : " + bookingWindowDays(customer.getMembershipLevel()) + " day(s)");
        System.out.println("------------------");
    }

    private static void runAdminMenu(AppContext context) {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            int choice = ConsoleInputter.intMenuWithZero("ADMIN MENU", "Back to Main Menu",
                    "Manage customers",
                    "Manage vehicles",
                    "Manage services",
                    "Set current date/period",
                    "Create booking for customer",
                    "Activate current service period",
                    "View queues/bookings by period",
                    "Process next booking",
                    "Confirm payment",
                    "Complete current booking",
                    "Cancel booking",
                    "View history",
                    "Undo last completed booking");
            switch (choice) {
                case 1:
                    runCustomerManagement(context);
                    break;
                case 2:
                    runVehicleManagement(context);
                    break;
                case 3:
                    runServiceManagement(context);
                    break;
                case 4:
                    runSimulationSettings(context);
                    break;
                case 5:
                    String customerId = ConsoleInputter.getStr("Enter customer ID");
                    createBookingForCustomer(context, customerId);
                    break;
                case 6:
                    PeriodActivationResult activationResult = context.simulationService.activateCurrentPeriod(
                            context.bookingService, context.customerService);
                    System.out.println("=> " + activationResult.getMessage());
                    break;
                case 7:
                    runQueueMonitoring(context);
                    break;
                case 8:
                    BookingActionResult processingResult = context.bookingService.processNextBooking();
                    System.out.println("=> " + processingResult.getMessage());
                    break;
                case 9:
                    confirmPayment(context);
                    break;
                case 10:
                    completeCurrentBooking(context);
                    break;
                case 11:
                    String bookingId = ConsoleInputter.getStr("Enter booking ID to cancel");
                    printCancellationResult(context.cancellationService.cancelAsAdmin(bookingId));
                    break;
                case 12:
                    runHistoryReports(context);
                    break;
                case 13:
                    UndoResult undoResult = context.undoService.undoLastCompletion();
                    System.out.println("=> " + undoResult.getMessage());
                    if (undoResult.isSuccessful() && undoResult.getReturnedToWaitlist() != null) {
                        System.out.println("   Returned to Waitlist: "
                                + undoResult.getReturnedToWaitlist().getBookingId());
                    }
                    break;
                case 0:
                    inAdminMenu = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void runCustomerManagement(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("CUSTOMER MANAGEMENT", "Back to Admin Menu",
                    "Display all customers",
                    "Search customers",
                    "Add new customer",
                    "Update customer information",
                    "Delete customer");
            switch (choice) {
                case 1:
                    context.customerService.displayAllCustomers();
                    break;
                case 2:
                    context.customerService.searchCustomers(
                            ConsoleInputter.getStr("Enter customer ID, name, or phone"));
                    break;
                case 3:
                    String name = ConsoleInputter.getStr("Enter customer name");
                    String phone = ConsoleInputter.getStr("Enter phone number", PHONE_PATTERN,
                            "Error: Phone number must contain exactly 10 digits and start with 0.");
                    context.customerService.addCustomer(name, phone, "MEMBER", 0);
                    break;
                case 4:
                    updateCustomer(context);
                    break;
                case 5:
                    String deleteId = ConsoleInputter.getStr("Enter customer ID to delete");
                    if (ConsoleInputter.getBoolean("Confirm deletion of " + deleteId + "?")) {
                        context.customerService.deleteCustomer(deleteId, context.vehicleService,
                                context.bookingService, context.historyList);
                    } else {
                        System.out.println("=> Deletion cancelled.");
                    }
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void updateCustomer(AppContext context) {
        String customerId = ConsoleInputter.getStr("Enter customer ID to update");
        Customer customer = context.customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("=> Error: Customer not found with ID: " + customerId);
            return;
        }

        displayCustomerProfile(customer);
        String newName = ConsoleInputter.updateStr("Enter new name", customer.getName());
        String newPhone;
        while (true) {
            newPhone = ConsoleInputter.updateStr("Enter new phone", customer.getPhone());
            if (newPhone.matches(PHONE_PATTERN)) {
                break;
            }
            System.out.println("Error: Phone number must contain exactly 10 digits and start with 0.");
        }
        context.customerService.updateCustomer(customerId, newName, newPhone,
                customer.getMembershipLevel(), customer.getPoints());
    }

    private static void runVehicleManagement(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("VEHICLE MANAGEMENT", "Back to Admin Menu",
                    "Display all vehicles",
                    "Display vehicles by customer",
                    "Add new vehicle",
                    "Search vehicle by license plate",
                    "Update vehicle owner",
                    "Delete vehicle");
            switch (choice) {
                case 1:
                    context.vehicleService.displayAllVehicles(context.customerService);
                    break;
                case 2:
                    context.vehicleService.displayVehiclesByCustomer(
                            ConsoleInputter.getStr("Enter customer ID"), context.customerService);
                    break;
                case 3:
                    String plate = ConsoleInputter.getStr("Enter license plate", LICENSE_PLATE_PATTERN,
                            "Error: Use a license plate format such as 59A-123.45 or 59A1-123.45.");
                    String ownerId = ConsoleInputter.getStr("Enter owner customer ID");
                    context.vehicleService.addVehicle(plate, ownerId, context.customerService);
                    break;
                case 4:
                    String searchPlate = ConsoleInputter.getStr("Enter license plate to search");
                    Vehicle vehicle = context.vehicleService.findVehicleByLicense(searchPlate);
                    System.out.println(vehicle == null
                            ? "=> Vehicle not found with license plate: " + searchPlate
                            : "=> Found vehicle: " + vehicle);
                    break;
                case 5:
                    String updatePlate = ConsoleInputter.getStr("Enter license plate to update");
                    String newOwnerId = ConsoleInputter.getStr("Enter new owner customer ID");
                    context.vehicleService.updateVehicle(updatePlate, newOwnerId, context.customerService);
                    break;
                case 6:
                    String deletePlate = ConsoleInputter.getStr("Enter license plate to delete");
                    if (ConsoleInputter.getBoolean("Confirm deletion of " + deletePlate + "?")) {
                        context.vehicleService.deleteVehicle(deletePlate, context.bookingService, context.historyList);
                    } else {
                        System.out.println("=> Deletion cancelled.");
                    }
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void runServiceManagement(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("SERVICE MANAGEMENT", "Back to Admin Menu",
                    "Display all services",
                    "Add new service",
                    "Search services",
                    "Update service",
                    "Delete service",
                    "Sort services by price",
                    "Sort services by duration");
            switch (choice) {
                case 1:
                    context.washService.displayAllServices();
                    break;
                case 2:
                    String name = ConsoleInputter.getStr("Enter service name");
                    double price = ConsoleInputter.getDouble("Enter price (VND)", 0.01, 100000000.0);
                    int duration = ConsoleInputter.getInt("Enter duration (minutes)", 1, 1440);
                    String status = selectServiceStatus();
                    context.washService.addService(name, price, duration, status);
                    break;
                case 3:
                    context.washService.searchServices(
                            ConsoleInputter.getStr("Enter service ID or name"));
                    break;
                case 4:
                    updateService(context);
                    break;
                case 5:
                    String deleteId = ConsoleInputter.getStr("Enter service ID to delete");
                    if (ConsoleInputter.getBoolean("Confirm deletion of " + deleteId + "?")) {
                        context.washService.deleteService(deleteId, context.bookingService, context.historyList);
                    } else {
                        System.out.println("=> Deletion cancelled.");
                    }
                    break;
                case 6:
                    context.washService.sortServicesByPrice(selectSortOrder());
                    context.washService.displayAllServices();
                    break;
                case 7:
                    context.washService.sortServicesByDuration(selectSortOrder());
                    context.washService.displayAllServices();
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void updateService(AppContext context) {
        String serviceId = ConsoleInputter.getStr("Enter service ID to update");
        WashPackage service = context.washService.findServiceById(serviceId);
        if (service == null) {
            System.out.println("=> Error: Service not found with ID: " + serviceId);
            return;
        }

        String name = ConsoleInputter.updateStr("Enter new name", service.getName());
        double price = ConsoleInputter.updateDouble("Enter new price", service.getPrice());
        int duration = ConsoleInputter.updateInt("Enter new duration", service.getDuration());
        String status = selectServiceStatus();
        context.washService.updateService(serviceId, name, price, duration, status);
    }

    private static void runSimulationSettings(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("SIMULATION TIME SETTINGS", "Back to Admin Menu",
                    "View current simulation time",
                    "Set current date",
                    "Set current period");
            switch (choice) {
                case 1:
                    context.simulationService.displayCurrentTime();
                    break;
                case 2:
                    String date = ConsoleInputter.getIsoDate("Enter current date");
                    if (date != null && !date.isEmpty()) {
                        context.simulationService.setCurrentDate(date);
                        syncCurrentQueues(context.bookingService, context.simulationService, context.customerService);
                    }
                    break;
                case 3:
                    context.simulationService.setCurrentPeriod(selectPeriodChoice());
                    syncCurrentQueues(context.bookingService, context.simulationService, context.customerService);
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void createBookingForCustomer(AppContext context, String customerId) {
        Customer customer = context.customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("=> Error: Customer not found with ID: " + customerId);
            return;
        }
        if (!hasOwnedVehicle(context.vehicleService, customer.getId())) {
            System.out.println("=> Error: This customer has no vehicle. Add a vehicle before creating a booking.");
            return;
        }

        context.vehicleService.displayVehiclesByCustomer(customer.getId(), context.customerService);
        String vehicleInput = ConsoleInputter.getStr("Enter vehicle ID or license plate");
        Vehicle vehicle = context.vehicleService.findVehicleByIdOrLicense(vehicleInput);
        if (vehicle == null || !customer.getId().equalsIgnoreCase(vehicle.getCustomerId())) {
            System.out.println("=> Error: Select a vehicle owned by customer " + customer.getId() + ".");
            return;
        }

        context.washService.displayAllServices();
        String serviceId = ConsoleInputter.getStr("Enter active service ID");
        WashPackage service = context.washService.findServiceById(serviceId);
        if (service == null || !context.washService.isServiceActive(serviceId)) {
            System.out.println("=> Error: Select an existing ACTIVE service.");
            return;
        }

        boolean selectingSchedule = true;
        while (selectingSchedule) {
            String date = ConsoleInputter.getIsoDate("Enter booking date");
            String period = selectPeriod();
            boolean created = context.bookingService.createBooking(customer.getId(), vehicle.getId(),
                    service.getId(), date, period, context.customerService, context.vehicleService,
                    context.washService, context.simulationService);
            if (created) {
                return;
            }

            int retry = ConsoleInputter.intMenuWithZero("BOOKING NOT CREATED", "Cancel booking creation",
                    "Try another date and period");
            selectingSchedule = retry == 1;
        }
    }

    private static void runQueueMonitoring(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("QUEUE AND BOOKING MONITORING", "Back to Admin Menu",
                    "Display current Main Queue",
                    "Display current Waitlist",
                    "Display future bookings by date/period",
                    "Display active bookings by customer");
            switch (choice) {
                case 1:
                    context.bookingService.displayMainQueue(context.simulationService.getCurrentPeriodStr(),
                            context.customerService, context.vehicleService, context.washService);
                    break;
                case 2:
                    context.bookingService.displayWaitlist(context.simulationService.getCurrentPeriodStr(),
                            context.customerService, context.vehicleService, context.washService);
                    break;
                case 3:
                    String date = ConsoleInputter.getIsoDate("Enter booking date");
                    String period = selectPeriod();
                    context.bookingService.displayFutureBookings(date, period, context.customerService,
                            context.vehicleService, context.washService,
                            context.simulationService.getCurrentDateStr(),
                            context.simulationService.getCurrentPeriodStr());
                    break;
                case 4:
                    String customerId = ConsoleInputter.getStr("Enter customer ID");
                    context.bookingService.displayCustomerActiveBookings(customerId, context.customerService,
                            context.vehicleService, context.washService);
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void confirmPayment(AppContext context) {
        if (!context.bookingService.displayServingPaymentDetails(context.customerService,
                context.vehicleService, context.washService)) {
            return;
        }
        int choice = ConsoleInputter.intMenuWithZero("PAYMENT METHOD", "Cancel",
                "CASH",
                "BANKING");
        if (choice == 0) {
            System.out.println("=> Payment confirmation cancelled.");
            return;
        }
        String method = choice == 1 ? "CASH" : "BANKING";
        BookingActionResult result = context.bookingService.confirmPayment(method, context.washService);
        System.out.println("=> " + result.getMessage());
        if (result.isSuccessful()) {
            System.out.printf("   Amount: %.0f VND%n", result.getAmount());
        }
    }

    private static void completeCurrentBooking(AppContext context) {
        Booking servingBooking = context.bookingService.findServingBooking();
        if (servingBooking == null) {
            System.out.println("=> Error: There is no SERVING booking to complete.");
            return;
        }
        CompletionResult result = context.completionService.completeBooking(servingBooking.getBookingId());
        System.out.println("=> " + result.getMessage());
        if (!result.isSuccessful()) {
            return;
        }

        Customer customer = result.getCustomer();
        System.out.println("   Loyalty points: " + result.getPreviousPoints() + " -> " + customer.getPoints());
        System.out.println("   Membership tier: " + result.getPreviousTier()
                + " -> " + customer.getMembershipLevel());
        if (result.getPromotedBooking() != null) {
            System.out.println("   Promoted from Waitlist: "
                    + result.getPromotedBooking().getBookingId());
        }
    }

    private static void runHistoryReports(AppContext context) {
        boolean active = true;
        while (active) {
            int choice = ConsoleInputter.intMenuWithZero("HISTORY REPORTS", "Back to Admin Menu",
                    "Display global history",
                    "Display history by customer ID");
            switch (choice) {
                case 1:
                    context.historyService.displayGlobalHistory(context.historyList);
                    break;
                case 2:
                    String customerId = ConsoleInputter.getStr("Enter customer ID");
                    context.historyService.displayCustomerHistory(context.historyList, customerId);
                    break;
                case 0:
                    active = false;
                    break;
                default:
                    System.out.println("=> Error: Please select a valid option.");
            }
        }
    }

    private static void printCancellationResult(CancellationResult result) {
        System.out.println("=> " + result.getMessage());
        if (result.isSuccessful() && result.getPromotedBooking() != null) {
            System.out.println("   Promoted from Waitlist: " + result.getPromotedBooking().getBookingId());
        }
    }

    private static boolean hasOwnedVehicle(VehicleService vehicleService, String customerId) {
        for (int i = 0; i < vehicleService.getVehicleList().size(); i++) {
            if (customerId.equalsIgnoreCase(vehicleService.getVehicleList().get(i).getCustomerId())) {
                return true;
            }
        }
        return false;
    }

    private static String selectPeriod() {
        int choice = selectPeriodChoice();
        if (choice == 1) return "MORNING";
        if (choice == 2) return "AFTERNOON";
        return "EVENING";
    }

    private static int selectPeriodChoice() {
        return ConsoleInputter.intMenu("SELECT PERIOD", "MORNING", "AFTERNOON", "EVENING");
    }

    private static String selectServiceStatus() {
        return ConsoleInputter.intMenu("SELECT SERVICE STATUS", "ACTIVE", "INACTIVE") == 1
                ? "ACTIVE" : "INACTIVE";
    }

    private static boolean selectSortOrder() {
        return ConsoleInputter.intMenu("SELECT SORT ORDER", "Ascending", "Descending") == 1;
    }

    private static int bookingWindowDays(String membershipLevel) {
        if ("SILVER".equalsIgnoreCase(membershipLevel)) return 10;
        if ("GOLD".equalsIgnoreCase(membershipLevel)) return 12;
        if ("PLATINUM".equalsIgnoreCase(membershipLevel)) return 14;
        return 7;
    }

    private static void syncCurrentQueues(BookingService bookingService,
            SimulationService simulationService, CustomerService customerService) {
        if (simulationService.isCurrentPeriodActivated()) {
            bookingService.rebuildCurrentQueues(simulationService.getCurrentDateStr(),
                    simulationService.getCurrentPeriodStr(), customerService);
        } else {
            bookingService.clearCurrentQueues();
        }
    }

    private static final class AppContext {
        private final CustomerService customerService = new CustomerService();
        private final WashServiceManager washService = new WashServiceManager();
        private final VehicleService vehicleService = new VehicleService();
        private final BookingService bookingService = new BookingService();
        private final MyLinkedList<Period> periodsList = new MyLinkedList<>();
        private final MyLinkedList<History> historyList = new MyLinkedList<>();
        private final SimulationService simulationService = new SimulationService(periodsList);
        private final CompletionService completionService;
        private final CancellationService cancellationService;
        private final UndoService undoService;
        private final HistoryService historyService = new HistoryService();

        private AppContext() {
            boolean hasSavedData = FileManager.loadData(customerService.getCustomerList(),
                    washService.getServiceList(), vehicleService.getVehicleList());
            if (!hasSavedData) {
                DataSeeder.seed(customerService, washService, vehicleService);
            } else {
                System.out.println("\n[System] -> Data loaded from .txt files successfully!");
            }

            FileManager.loadExtraData(bookingService.getBookingList(), periodsList, historyList);
            completionService = new CompletionService(bookingService, customerService,
                    washService, vehicleService, historyList);
            cancellationService = new CancellationService(bookingService, washService);
            undoService = new UndoService(bookingService, completionService, customerService, historyList);
            syncCurrentQueues(bookingService, simulationService, customerService);
        }

        private void saveAll() {
            FileManager.saveData(customerService.getCustomerList(), washService.getServiceList(),
                    vehicleService.getVehicleList());
            FileManager.saveExtraData(bookingService.getBookingList(), periodsList, historyList);
        }
    }
}
