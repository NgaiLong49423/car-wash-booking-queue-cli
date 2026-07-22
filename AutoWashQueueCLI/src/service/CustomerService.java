package service;

import datastructure.MyLinkedList;
import model.*;
import util.FileManager;
import util.TablePrinter;

public class CustomerService {
    private MyLinkedList<Customer> customerList;

    public CustomerService() {
        this.customerList = new MyLinkedList<>();
    }

    public void displayAllCustomers() {
        if (customerList.isEmpty()) {
            System.out.println("No customers found in the system!");
            return;
        }
        printCustomers("Customer List", customerList);
    }

    public void addCustomer(String name, String phone, String level, int points) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            System.out.println("=> Error: Customer name cannot be empty!");
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("=> Error: Phone number cannot be empty!");
            return;
        }
        if (!isValidPhone(phone)) {
            System.out.println("=> Error: Phone number must contain exactly 10 digits and start with 0.");
            return;
        }

        phone = phone.trim();

        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            if (customerList.get(i).getPhone().equals(phone)) {
                System.out.println("=> Error: Phone number " + phone + " already exists in the system!");
                return;
            }
        }

        int maxIdNum = 0;
        for (int i = 0; i < size; i++) {
            String currentId = customerList.get(i).getId();
            if (currentId.startsWith("C")) {
                try {
                    int num = Integer.parseInt(currentId.substring(1));
                    if (num > maxIdNum) {
                        maxIdNum = num;
                    }
                } catch (Exception e) {}
            }
        }
        String newId = String.format("C%03d", maxIdNum + 1);

        Customer newCustomer = new Customer(newId, name, phone, level, points, 0.0, 0);
        customerList.addLast(newCustomer);
        System.out.println("=> Added customer successfully: " + name + " (Customer ID: " + newId + ")");
        
        // Auto-save on change (FR-23)
        FileManager.saveCustomers(customerList);
    }

    public Customer findCustomerById(String id) {
        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            Customer c = customerList.get(i);
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public void searchCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            System.out.println("Search query cannot be empty!");
            return;
        }
        MyLinkedList<Customer> matches = new MyLinkedList<>();
        for (int i = 0; i < customerList.size(); i++) {
            Customer c = customerList.get(i);
            if (c.getId().equalsIgnoreCase(query) || 
                c.getName().toLowerCase().contains(query.toLowerCase()) || 
                c.getPhone().equals(query)) {
                matches.addLast(c);
            }
        }
        if (matches.isEmpty()) {
            System.out.println("No matching customers found!");
            return;
        }
        printCustomers("Customer Search Results", matches);
    }

    public void updateCustomer(String id, String newName, String newPhone, String newLevel, int newPoints) {
        Customer c = findCustomerById(id);
        if (c != null) {
            if (newName == null || newName.trim().isEmpty()) {
                System.out.println("=> Error: Customer name cannot be empty!");
                return;
            }
            if (newPhone == null || newPhone.trim().isEmpty()) {
                System.out.println("=> Error: Phone number cannot be empty!");
                return;
            }
            if (!isValidPhone(newPhone)) {
                System.out.println("=> Error: Phone number must contain exactly 10 digits and start with 0.");
                return;
            }

            newPhone = newPhone.trim();

            if (!c.getPhone().equals(newPhone)) {
                int size = customerList.size();
                for (int i = 0; i < size; i++) {
                    if (customerList.get(i).getPhone().equals(newPhone)) {
                        System.out.println("=> Error: New phone number belongs to another customer!");
                        return;
                    }
                }
            }
            c.setName(newName);
            c.setPhone(newPhone);
            c.setMembershipLevel(newLevel);
            c.setPoints(newPoints);
            System.out.println("=> Updated customer successfully: " + id);
            
            // Auto-save on change (FR-23)
            FileManager.saveCustomers(customerList);
        } else {
            System.out.println("=> Error: Customer not found with ID: " + id);
        }
    }

    public void deleteCustomer(String id, VehicleService vehicleService, BookingService bookingService, MyLinkedList<History> historyList) {
        Customer c = findCustomerById(id);
        if (c == null) {
            System.out.println("=> Error: Customer not found with ID: " + id);
            return;
        }

        // 1. Kiểm tra ràng buộc liên kết Xe
        MyLinkedList<Vehicle> vehicles = vehicleService.getVehicleList();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getCustomerId().equalsIgnoreCase(id)) {
                System.out.println("=> Error: Cannot delete customer " + id + " because they have associated vehicle(s)!");
                return;
            }
        }

        // 2. Kiểm tra ràng buộc liên kết Booking
        MyLinkedList<Booking> bookings = bookingService.getBookingList();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getCustomerId().equalsIgnoreCase(id)) {
                System.out.println("=> Error: Cannot delete customer " + id + " because they have associated booking(s)!");
                return;
            }
        }

        // 3. Kiểm tra ràng buộc liên kết Lịch sử rửa xe
        for (int i = 0; i < historyList.size(); i++) {
            if (historyList.get(i).getCustomerId().equalsIgnoreCase(id)) {
                System.out.println("=> Error: Cannot delete customer " + id + " because they have associated history records!");
                return;
            }
        }

        // Nếu qua hết các kiểm tra, tiến hành xóa
        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            if (customerList.get(i).getId().equalsIgnoreCase(id)) {
                customerList.remove(i);
                System.out.println("=> Deleted customer successfully: " + id);
                
                // Auto-save on change (FR-23)
                FileManager.saveCustomers(customerList);
                return;
            }
        }
    }

    public MyLinkedList<Customer> getCustomerList() {
        return customerList;
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.trim().matches("0\\d{9}");
    }

    private void printCustomers(String title, MyLinkedList<Customer> customers) {
        TablePrinter.printTable(title, customers,
                new TablePrinter.Column<Customer>("ID", 6, Customer::getId),
                new TablePrinter.Column<Customer>("NAME", 22, Customer::getName),
                new TablePrinter.Column<Customer>("PHONE", 12, Customer::getPhone),
                new TablePrinter.Column<Customer>("TIER", 10, Customer::getMembershipLevel),
                new TablePrinter.Column<Customer>("POINTS", 8,
                        customer -> String.valueOf(customer.getPoints())),
                new TablePrinter.Column<Customer>("TOTAL SPENT", 14,
                        customer -> String.format("%.0f", customer.getTotalSpent())));
    }
}
