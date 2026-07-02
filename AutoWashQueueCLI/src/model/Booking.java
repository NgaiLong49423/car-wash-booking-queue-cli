package model;

public class Booking {
    private String bookingId;
    private String licensePlate;
    private String serviceId;
    private String status;

    public Booking(String bookingId, String licensePlate, String serviceId, String status) {
        this.bookingId = bookingId;
        this.licensePlate = licensePlate;
        this.serviceId = serviceId;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return bookingId + "|" + licensePlate + "|" + serviceId + "|" + status;
    }
}