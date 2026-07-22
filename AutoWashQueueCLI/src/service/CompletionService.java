package service;

import datastructure.MyLinkedList;
import model.Booking;
import model.CompletionResult;
import model.Customer;
import model.History;
import model.Vehicle;
import model.WashPackage;
import util.FileManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/** Implements FR-17 and FR-19 as one atomic business operation. */
public class CompletionService {
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final WashServiceManager washServiceManager;
    private final VehicleService vehicleService;
    private final MyLinkedList<History> historyList;
    private final SimulationService simulationService;

    public CompletionService(BookingService bookingService, CustomerService customerService,
            WashServiceManager washServiceManager, VehicleService vehicleService,
            MyLinkedList<History> historyList, SimulationService simulationService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.washServiceManager = washServiceManager;
        this.vehicleService = vehicleService;
        this.historyList = historyList;
        this.simulationService = simulationService;
    }

    public CompletionResult completeBooking(String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null) {
            return failed("Booking not found.");
        }
        if (!"SERVING".equalsIgnoreCase(booking.getBookingStatus())) {
            return failed("Only a SERVING booking can be completed.");
        }
        if (!"PAID".equalsIgnoreCase(booking.getPaymentStatus())) {
            return failed("Payment must be confirmed before completing this booking.");
        }

        Customer customer = customerService.findCustomerById(booking.getCustomerId());
        WashPackage packageToComplete = washServiceManager.findServiceById(booking.getServiceId());
        Vehicle vehicle = findVehicleById(booking.getVehicleId());
        if (customer == null || packageToComplete == null || vehicle == null) {
            return failed("Booking references missing customer, vehicle, or service data.");
        }

        int previousPoints = customer.getPoints();
        int previousVisitCount = customer.getVisitCount();
        String previousTier = customer.getMembershipLevel();

        booking.setBookingStatus("COMPLETED");
        addHistoryIfMissing(booking, customer, vehicle, packageToComplete);
        recalculateLoyalty(customer);
        MyLinkedList<Booking> promotedBookings = bookingService.promoteFittingWaitlistBookings(
                booking.getDate(), booking.getPeriod(), washServiceManager);
        bookingService.recordCompletion(booking, promotedBookings);

        FileManager.saveBookings(bookingService.getBookingList());
        FileManager.saveHistories(historyList);
        FileManager.saveCustomers(customerService.getCustomerList());

        return new CompletionResult(true, "Booking completed successfully.", booking,
                promotedBookings, customer, previousPoints, previousVisitCount, previousTier);
    }

    public void recalculateLoyalty(Customer customer) {
        LocalDate currentDate = parseDate(simulationService.getCurrentDateStr());
        if (customer == null || currentDate == null) {
            return;
        }
        int visitCount = 0;
        double totalSpent = 0.0;
        for (int i = 0; i < historyList.size(); i++) {
            History history = historyList.get(i);
            LocalDate completedDate = parseHistoryDate(history.getCompletedTime());
            if (customer.getId().equalsIgnoreCase(history.getCustomerId())
                    && isWithinLoyaltyWindow(completedDate, currentDate)) {
                visitCount++;
                totalSpent += history.getAmountPaid();
            }
        }
        customer.setVisitCount(visitCount);
        customer.setTotalSpent(totalSpent);
        customer.setPoints((int) (totalSpent / 1000));
        customer.setMembershipLevel(determineTier(visitCount, totalSpent));
    }

    public void recalculateAllLoyalty() {
        for (int i = 0; i < customerService.getCustomerList().size(); i++) {
            recalculateLoyalty(customerService.getCustomerList().get(i));
        }
    }

    private boolean isWithinLoyaltyWindow(LocalDate completedDate, LocalDate currentDate) {
        if (completedDate == null || currentDate == null || completedDate.isAfter(currentDate)) {
            return false;
        }
        return ChronoUnit.DAYS.between(completedDate, currentDate) <= 365;
    }

    private String determineTier(int visitCount, double totalSpent) {
        if (visitCount >= 30 || totalSpent >= 15000000) return "PLATINUM";
        if (visitCount >= 15 || totalSpent >= 6000000) return "GOLD";
        if (visitCount >= 5 || totalSpent >= 2000000) return "SILVER";
        return "MEMBER";
    }

    private void addHistoryIfMissing(Booking booking, Customer customer, Vehicle vehicle, WashPackage washPackage) {
        for (int i = 0; i < historyList.size(); i++) {
            if (booking.getBookingId().equalsIgnoreCase(historyList.get(i).getBookingId())) return;
        }
        String completedTime = simulationService.getCurrentDateStr() + " "
                + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        historyList.addLast(new History(booking.getBookingId(), customer.getId(), customer.getName(),
                vehicle.getLicensePlate(), washPackage.getName(), completedTime, washPackage.getPrice(),
                (int) (washPackage.getPrice() / 1000)));
    }

    private Booking findBooking(String bookingId) {
        if (bookingId == null) return null;
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookingId.equalsIgnoreCase(bookings.get(i).getBookingId())) return bookings.get(i);
        }
        return null;
    }

    private Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null) return null;
        for (int i = 0; i < vehicleService.getVehicleList().size(); i++) {
            Vehicle vehicle = vehicleService.getVehicleList().get(i);
            if (vehicleId.equalsIgnoreCase(vehicle.getId())) return vehicle;
        }
        return null;
    }

    private LocalDate parseHistoryDate(String completedTime) {
        if (completedTime == null || completedTime.trim().length() < 10) return null;
        return parseDate(completedTime.trim().substring(0, 10));
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private CompletionResult failed(String message) {
        return new CompletionResult(false, message, null, new MyLinkedList<Booking>(), null, 0, 0, null);
    }
}
