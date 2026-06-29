package data;

import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;

public class DataSeeder {
    
    // Hàm này sẽ nhận vào các bộ quản lý (Manager/Service) để bơm dữ liệu vào
    public static void seed(CustomerService customerService, WashServiceManager washService, VehicleService vehicleService) {
        
        // 1. Thêm Khách hàng mẫu
        customerService.addCustomer("KH01", "Nguyen Van A", "0901234567", "Gold", 100);
        customerService.addCustomer("KH02", "Tran Thi B", "0912345678", "Silver", 50);
        customerService.addCustomer("KH03", "Le Van C", "0988777666", "Thong thuong", 0);

        // 2. Thêm Dịch vụ mẫu
        washService.addService("DV01", "Rua xe tieu chuan", 50000);
        washService.addService("DV02", "Rua xe cao cap + Hut bui", 100000);
        washService.addService("DV03", "Rua xe + Danh bong", 250000);

        // 3. Thêm Xe cộ mẫu
        vehicleService.addVehicle("59A-123.45", "KH01", "Sedan 4 cho");
        vehicleService.addVehicle("60B-678.90", "KH02", "SUV 7 cho");
        vehicleService.addVehicle("51C-999.99", "KH03", "Ban tai");
        
        System.out.println("\n[He thong] -> Da nap du lieu mau (Seed Data) thanh cong!");
    }
}