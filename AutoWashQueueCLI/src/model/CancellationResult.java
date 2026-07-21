package model;

import datastructure.MyLinkedList;

/** Result returned after attempting to cancel a booking. */
public class CancellationResult {
    private final boolean successful;
    private final String message;
    private final Booking cancelledBooking;
    private final MyLinkedList<Booking> promotedBookings;

    public CancellationResult(boolean successful, String message,
            Booking cancelledBooking, Booking promotedBooking) {
        this(successful, message, cancelledBooking, singleBookingList(promotedBooking));
    }

    public CancellationResult(boolean successful, String message,
            Booking cancelledBooking, MyLinkedList<Booking> promotedBookings) {
        this.successful = successful;
        this.message = message;
        this.cancelledBooking = cancelledBooking;
        this.promotedBookings = promotedBookings == null
                ? new MyLinkedList<Booking>() : promotedBookings;
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getCancelledBooking() { return cancelledBooking; }
    public Booking getPromotedBooking() {
        return promotedBookings.isEmpty() ? null : promotedBookings.getFirst();
    }
    public MyLinkedList<Booking> getPromotedBookings() { return promotedBookings; }

    private static MyLinkedList<Booking> singleBookingList(Booking booking) {
        MyLinkedList<Booking> bookings = new MyLinkedList<>();
        if (booking != null) bookings.addLast(booking);
        return bookings;
    }
}
