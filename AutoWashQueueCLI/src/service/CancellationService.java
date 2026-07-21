package service;

import datastructure.MyLinkedList;
import model.Booking;
import model.CancellationResult;
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
        MyLinkedList<Booking> promotedBookings = new MyLinkedList<Booking>();
        if (wasServing && isAdmin) {
            promotedBookings = bookingService.promoteFittingWaitlistBookings(
                    booking.getDate(), booking.getPeriod(), washServiceManager);
        } else if (wasInMainQueue) {
            promotedBookings = bookingService.promoteFittingWaitlistBookings(
                    booking.getDate(), booking.getPeriod(), washServiceManager);
        }

        FileManager.saveBookings(bookingService.getBookingList());
        String location = wasServing ? "service position"
                : (wasInWaitlist ? "waitlist" : (wasInMainQueue ? "main queue" : "future booking list"));
        String message = "Booking " + booking.getBookingId() + " was cancelled from the " + location + ".";
        if (wasServing) {
            message += " The service position was released.";
        }
        return new CancellationResult(true, message, booking, promotedBookings);
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
        return new CancellationResult(false, message, null, new MyLinkedList<Booking>());
    }
}
