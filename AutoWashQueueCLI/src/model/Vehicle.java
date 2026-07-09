package model;

public class Vehicle {
    private String id;
    private String licensePlate;
    private String customerId;

    public Vehicle(String id, String licensePlate, String customerId) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.customerId = customerId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    @Override
    public String toString() {
        return id + "|" + licensePlate + "|" + customerId;
    }
}