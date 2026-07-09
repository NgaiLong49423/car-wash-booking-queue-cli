package service;

import datastructure.MyLinkedList;
import model.WashPackage;
import util.FileManager;

public class WashServiceManager {
    private MyLinkedList<WashPackage> serviceList;

    public WashServiceManager() {
        this.serviceList = new MyLinkedList<>();
    }

    public void displayAllServices() {
        System.out.println("\n--- SERVICE LIST ---");
        if (serviceList.isEmpty()) {
            System.out.println("No services found in the system!");
            return;
        }
        serviceList.display();
        System.out.println("--------------------");
    }

    public void addService(String id, String name, double price) {
        // Default values for duration and status if not provided (compatibility)
        addService(id, name, price, 30, "ACTIVE");
    }

    public void addService(String id, String name, double price, int duration, String status) {
        if (findServiceById(id) != null) {
            System.out.println("=> Error: Service ID " + id + " already exists!");
            return;
        }
        WashPackage newService = new WashPackage(id, name, price, duration, status);
        serviceList.addLast(newService);
        System.out.println("=> Added service successfully: " + name);
        
        // Auto-save on change (FR-23)
        FileManager.saveServices(serviceList);
    }

    public WashPackage findServiceById(String id) {
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            WashPackage ws = serviceList.get(i);
            if (ws.getId().equalsIgnoreCase(id)) {
                return ws;
            }
        }
        return null;
    }

    public void updateService(String id, String newName, double newPrice) {
        WashPackage ws = findServiceById(id);
        if (ws != null) {
            ws.setName(newName);
            ws.setPrice(newPrice);
            System.out.println("=> Updated service successfully: " + id);
            
            // Auto-save on change (FR-23)
            FileManager.saveServices(serviceList);
        } else {
            System.out.println("=> Error: Service not found with ID: " + id);
        }
    }

    public void updateService(String id, String newName, double newPrice, int newDuration, String newStatus) {
        WashPackage ws = findServiceById(id);
        if (ws != null) {
            ws.setName(newName);
            ws.setPrice(newPrice);
            ws.setDuration(newDuration);
            ws.setStatus(newStatus);
            System.out.println("=> Updated service successfully: " + id);
            
            // Auto-save on change (FR-23)
            FileManager.saveServices(serviceList);
        } else {
            System.out.println("=> Error: Service not found with ID: " + id);
        }
    }

    public void deleteService(String id) {
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            WashPackage ws = serviceList.get(i);
            if (ws.getId().equalsIgnoreCase(id)) {
                serviceList.remove(i);
                System.out.println("=> Deleted service successfully: " + id);
                
                // Auto-save on change (FR-23)
                FileManager.saveServices(serviceList);
                return;
            }
        }
        System.out.println("=> Error: Service not found with ID: " + id);
    }

    public MyLinkedList<WashPackage> getServiceList() {
        return serviceList;
    }
}