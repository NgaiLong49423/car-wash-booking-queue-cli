package model;

/** Result returned after undoing the latest completion operation. */
public class UndoResult {
    private final boolean successful;
    private final String message;
    private final Booking restoredBooking;
    private final Booking returnedToWaitlist;

    public UndoResult(boolean successful, String message,
            Booking restoredBooking, Booking returnedToWaitlist) {
        this.successful = successful;
        this.message = message;
        this.restoredBooking = restoredBooking;
        this.returnedToWaitlist = returnedToWaitlist;
    }

    public static UndoResult failure(String message) {
        return new UndoResult(false, message, null, null);
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public Booking getRestoredBooking() { return restoredBooking; }
    public Booking getReturnedToWaitlist() { return returnedToWaitlist; }
}
