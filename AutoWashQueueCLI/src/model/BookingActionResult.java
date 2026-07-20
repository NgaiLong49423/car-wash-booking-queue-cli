package model;

/** Result of starting service or confirming payment for a booking. */
public class BookingActionResult {
    private final boolean successful;
    private final String message;
    private final Booking booking;
    private final double amount;

    public BookingActionResult(boolean successful, String message, Booking booking, double amount) {
        this.successful = successful;
        this.message = message;
        this.booking = booking;
        this.amount = amount;
    }

    public static BookingActionResult failure(String message) {
        return new BookingActionResult(false, message, null, 0.0);
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
}
