package service;

import model.Booking;
import model.CancellationResult;
import model.WaitlistEntry;
import model.WashPackage;
import util.FileManager;

/** Implements FR-18: booking cancellation and waitlist promotion. */
public class CancellationService {
    private final BookingService bookingService;
    private final WashServiceManager washServiceManager;

    public CancellationService(BookingService bookingService, WashServiceManager washServiceManager) {
        this.bookingService = bookingService;
        this.washServiceManager = washServiceManager;
    }

    public CancellationResult cancelAsCustomer(String customerId, String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null) return failed("Booking not found.");
        if (!booking.getCustomerId().equalsIgnoreCase(customerId)) {
            return failed("Customers can only cancel their own bookings.");
        }
        if (!"WAITING".equalsIgnoreCase(booking.getBookingStatus())) {
            return failed("Customers can only cancel bookings with WAITING status.");
        }
        return cancel(booking, false);
    }

    public CancellationResult cancelAsAdmin(String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null) return failed("Booking not found.");
        if (!"WAITING".equalsIgnoreCase(booking.getBookingStatus())
                && !"SERVING".equalsIgnoreCase(booking.getBookingStatus())) {
            return failed("Only WAITING or SERVING bookings can be cancelled.");
        }
        return cancel(booking, true);
    }

    private CancellationResult cancel(Booking booking, boolean isAdmin) {
        boolean wasServing = "SERVING".equalsIgnoreCase(booking.getBookingStatus());
        boolean wasInMainQueue = bookingService.getBookingQueue().removeBookingById(booking.getBookingId());
        boolean wasInWaitlist = bookingService.removeFromWaitlist(booking.getBookingId());

        booking.setBookingStatus("CANCELLED");
        Booking promotedBooking = null;
        if (wasServing && isAdmin) {
            promotedBooking = promoteTopWaitlistBooking(booking.getDate(), booking.getPeriod(), true);
        } else if (wasInMainQueue) {
            promotedBooking = promoteTopWaitlistBooking(booking.getDate(), booking.getPeriod(), false);
        }

        FileManager.saveBookings(bookingService.getBookingList());
        String location = wasServing ? "service position"
                : (wasInWaitlist ? "waitlist" : (wasInMainQueue ? "main queue" : "future booking list"));
        String message = "Booking " + booking.getBookingId() + " was cancelled from the " + location + ".";
        if (wasServing) {
            message += " The service position was released.";
        }
        return new CancellationResult(true, message, booking, promotedBooking);
    }

    private Booking promoteTopWaitlistBooking(String date, String period, boolean checkRemainingTime) {
        WaitlistEntry topEntry = bookingService.peekHighestPriorityWaitlist(date, period);
        Booking topBooking = topEntry == null ? null : topEntry.getBooking();
        if (topBooking == null) return null;
        if (checkRemainingTime && getRemainingMinutes(date, period) < getDuration(topBooking)) {
            return null;
        }
        Booking promotedBooking = bookingService.pollHighestPriorityWaitlist(date, period);
        bookingService.getBookingQueue().enqueue(promotedBooking);
        return promotedBooking;
    }

    private int getRemainingMinutes(String date, String period) {
        int usedMinutes = 0;
        datastructure.MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && occupiesServiceTime(booking)) {
                usedMinutes += getDuration(booking);
            }
        }
        return getPeriodDuration(period) - usedMinutes;
    }

    private boolean occupiesServiceTime(Booking booking) {
        String status = booking.getBookingStatus();
        return "COMPLETED".equalsIgnoreCase(status)
                || "SERVING".equalsIgnoreCase(status)
                || ("WAITING".equalsIgnoreCase(status)
                && bookingService.isInMainQueue(booking.getBookingId()));
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

    private Booking findBooking(String bookingId) {
        if (bookingId == null) return null;
        datastructure.MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookingId.equalsIgnoreCase(bookings.get(i).getBookingId())) return bookings.get(i);
        }
        return null;
    }

    private CancellationResult failed(String message) {
        return new CancellationResult(false, message, null, null);
    }
}
