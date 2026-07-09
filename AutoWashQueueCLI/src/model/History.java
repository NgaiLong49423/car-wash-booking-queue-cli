package model;

public class History {
    private String bookingId;
    private String customerId;
    private String customerName;
    private String plateNumber;
    private String serviceName;
    private String completedTime;
    private double amountPaid;
    private int loyaltyPointsEarned;

    public History(String bookingId, String customerId, String customerName, String plateNumber,
                   String serviceName, String completedTime, double amountPaid, int loyaltyPointsEarned) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.plateNumber = plateNumber;
        this.serviceName = serviceName;
        this.completedTime = completedTime;
        this.amountPaid = amountPaid;
        this.loyaltyPointsEarned = loyaltyPointsEarned;
    }

    // Legacy constructor for compatibility
    public History(String historyId, String bookingId, String licensePlate, String serviceId, String completionDate) {
        this(bookingId, "C000", "Unknown Customer", licensePlate, "Unknown Service", completionDate, 0.0, 0);
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getCompletedTime() { return completedTime; }
    public void setCompletedTime(String completedTime) { this.completedTime = completedTime; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public int getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(int loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }

    // Legacy getters for compatibility
    public String getHistoryId() { return bookingId; }
    public String getLicensePlate() { return plateNumber; }
    public String getServiceId() { return serviceName; }
    public String getCompletionDate() { return completedTime; }

    @Override
    public String toString() {
        return bookingId + "|" + customerId + "|" + customerName + "|" + plateNumber + "|" + 
               serviceName + "|" + completedTime + "|" + amountPaid + "|" + loyaltyPointsEarned;
    }
}