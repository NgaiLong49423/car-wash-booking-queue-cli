package model;

public class Vehicle {
    private String licensePlate;
    private String customerId;
    private String vehicleType;

    public Vehicle(String licensePlate, String customerId, String vehicleType) {
        this.licensePlate = licensePlate;
        this.customerId = customerId;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    @Override
    public String toString() {
        return "Xe [" + licensePlate + " | Chu xe (Ma KH): " + customerId + " | Loai: " + vehicleType + "]";
    }
}