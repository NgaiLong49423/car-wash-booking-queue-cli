package service;

import datastructure.MyLinkedList;
import model.Vehicle;

public class VehicleService {
    private MyLinkedList<Vehicle> vehicleList;

    public VehicleService() {
        this.vehicleList = new MyLinkedList<>();
    }

    public void displayAllVehicles() {
        System.out.println("\n--- VEHICLE LIST ---");
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles found in the system!");
            return;
        }
        vehicleList.display();
        System.out.println("--------------------");
    }

    public void addVehicle(String licensePlate, String customerId, String type) {
        if (findVehicleByLicense(licensePlate) != null) {
            System.out.println("=> Error: License plate " + licensePlate + " already exists!");
            return;
        }
        Vehicle newVehicle = new Vehicle(licensePlate, customerId, type);
        vehicleList.addLast(newVehicle);
        System.out.println("=> Added vehicle successfully: " + licensePlate);
    }

    public Vehicle findVehicleByLicense(String licensePlate) {
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return v;
            }
        }
        return null;
    }

    public void updateVehicle(String licensePlate, String newCustomerId, String newType) {
        Vehicle v = findVehicleByLicense(licensePlate);
        if (v != null) {
            v.setCustomerId(newCustomerId);
            v.setVehicleType(newType);
            System.out.println("=> Updated vehicle successfully: " + licensePlate);
        } else {
            System.out.println("=> Error: Vehicle not found with license plate: " + licensePlate);
        }
    }

    public void deleteVehicle(String licensePlate) {
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                vehicleList.remove(i);
                System.out.println("=> Deleted vehicle successfully: " + licensePlate);
                return;
            }
        }
        System.out.println("=> Error: Vehicle not found with license plate: " + licensePlate);
    }

    public MyLinkedList<Vehicle> getVehicleList() {
        return vehicleList;
    }
}