import datastructure.MyLinkedList;
import model.Booking;
import model.Customer;
import model.Period;
import model.Vehicle;
import model.WashPackage;
import service.BookingService;
import service.CustomerService;
import service.SimulationService;
import service.VehicleService;
import service.WashServiceManager;

public class BookingServiceFlowTest {
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        VehicleService vehicleService = new VehicleService();
        WashServiceManager washService = new WashServiceManager();
        BookingService bookingService = new BookingService();
        MyLinkedList<Period> periods = new MyLinkedList<>();

        periods.addLast(new Period("2026-07-17", "MORNING", "ACTIVATED"));
        periods.addLast(new Period("2026-07-17", "AFTERNOON", "NOT_ACTIVATED"));
        periods.addLast(new Period("2026-07-17", "EVENING", "NOT_ACTIVATED"));

        SimulationService simulationService = new SimulationService(periods);
        bookingService.setAutoSave(false);

        customerService.getCustomerList().addLast(new Customer("C001", "Nguyen Van A", "0900000001", "MEMBER", 0, 0.0, 0));
        vehicleService.getVehicleList().addLast(new Vehicle("V001", "51A-11111", "C001"));
        washService.getServiceList().addLast(new WashPackage("S001", "Basic Wash", 100000, 30, "ACTIVE"));
        washService.getServiceList().addLast(new WashPackage("S002", "Inactive Wash", 120000, 30, "INACTIVE"));

        boolean invalidCustomer = bookingService.createBooking(
                "C999",
                "V001",
                "S001",
                "2026-07-17",
                "MORNING",
                customerService,
                vehicleService,
                washService,
                simulationService
        );

        if (invalidCustomer) {
            throw new RuntimeException("Expected booking with invalid customer to fail");
        }

        boolean inactiveService = bookingService.createBooking(
                "C001",
                "V001",
                "S002",
                "2026-07-17",
                "MORNING",
                customerService,
                vehicleService,
                washService,
                simulationService
        );

        if (inactiveService) {
            throw new RuntimeException("Expected booking with inactive service to fail");
        }

        boolean created = bookingService.createBooking(
                "C001",
                "V001",
                "S001",
                "2026-07-17",
                "MORNING",
                customerService,
                vehicleService,
                washService,
                simulationService
        );

        if (!created) {
            throw new RuntimeException("Expected booking creation to succeed");
        }

        if (bookingService.getBookingList().size() != 1) {
            throw new RuntimeException("Expected exactly one booking after valid create");
        }

        Booking booking = bookingService.getBookingList().get(0);
        if (!"B001".equalsIgnoreCase(booking.getBookingId())) {
            throw new RuntimeException("Expected generated booking ID B001");
        }

        if (bookingService.getBookingQueue().size() != 1) {
            throw new RuntimeException("Expected booking to enter main queue");
        }

        bookingService.processNextBooking();
        if (!"SERVING".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new RuntimeException("Expected booking to become SERVING");
        }

        System.out.println("BookingServiceFlowTest passed");
    }
}
