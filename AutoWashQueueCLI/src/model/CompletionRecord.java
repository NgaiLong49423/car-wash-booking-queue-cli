package model;

/** Data required by the later Undo feature to reverse a completion operation. */
public class CompletionRecord {
    private final Booking completedBooking;
    private final Booking promotedBooking;

    public CompletionRecord(Booking completedBooking, Booking promotedBooking) {
        this.completedBooking = completedBooking;
        this.promotedBooking = promotedBooking;
    }

    public Booking getCompletedBooking() { return completedBooking; }
    public Booking getPromotedBooking() { return promotedBooking; }
}
