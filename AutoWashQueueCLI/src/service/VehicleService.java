package service;

import datastructure.MyLinkedList;
import model.Vehicle;

public class VehicleService {
    private MyLinkedList<Vehicle> vehicleList;

    public VehicleService() {
        this.vehicleList = new MyLinkedList<>();
    }

    public void displayAllVehicles() {
        System.out.println("\n--- DANH SACH XE CO ---");
        if (vehicleList.isEmpty()) {
            System.out.println("Chua co xe nao trong he thong!");
            return;
        }
        vehicleList.display();
        System.out.println("-----------------------");
    }

    public void addVehicle(String licensePlate, String customerId, String type) {
        if (findVehicleByLicense(licensePlate) != null) {
            System.out.println("=> Loi: Bien so xe " + licensePlate + " da ton tai!");
            return;
        }
        Vehicle newVehicle = new Vehicle(licensePlate, customerId, type);
        vehicleList.addLast(newVehicle);
        System.out.println("=> Da them xe thanh cong: " + licensePlate);
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
            System.out.println("=> Da cap nhat thong tin xe: " + licensePlate);
        } else {
            System.out.println("=> Loi: Khong tim thay xe bien so " + licensePlate);
        }
    }

    public void deleteVehicle(String licensePlate) {
        int size = vehicleList.size();
        for (int i = 0; i < size; i++) {
            Vehicle v = vehicleList.get(i);
            if (v.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                vehicleList.remove(i);
                System.out.println("=> Da xoa xe: " + licensePlate);
                return;
            }
        }
        System.out.println("=> Loi: Khong tim thay xe bien so " + licensePlate);
    }

    // --- HÀM MỚI THÊM ĐỂ FILE MANAGER LẤY DỮ LIỆU ---
    public MyLinkedList<Vehicle> getVehicleList() {
        return vehicleList;
    }
}