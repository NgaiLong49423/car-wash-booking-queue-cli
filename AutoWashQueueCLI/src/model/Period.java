package model;

public class Period {
    private String date;
    private String periodName;
    private String status;

    public Period(String date, String periodName, String status) {
        this.date = date;
        this.periodName = periodName;
        this.status = status;
    }

    // Legacy constructor for compatibility
    public Period(String periodId, String periodName, boolean isActive) {
        this("2026-07-10", periodName, isActive ? "ACTIVATED" : "NOT_ACTIVATED");
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPeriodName() { return periodName; }
    public void setPeriodName(String periodName) { this.periodName = periodName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Legacy getters for compatibility
    public String getPeriodId() { return periodName; }
    public boolean isActive() { return "ACTIVATED".equalsIgnoreCase(status); }
    public void setActive(boolean active) { this.status = active ? "ACTIVATED" : "NOT_ACTIVATED"; }

    @Override
    public String toString() {
        return date + "|" + periodName + "|" + status;
    }
}