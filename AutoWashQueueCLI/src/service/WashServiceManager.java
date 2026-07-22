package service;

import datastructure.MyLinkedList;
import model.WashPackage;
import model.Booking;
import model.History;
import util.FileManager;
import util.TablePrinter;

public class WashServiceManager {
    private MyLinkedList<WashPackage> serviceList;

    public WashServiceManager() {
        this.serviceList = new MyLinkedList<>();
    }

    public void displayAllServices() {
        if (serviceList.isEmpty()) {
            System.out.println("No services found in the system!");
            return;
        }
        TablePrinter.printTable("Service List", serviceList,
                new TablePrinter.Column<WashPackage>("ID", 6, WashPackage::getId),
                new TablePrinter.Column<WashPackage>("SERVICE", 28, WashPackage::getName),
                new TablePrinter.Column<WashPackage>("PRICE (VND)", 14,
                        service -> String.format("%.0f", service.getPrice())),
                new TablePrinter.Column<WashPackage>("DURATION", 12,
                        service -> service.getDuration() + " min"),
                new TablePrinter.Column<WashPackage>("STATUS", 10, WashPackage::getStatus));
    }

    // Default legacy method for backward compatibility
    public void addService(String id, String name, double price) {
        addService(name, price, 30, "ACTIVE");
    }

    public void addService(String name, double price, int duration, String status) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            System.out.println("=> Error: Service name cannot be empty!");
            return;
        }
        if (price <= 0) {
            System.out.println("=> Error: Service price must be greater than 0!");
            return;
        }
        if (duration <= 0) {
            System.out.println("=> Error: Service duration must be greater than 0!");
            return;
        }
        if (status == null || (!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("INACTIVE"))) {
            System.out.println("=> Error: Service status must be either ACTIVE or INACTIVE!");
            return;
        }

        // Generate unique Service ID
        int size = serviceList.size();
        int maxIdNum = 0;
        for (int i = 0; i < size; i++) {
            String currentId = serviceList.get(i).getId();
            if (currentId != null && currentId.startsWith("S")) {
                try {
                    int num = Integer.parseInt(currentId.substring(1));
                    if (num > maxIdNum) {
                        maxIdNum = num;
                    }
                } catch (Exception e) {}
            }
        }
        String newId = String.format("S%03d", maxIdNum + 1);

        WashPackage newService = new WashPackage(newId, name, price, duration, status.toUpperCase());
        serviceList.addLast(newService);
        System.out.println("=> Added service successfully: " + name + " (Service ID: " + newId + ")");
        
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

    public boolean isServiceActive(String serviceId) {
        WashPackage ws = findServiceById(serviceId);
        if (ws == null) {
            return false;
        }

        return "ACTIVE".equalsIgnoreCase(ws.getStatus());
    }

    public void searchServices(String query) {
        if (query == null || query.trim().isEmpty()) {
            System.out.println("Search query cannot be empty!");
            return;
        }
        MyLinkedList<WashPackage> matches = new MyLinkedList<>();
        for (int i = 0; i < serviceList.size(); i++) {
            WashPackage ws = serviceList.get(i);
            if (ws.getId().equalsIgnoreCase(query) || 
                ws.getName().toLowerCase().contains(query.toLowerCase())) {
                matches.addLast(ws);
            }
        }
        if (matches.isEmpty()) {
            System.out.println("No matching services found!");
            return;
        }
        TablePrinter.printTable("Service Search Results", matches,
                new TablePrinter.Column<WashPackage>("ID", 6, WashPackage::getId),
                new TablePrinter.Column<WashPackage>("SERVICE", 28, WashPackage::getName),
                new TablePrinter.Column<WashPackage>("PRICE (VND)", 14,
                        service -> String.format("%.0f", service.getPrice())),
                new TablePrinter.Column<WashPackage>("DURATION", 12,
                        service -> service.getDuration() + " min"),
                new TablePrinter.Column<WashPackage>("STATUS", 10, WashPackage::getStatus));
    }

    // Legacy update method for compatibility
    public void updateService(String id, String newName, double newPrice) {
        updateService(id, newName, newPrice, 30, "ACTIVE");
    }

    public void updateService(String id, String newName, double newPrice, int newDuration, String newStatus) {
        WashPackage ws = findServiceById(id);
        if (ws == null) {
            System.out.println("=> Error: Service not found with ID: " + id);
            return;
        }

        // Validate inputs
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("=> Error: Service name cannot be empty!");
            return;
        }
        if (newPrice <= 0) {
            System.out.println("=> Error: Service price must be greater than 0!");
            return;
        }
        if (newDuration <= 0) {
            System.out.println("=> Error: Service duration must be greater than 0!");
            return;
        }
        if (newStatus == null || (!newStatus.equalsIgnoreCase("ACTIVE") && !newStatus.equalsIgnoreCase("INACTIVE"))) {
            System.out.println("=> Error: Service status must be either ACTIVE or INACTIVE!");
            return;
        }

        ws.setName(newName);
        ws.setPrice(newPrice);
        ws.setDuration(newDuration);
        ws.setStatus(newStatus.toUpperCase());
        System.out.println("=> Updated service successfully: " + id);
        
        // Auto-save on change (FR-23)
        FileManager.saveServices(serviceList);
    }

    public void deleteService(String id, BookingService bookingService, MyLinkedList<History> historyList) {
        WashPackage ws = findServiceById(id);
        if (ws == null) {
            System.out.println("=> Error: Service not found with ID: " + id);
            return;
        }

        // 1. Kiểm tra ràng buộc liên kết Booking
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            String bServiceId = bookings.get(i).getServiceId();
            if (bServiceId != null && bServiceId.equalsIgnoreCase(id)) {
                System.out.println("=> Error: Cannot delete service " + id + " because it is linked to booking(s)!");
                return;
            }
        }

        // 2. Kiểm tra ràng buộc liên kết Lịch sử rửa xe (History)
        for (int i = 0; i < historyList.size(); i++) {
            String hServiceName = historyList.get(i).getServiceName();
            String hHistoryId = historyList.get(i).getHistoryId();
            if ((hServiceName != null && ws.getName() != null && hServiceName.equalsIgnoreCase(ws.getName())) || 
                (hHistoryId != null && hHistoryId.equalsIgnoreCase(id))) {
                System.out.println("=> Error: Cannot delete service " + id + " because it has associated history records!");
                return;
            }
        }

        // Tiến hành xóa
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).getId().equalsIgnoreCase(id)) {
                serviceList.remove(i);
                System.out.println("=> Deleted service successfully: " + id);
                
                // Auto-save on change (FR-23)
                FileManager.saveServices(serviceList);
                return;
            }
        }
    }

    // Sắp xếp chọn (Selection Sort) theo Giá dịch vụ
    public void sortServicesByPrice(boolean ascending) {
        int size = serviceList.size();
        if (size <= 1) return;

        // Trích xuất ra mảng
        WashPackage[] arr = new WashPackage[size];
        for (int i = 0; i < size; i++) {
            arr[i] = serviceList.get(i);
        }

        // Thực hiện Selection Sort trên mảng
        for (int i = 0; i < size - 1; i++) {
            int targetIdx = i;
            for (int j = i + 1; j < size; j++) {
                if (ascending ? (arr[j].getPrice() < arr[targetIdx].getPrice()) : (arr[j].getPrice() > arr[targetIdx].getPrice())) {
                    targetIdx = j;
                }
            }
            WashPackage temp = arr[i];
            arr[i] = arr[targetIdx];
            arr[targetIdx] = temp;
        }

        // Đắp ngược trở lại LinkedList
        serviceList.clear();
        for (int i = 0; i < size; i++) {
            serviceList.addLast(arr[i]);
        }
        System.out.println("=> Sorted services by price " + (ascending ? "ascending" : "descending") + " successfully.");
    }

    // Sắp xếp chọn (Selection Sort) theo Thời gian thi công
    public void sortServicesByDuration(boolean ascending) {
        int size = serviceList.size();
        if (size <= 1) return;

        // Trích xuất ra mảng
        WashPackage[] arr = new WashPackage[size];
        for (int i = 0; i < size; i++) {
            arr[i] = serviceList.get(i);
        }

        // Thực hiện Selection Sort trên mảng
        for (int i = 0; i < size - 1; i++) {
            int targetIdx = i;
            for (int j = i + 1; j < size; j++) {
                if (ascending ? (arr[j].getDuration() < arr[targetIdx].getDuration()) : (arr[j].getDuration() > arr[targetIdx].getDuration())) {
                    targetIdx = j;
                }
            }
            WashPackage temp = arr[i];
            arr[i] = arr[targetIdx];
            arr[targetIdx] = temp;
        }

        // Đắp ngược trở lại LinkedList
        serviceList.clear();
        for (int i = 0; i < size; i++) {
            serviceList.addLast(arr[i]);
        }
        System.out.println("=> Sorted services by duration " + (ascending ? "ascending" : "descending") + " successfully.");
    }

    public MyLinkedList<WashPackage> getServiceList() {
        return serviceList;
    }
}
