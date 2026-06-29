package service;

import datastructure.MyLinkedList;
import model.WashService;

public class WashServiceManager {
    private MyLinkedList<WashService> serviceList;

    public WashServiceManager() {
        this.serviceList = new MyLinkedList<>();
    }

    public void displayAllServices() {
        System.out.println("\n--- DANH SACH DICH VU ---");
        if (serviceList.isEmpty()) {
            System.out.println("Chua co dich vu nao trong he thong!");
            return;
        }
        serviceList.display();
        System.out.println("-------------------------");
    }

    public void addService(String id, String name, double price) {
        if (findServiceById(id) != null) {
            System.out.println("=> Loi: Ma dich vu " + id + " da ton tai!");
            return;
        }
        WashService newService = new WashService(id, name, price);
        serviceList.addLast(newService);
        System.out.println("=> Da them dich vu: " + name);
    }

    public WashService findServiceById(String id) {
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            WashService ws = serviceList.get(i);
            if (ws.getId().equalsIgnoreCase(id)) {
                return ws;
            }
        }
        return null;
    }

    public void updateService(String id, String newName, double newPrice) {
        WashService ws = findServiceById(id);
        if (ws != null) {
            ws.setName(newName);
            ws.setPrice(newPrice);
            System.out.println("=> Da cap nhat dich vu: " + id);
        } else {
            System.out.println("=> Loi: Khong tim thay dich vu co ma " + id);
        }
    }

    public void deleteService(String id) {
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            WashService ws = serviceList.get(i);
            if (ws.getId().equalsIgnoreCase(id)) {
                serviceList.remove(i);
                System.out.println("=> Da xoa dich vu: " + id);
                return;
            }
        }
        System.out.println("=> Loi: Khong tim thay dich vu co ma " + id);
    }

    // --- HÀM MỚI THÊM ĐỂ FILE MANAGER LẤY DỮ LIỆU ---
    public MyLinkedList<WashService> getServiceList() {
        return serviceList;
    }
}