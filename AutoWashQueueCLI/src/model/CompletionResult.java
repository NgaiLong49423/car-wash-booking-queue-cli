package model;

/** Result returned after attempting to complete a booking. */
public class CompletionResult {
    private final boolean successful;
    private final String message;
    private final Booking completedBooking;
    private final Booking promotedBooking;
    private final Customer customer;
    private final int previousPoints;
    private final String previousTier;

    public CompletionResult(boolean successful, String message, Booking completedBooking,
            Booking promotedBooking, Customer customer, int previousPoints, String previousTier) {
        this.successful = successful;
        this.message = message;
        this.completedBooking = completedBooking;
        this.promotedBooking = promotedBooking;
        this.customer = customer;
        this.previousPoints = previousPoints;
        this.previousTier = previousTier;
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getCompletedBooking() { return completedBooking; }
    public Booking getPromotedBooking() { return promotedBooking; }
    public Customer getCustomer() { return customer; }
    public int getPreviousPoints() { return previousPoints; }
    public String getPreviousTier() { return previousTier; }
}
