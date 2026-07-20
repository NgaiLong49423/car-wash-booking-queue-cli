package model;

/** Result returned after attempting to activate one service period. */
public class PeriodActivationResult {
    private final boolean successful;
    private final String message;
    private final int mainQueueCount;
    private final int waitlistCount;
    private final int overflowCount;

    public PeriodActivationResult(boolean successful, String message,
            int mainQueueCount, int waitlistCount, int overflowCount) {
        this.successful = successful;
        this.message = message;
        this.mainQueueCount = mainQueueCount;
        this.waitlistCount = waitlistCount;
        this.overflowCount = overflowCount;
    }

    public static PeriodActivationResult failure(String message) {
        return new PeriodActivationResult(false, message, 0, 0, 0);
    }

    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
    public int getMainQueueCount() { return mainQueueCount; }
    public int getWaitlistCount() { return waitlistCount; }
    public int getOverflowCount() { return overflowCount; }
}
