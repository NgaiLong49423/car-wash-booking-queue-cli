package service;

import datastructure.MyLinkedList;
import model.Vehicle;
import util.FileManager;

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

        // Generate unique Vehicle ID
        int size = vehicleList.size();
        int maxIdNum = 0;
        for (int i = 0; i < size; i++) {
            String currentId = vehicleList.get(i).getId();
            if (currentId != null && currentId.startsWith("V")) {
                try {
                    int num = Integer.parseInt(currentId.substring(1));
                    if (num > maxIdNum) {
                        maxIdNum = num;
                    }
                } catch (Exception e) {}
            }
        }
        String newId = String.format("V%03d", maxIdNum + 1);

        Vehicle newVehicle = new Vehicle(newId, licensePlate, customerId);
        vehicleList.addLast(newVehicle);
        System.out.println("=> Added vehicle successfully: " + licensePlate + " (Vehicle ID: " + newId + ")");
        
        // Auto-save on change (FR-23)
        FileManager.saveVehicles(vehicleList);
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
            // newType is ignored/retained in memory or not stored as per updated Vehicle model without vehicleType
            System.out.println("=> Updated vehicle successfully: " + licensePlate);
            
            // Auto-save on change (FR-23)
            FileManager.saveVehicles(vehicleList);
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
                
                // Auto-save on change (FR-23)
                FileManager.saveVehicles(vehicleList);
                return;
            }
        }
        System.out.println("=> Error: Vehicle not found with license plate: " + licensePlate);
    }

    public MyLinkedList<Vehicle> getVehicleList() {
        return vehicleList;
    }
}