package model;

import datastructure.MyLinkedList;

/** Result returned after attempting to complete a booking. */
public class CompletionResult {
    private final boolean successful;
    private final String message;
    private final Booking completedBooking;
    private final MyLinkedList<Booking> promotedBookings;
    private final Customer customer;
    private final int previousPoints;
    private final String previousTier;

    public CompletionResult(boolean successful, String message, Booking completedBooking,
            Booking promotedBooking, Customer customer, int previousPoints, String previousTier) {
        this(successful, message, completedBooking, singleBookingList(promotedBooking),
                customer, previousPoints, previousTier);
    }

    public CompletionResult(boolean successful, String message, Booking completedBooking,
            MyLinkedList<Booking> promotedBookings, Customer customer, int previousPoints, String previousTier) {
        this.successful = successful;
        this.message = message;
        this.completedBooking = completedBooking;
        this.promotedBookings = promotedBookings == null ? new MyLinkedList<Booking>() : promotedBookings;
        this.customer = customer;
        this.previousPoints = previousPoints;
        this.previousTier = previousTier;
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getCompletedBooking() { return completedBooking; }
    public Booking getPromotedBooking() {
        return promotedBookings.isEmpty() ? null : promotedBookings.getFirst();
    }
    public MyLinkedList<Booking> getPromotedBookings() { return promotedBookings; }
    public Customer getCustomer() { return customer; }
    public int getPreviousPoints() { return previousPoints; }
    public String getPreviousTier() { return previousTier; }

    private static MyLinkedList<Booking> singleBookingList(Booking booking) {
        MyLinkedList<Booking> bookings = new MyLinkedList<>();
        if (booking != null) bookings.addLast(booking);
        return bookings;
    }
}
