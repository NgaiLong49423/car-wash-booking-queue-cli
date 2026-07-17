package model;

/** Result returned after attempting to cancel a booking. */
public class CancellationResult {
    private final boolean successful;
    private final String message;
    private final Booking cancelledBooking;
    private final Booking promotedBooking;

    public CancellationResult(boolean successful, String message,
            Booking cancelledBooking, Booking promotedBooking) {
        this.successful = successful;
        this.message = message;
        this.cancelledBooking = cancelledBooking;
        this.promotedBooking = promotedBooking;
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getCancelledBooking() { return cancelledBooking; }
    public Booking getPromotedBooking() { return promotedBooking; }
}
