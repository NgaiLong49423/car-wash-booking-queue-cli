package service;

import datastructure.MyLinkedList;
import model.Customer;

public class CustomerService {
    // Khai báo danh sách liên kết đơn tự cài đặt theo đúng yêu cầu 
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
        // Gọi hàm display() trong MyLinkedList
        customerList.display();
        System.out.println("----------------------------");
    }

    public void addCustomer(String id, String name, String phone, String level, int points) {
        // Kiểm tra trùng lặp mã khách hàng
        if (findCustomerById(id) != null) {
            System.out.println("=> Loi: Ma khach hang " + id + " da ton tai!");
            return;
        }
        Customer newCustomer = new Customer(id, name, phone, level, points);
        // Dùng hàm addLast để thêm vào cuối danh sách liên kết
        customerList.addLast(newCustomer);
        System.out.println("=> Da them khach hang thanh cong: " + name);
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

    // --- HÀM MỚI THÊM ĐỂ FILE MANAGER LẤY DỮ LIỆU ---
    public MyLinkedList<Customer> getCustomerList() {
        return customerList;
    }
}