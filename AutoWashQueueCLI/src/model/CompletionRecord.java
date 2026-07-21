package model;

import datastructure.MyLinkedList;

/** Data required by the later Undo feature to reverse a completion operation. */
public class CompletionRecord {
    private final Booking completedBooking;
    private final MyLinkedList<Booking> promotedBookings;

    public CompletionRecord(Booking completedBooking, Booking promotedBooking) {
        this(completedBooking, singleBookingList(promotedBooking));
    }

    public CompletionRecord(Booking completedBooking, MyLinkedList<Booking> promotedBookings) {
        this.completedBooking = completedBooking;
        this.promotedBookings = promotedBookings == null ? new MyLinkedList<Booking>() : promotedBookings;
    }

    public Booking getCompletedBooking() { return completedBooking; }
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
