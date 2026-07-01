package app;

import util.DataSeeder;
import util.FileManager;
import service.BookingService;
import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;
import util.ConsoleInputter;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo toàn bộ hệ thống xử lý
        CustomerService customerService = new CustomerService();
        WashServiceManager washService = new WashServiceManager();
        VehicleService vehicleService = new VehicleService();
        BookingService bookingService = new BookingService();

        // 2. Cố gắng Load dữ liệu từ file. Nếu chưa có file (lần chạy đầu tiên), dùng DataSeeder
        boolean hasSavedData = FileManager.loadData(
                customerService.getCustomerList(), 
                washService.getServiceList(), 
                vehicleService.getVehicleList()
        );
        
        if (!hasSavedData) {
            DataSeeder.seed(customerService, washService, vehicleService);
        } else {
            System.out.println("\n[He thong] -> Da tai du lieu tu file .txt thanh cong!");
        }

        // 3. Vòng lặp Menu chính
        while (true) {
            int choice = ConsoleInputter.intMenu("HE THONG QUAN LY RUA XE",
                    "Quan ly Khach hang",
                    "Quan ly Dich vu",
                    "Quan ly Xe co",
                    "Quan ly Hang doi (Dat lich)",
                    "Thoat & Luu du lieu");

            switch (choice) {
                case 1:
                    customerService.displayAllCustomers();
                    break;
                case 2:
                    washService.displayAllServices();
                    break;
                case 3:
                    vehicleService.displayAllVehicles();
                    break;
                case 4:
                    bookingService.displayQueue();
                    break;
                case 5:
                    // Tự động sao lưu dữ liệu trước khi thoát
                    FileManager.saveData(
                            customerService.getCustomerList(), 
                            washService.getServiceList(), 
                            vehicleService.getVehicleList()
                    );
                    System.out.println("=> He thong dong. Tam biet!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Vui long chon chuc nang hop le!");
            }
        }
    }
}