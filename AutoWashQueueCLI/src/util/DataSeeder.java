package util;

import service.CustomerService;
import service.VehicleService;
import service.WashServiceManager;

public class DataSeeder {
    
    public static void seed(CustomerService customerService, WashServiceManager washService, VehicleService vehicleService) {
        
        // 1. Thêm Khách hàng mẫu 
        customerService.addCustomer("Nguyen Van A", "0901234567", "GOLD", 100);
        customerService.addCustomer("Tran Thi B", "0912345678", "SILVER", 50);
        customerService.addCustomer("Le Van C", "0988777666", "MEMBER", 0);

        // 2. Thêm Dịch vụ mẫu
        washService.addService("DV01", "Rua xe tieu chuan", 50000);
        washService.addService("DV02", "Rua xe cao cap + Hut bui", 100000);
        washService.addService("DV03", "Rua xe + Danh bong", 250000);

        // 3. Thêm Xe cộ mẫu 
        vehicleService.addVehicle("59A-123.45", "C001", "Sedan 4 cho");
        vehicleService.addVehicle("60B-678.90", "C002", "SUV 7 cho");
        vehicleService.addVehicle("51C-999.99", "C003", "Ban tai");
        
        System.out.println("\n[He thong] -> Da nap du lieu mau (Seed Data) thanh cong!");
    }
}