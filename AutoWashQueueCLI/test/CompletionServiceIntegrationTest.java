import datastructure.MyLinkedList;
import model.Booking;
import model.CompletionResult;
import model.Customer;
import model.History;
import model.Vehicle;
import model.WashPackage;
import service.BookingService;
import service.CompletionService;
import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;

public class CompletionServiceIntegrationTest {
    public static void main(String[] args) {
        rejectsUnpaidBooking();
        completesBookingRecalculatesLoyaltyAndPromotesTopWaitlist();
        doesNotSkipTopWaitlistBookingWhenTimeIsInsufficient();
        System.out.println("CompletionServiceIntegrationTest: PASS");
    }

    private static void rejectsUnpaidBooking() {
        TestContext context = new TestContext();
        Booking booking = context.addBooking("B001", "C001", "V001", "S001", "SERVING", "UNPAID");
        CompletionResult result = context.completionService.completeBooking(booking.getBookingId());
        assertFalse(result.isSuccessful(), "An unpaid booking must not be completed");
        assertEquals("SERVING", booking.getBookingStatus(), "Rejected booking status must stay SERVING");
        assertEquals(0, context.histories.size(), "Rejected booking must not create history");
    }

    private static void completesBookingRecalculatesLoyaltyAndPromotesTopWaitlist() {
        TestContext context = new TestContext();
        Booking serving = context.addBooking("B001", "C001", "V001", "S001", "SERVING", "PAID");
        Booking topWaitlist = context.addBooking("B002", "C002", "V002", "S002", "WAITING", "UNPAID");
        context.bookingService.addToWaitlist(topWaitlist);

        CompletionResult result = context.completionService.completeBooking(serving.getBookingId());
        Customer customer = context.customerService.findCustomerById("C001");

        assertTrue(result.isSuccessful(), "A paid SERVING booking must complete");
        assertEquals("COMPLETED", serving.getBookingStatus(), "Completed status must be stored");
        assertEquals(1, context.histories.size(), "Completion must add exactly one history record");
        assertEquals(2000, customer.getPoints(), "Loyalty must be recalculated from completed spend");
        assertEquals("SILVER", customer.getMembershipLevel(), "Tier must be upgraded by spend threshold");
        assertEquals(0, context.bookingService.getWaitlist().size(), "Promoted booking must leave waitlist");
        assertEquals("B002", context.bookingService.getBookingQueue().peek().getBookingId(), "Highest priority waitlist booking must be promoted");
    }

    private static void doesNotSkipTopWaitlistBookingWhenTimeIsInsufficient() {
        TestContext context = new TestContext();
        context.addBooking("B000", "C004", "V004", "S003", "COMPLETED", "PAID"); // 220 minutes already used
        Booking serving = context.addBooking("B001", "C001", "V001", "S001", "SERVING", "PAID"); // 20 minutes
        Booking goldShort = context.addBooking("B002", "C002", "V002", "S004", "WAITING", "UNPAID"); // 50 minutes
        Booking platinumLong = context.addBooking("B003", "C003", "V003", "S005", "WAITING", "UNPAID"); // 90 minutes
        context.bookingService.addToWaitlist(goldShort);
        context.bookingService.addToWaitlist(platinumLong);

        CompletionResult result = context.completionService.completeBooking(serving.getBookingId());

        assertTrue(result.isSuccessful(), "Completion itself must still succeed");
        assertEquals(null, result.getPromotedBooking(), "No booking may be promoted when the top priority booking does not fit");
        assertEquals(2, context.bookingService.getWaitlist().size(), "Waitlist must remain untouched; shorter lower-priority booking cannot be skipped to");
        assertTrue(context.bookingService.getBookingQueue().isEmpty(), "Main queue must remain empty");
    }

    private static class TestContext {
        private final BookingService bookingService = new BookingService();
        private final CustomerService customerService = new CustomerService();
        private final WashServiceManager washServiceManager = new WashServiceManager();
        private final VehicleService vehicleService = new VehicleService();
        private final MyLinkedList<History> histories = new MyLinkedList<>();
        private final CompletionService completionService;

        private TestContext() {
            customerService.getCustomerList().addLast(new Customer("C001", "Member", "0900000001", "MEMBER", 0, 0, 0));
            customerService.getCustomerList().addLast(new Customer("C002", "Gold", "0900000002", "GOLD", 0, 0, 0));
            customerService.getCustomerList().addLast(new Customer("C003", "Platinum", "0900000003", "PLATINUM", 0, 0, 0));
            customerService.getCustomerList().addLast(new Customer("C004", "Existing", "0900000004", "MEMBER", 0, 0, 0));
            vehicleService.getVehicleList().addLast(new Vehicle("V001", "30A-00001", "C001"));
            vehicleService.getVehicleList().addLast(new Vehicle("V002", "30A-00002", "C002"));
            vehicleService.getVehicleList().addLast(new Vehicle("V003", "30A-00003", "C003"));
            vehicleService.getVehicleList().addLast(new Vehicle("V004", "30A-00004", "C004"));
            washServiceManager.getServiceList().addLast(new WashPackage("S001", "Completion", 2000000, 20, "ACTIVE"));
            washServiceManager.getServiceList().addLast(new WashPackage("S002", "Waitlist", 100000, 60, "ACTIVE"));
            washServiceManager.getServiceList().addLast(new WashPackage("S003", "Existing", 100000, 220, "ACTIVE"));
            washServiceManager.getServiceList().addLast(new WashPackage("S004", "Short", 100000, 50, "ACTIVE"));
            washServiceManager.getServiceList().addLast(new WashPackage("S005", "Long", 100000, 90, "ACTIVE"));
            completionService = new CompletionService(bookingService, customerService, washServiceManager, vehicleService, histories);
        }

        private Booking addBooking(String id, String customerId, String vehicleId, String serviceId, String status, String paymentStatus) {
            Booking booking = new Booking(id, customerId, vehicleId, serviceId, "2026-07-10", "MORNING", status, paymentStatus, "CASH", Long.parseLong(id.substring(1)));
            bookingService.getBookingList().addLast(booking);
            return booking;
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (expected=" + expected + ", actual=" + actual + ")");
        }
    }
}
