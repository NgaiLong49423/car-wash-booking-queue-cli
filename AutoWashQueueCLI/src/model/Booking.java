package model;

public class Booking {
    private String bookingId;
    private String customerId;
    private String vehicleId;
    private String serviceId;
    private String date;
    private String period;
    private String bookingStatus;
    private String paymentStatus;
    private String paymentMethod;
    private long createdTime;

    public Booking(String bookingId, String customerId, String vehicleId, String serviceId, String date, String period,
                   String bookingStatus, String paymentStatus, String paymentMethod, long createdTime) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.serviceId = serviceId;
        this.date = date;
        this.period = period;
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.createdTime = createdTime;
    }

    // Deprecated/Legacy constructor for compatibility with existing BookingService if needed, but we will update BookingService too.
    // However, to avoid compile errors before other files are updated, we can also provide a 4-arg constructor:
    public Booking(String bookingId, String vehicleId, String serviceId, String bookingStatus) {
        this(bookingId, "C000", vehicleId, serviceId, "2026-07-10", "MORNING", bookingStatus, "UNPAID", "NONE", System.currentTimeMillis());
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }

    // Legacy getter used in existing code
    public String getLicensePlate() { return vehicleId; }
    public void setLicensePlate(String licensePlate) { this.vehicleId = licensePlate; }
    public String getStatus() { return bookingStatus; }
    public void setStatus(String status) { this.bookingStatus = status; }

    @Override
    public String toString() {
        return bookingId + "|" + customerId + "|" + vehicleId + "|" + serviceId + "|" + date + "|" + period + "|" + 
               bookingStatus + "|" + paymentStatus + "|" + paymentMethod + "|" + createdTime;
    }
}
