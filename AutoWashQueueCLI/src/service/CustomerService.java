package service;

import datastructure.MyLinkedList;
import model.Customer;

public class CustomerService {
    private MyLinkedList<Customer> customerList;

    public CustomerService() {
        this.customerList = new MyLinkedList<>();
    }

    public void displayAllCustomers() {
        System.out.println("\n--- DANH SACH KHACH HANG ---");
        if (customerList.isEmpty()) {
            System.out.println("Chua co khach hang nao trong he thong!");
            return;
        }
        customerList.display();
        System.out.println("----------------------------");
    }

    // Đã bỏ tham số 'id', tự động sinh ID bên trong hàm và kiểm tra số điện thoại
    public void addCustomer(String name, String phone, String level, int points) {
        // 1. Kiểm tra trùng lặp số điện thoại
        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            if (customerList.get(i).getPhone().equals(phone)) {
                System.out.println("=> Loi: So dien thoai " + phone + " da ton tai trong he thong!");
                return;
            }
        }

        // 2. Logic tự động sinh ID (C001, C002...)
        int maxIdNum = 0;
        for (int i = 0; i < size; i++) {
            String currentId = customerList.get(i).getId();
            if (currentId.startsWith("C")) {
                try {
                    int num = Integer.parseInt(currentId.substring(1));
                    if (num > maxIdNum) {
                        maxIdNum = num;
                    }
                } catch (Exception e) {
                    // Bỏ qua nếu mã không đúng định dạng
                }
            }
        }
        String newId = String.format("C%03d", maxIdNum + 1);

        // 3. Tạo và lưu khách hàng
        Customer newCustomer = new Customer(newId, name, phone, level, points);
        customerList.addLast(newCustomer);
        System.out.println("=> Da them khach hang thanh cong: " + name + " (Ma KH: " + newId + ")");
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
            // Kiểm tra trùng số điện thoại nếu khách hàng đổi sang số mới
            if (!c.getPhone().equals(newPhone)) {
                int size = customerList.size();
                for (int i = 0; i < size; i++) {
                    if (customerList.get(i).getPhone().equals(newPhone)) {
                        System.out.println("=> Loi: So dien thoai moi da bi trung voi khach hang khac!");
                        return;
                    }
                }
            }
            c.setName(newName);
            c.setPhone(newPhone);
            c.setMembershipLevel(newLevel);
            c.setPoints(newPoints);
            System.out.println("=> Da cap nhat thong tin cho khach hang: " + id);
        } else {
            System.out.println("=> Loi: Khong tim thay khach hang co ma " + id);
        }
    }

    public void deleteCustomer(String id) {
        int size = customerList.size();
        for (int i = 0; i < size; i++) {
            Customer c = customerList.get(i);
            if (c.getId().equalsIgnoreCase(id)) {
                customerList.remove(i);
                System.out.println("=> Da xoa khach hang: " + id);
                return;
            }
        }
        System.out.println("=> Loi: Khong tim thay khach hang co ma " + id);
    }

    public MyLinkedList<Customer> getCustomerList() {
        return customerList;
    }
}