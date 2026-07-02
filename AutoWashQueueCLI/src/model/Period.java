package model;

public class Period {
    private String periodId;
    private String periodName; // Ví dụ: Sáng, Chiều, Tối
    private boolean isActive;  // Trạng thái kích hoạt của buổi đó

    public Period(String periodId, String periodName, boolean isActive) {
        this.periodId = periodId;
        this.periodName = periodName;
        this.isActive = isActive;
    }

    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }

    public String getPeriodName() { return periodName; }
    public void setPeriodName(String periodName) { this.periodName = periodName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        // Sử dụng dấu | để chuẩn hóa việc ghi file theo yêu cầu của Leader
        return periodId + "|" + periodName + "|" + isActive;
    }
}