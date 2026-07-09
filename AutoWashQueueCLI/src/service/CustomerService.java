package service;

import datastructure.MyLinkedList;
import model.Customer;
import util.FileManager;

public class CustomerService {
    private MyLinkedList<Customer> customerList;

    public CustomerService() {
        this.customerList = new MyLinkedList<>();
    }

    public void displayAllCustomers() {
        System.out.println("\n--- CUSTOMER LIST ---");
        if (customerList.isEmpty()) {
            System.out.println("No customers found in the system!");
            return;
        }
        customerList.display();
        System.out.println("---------------------");
    }

    public void addCustomer(String name, String phone, String level, int points) {
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

    public void updateCustomer(String id, String newName, String newPhone, String newLevel, int newPoints) {
        Customer c = findCustomerById(id);
        if (c != null) {
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

    public void deleteCustomer(String id) {
        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            Customer c = customerList.get(i);
            if (c.getId().equalsIgnoreCase(id)) {
                customerList.remove(i);
                System.out.println("=> Deleted customer successfully: " + id);
                
                // Auto-save on change (FR-23)
                FileManager.saveCustomers(customerList);
                return;
            }
        }
        System.out.println("=> Error: Customer not found with ID: " + id);
    }

    public MyLinkedList<Customer> getCustomerList() {
        return customerList;
    }
}