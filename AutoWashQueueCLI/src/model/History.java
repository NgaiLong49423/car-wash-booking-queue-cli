package model;

public class History {
    private String historyId;
    private String bookingId;
    private String licensePlate;
    private String serviceId;
    private String completionDate; // Ngày/Giờ hoàn tất dịch vụ

    public History(String historyId, String bookingId, String licensePlate, String serviceId, String completionDate) {
        this.historyId = historyId;
        this.bookingId = bookingId;
        this.licensePlate = licensePlate;
        this.serviceId = serviceId;
        this.completionDate = completionDate;
    }

    public String getHistoryId() { return historyId; }
    public void setHistoryId(String historyId) { this.historyId = historyId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getCompletionDate() { return completionDate; }
    public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }

    @Override
    public String toString() {
        // Chuẩn hóa phân cách bằng dấu |
        return historyId + "|" + bookingId + "|" + licensePlate + "|" + serviceId + "|" + completionDate;
    }
}