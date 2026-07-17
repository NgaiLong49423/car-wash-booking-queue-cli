package model;

/** A booking together with its immutable waitlist priority. */
public class WaitlistEntry implements Comparable<WaitlistEntry> {
    private final Booking booking;
    private final int tierPriority;

    public WaitlistEntry(Booking booking, int tierPriority) {
        this.booking = booking;
        this.tierPriority = tierPriority;
    }

    public Booking getBooking() {
        return booking;
    }

    @Override
    public int compareTo(WaitlistEntry other) {
        if (tierPriority != other.tierPriority) {
            return tierPriority - other.tierPriority;
        }
        return Long.compare(other.booking.getCreatedTime(), booking.getCreatedTime());
    }
}
