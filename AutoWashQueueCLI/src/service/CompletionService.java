package service;

import datastructure.MyLinkedList;
import model.Booking;
import model.CompletionResult;
import model.Customer;
import model.History;
import model.Vehicle;
import model.WaitlistEntry;
import model.WashPackage;
import util.FileManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Implements FR-17 and FR-19 as one atomic business operation. */
public class CompletionService {
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final WashServiceManager washServiceManager;
    private final VehicleService vehicleService;
    private final MyLinkedList<History> historyList;

    public CompletionService(BookingService bookingService, CustomerService customerService,
            WashServiceManager washServiceManager, VehicleService vehicleService,
            MyLinkedList<History> historyList) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.washServiceManager = washServiceManager;
        this.vehicleService = vehicleService;
        this.historyList = historyList;
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
        String previousTier = customer.getMembershipLevel();

        booking.setBookingStatus("COMPLETED");
        addHistoryIfMissing(booking, customer, vehicle, packageToComplete);
        recalculateLoyalty(customer);
        Booking promotedBooking = promoteTopWaitlistBooking(booking.getDate(), booking.getPeriod());
        bookingService.recordCompletion(booking, promotedBooking);

        FileManager.saveBookings(bookingService.getBookingList());
        FileManager.saveHistories(historyList);
        FileManager.saveCustomers(customerService.getCustomerList());

        return new CompletionResult(true, "Booking completed successfully.", booking,
                promotedBooking, customer, previousPoints, previousTier);
    }

    public void recalculateLoyalty(Customer customer) {
        int visitCount = 0;
        double totalSpent = 0.0;
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            if (customer.getId().equalsIgnoreCase(booking.getCustomerId())
                    && "COMPLETED".equalsIgnoreCase(booking.getBookingStatus())) {
                WashPackage washPackage = washServiceManager.findServiceById(booking.getServiceId());
                if (washPackage != null) {
                    visitCount++;
                    totalSpent += washPackage.getPrice();
                }
            }
        }
        customer.setVisitCount(visitCount);
        customer.setTotalSpent(totalSpent);
        customer.setPoints((int) (totalSpent / 1000));
        customer.setMembershipLevel(determineTier(visitCount, totalSpent));
    }

    private Booking promoteTopWaitlistBooking(String date, String period) {
        WaitlistEntry topEntry = bookingService.peekHighestPriorityWaitlist(date, period);
        Booking topBooking = topEntry == null ? null : topEntry.getBooking();
        if (topBooking == null || getRemainingMinutes(date, period) < getDuration(topBooking)) {
            return null;
        }
        Booking promotedBooking = bookingService.pollHighestPriorityWaitlist(date, period);
        bookingService.getBookingQueue().enqueue(promotedBooking);
        return promotedBooking;
    }

    private int getRemainingMinutes(String date, String period) {
        int usedMinutes = 0;
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && "COMPLETED".equalsIgnoreCase(booking.getBookingStatus())) {
                usedMinutes += getDuration(booking);
            }
        }
        return getPeriodDuration(period) - usedMinutes;
    }

    private int getDuration(Booking booking) {
        WashPackage washPackage = washServiceManager.findServiceById(booking.getServiceId());
        return washPackage == null ? 0 : washPackage.getDuration();
    }

    private int getPeriodDuration(String period) {
        if ("MORNING".equalsIgnoreCase(period)) return 300;
        if ("AFTERNOON".equalsIgnoreCase(period)) return 240;
        if ("EVENING".equalsIgnoreCase(period)) return 180;
        return 0;
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
        String completedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    private CompletionResult failed(String message) {
        return new CompletionResult(false, message, null, null, null, 0, null);
    }
}
