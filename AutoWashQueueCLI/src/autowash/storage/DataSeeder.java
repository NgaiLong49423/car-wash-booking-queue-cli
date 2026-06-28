package autowash.storage;

import java.io.File;

public class DataSeeder {

    public static void initializeData() {
        System.out.println("=== KHOI TAO DU LIEU MAU (SEED DATA) ===");

        // 1. Dịch vụ (Services): Mã dịch vụ, Tên, Giá tiền
        String serviceFile = "services.txt";
        if (!new File(serviceFile).exists()) {
            FileManager.writeToFile(serviceFile, "SRV01,Rua xe tieu chuan,50000", true);
            FileManager.writeToFile(serviceFile, "SRV02,Rua xe VIP,100000", true);
            FileManager.writeToFile(serviceFile, "SRV03,Ve sinh noi that,150000", true);
        }

        // 2. Khách hàng (Customers): Mã KH, Tên, Hạng thành viên, Điểm tích lũy
        String customerFile = "customers.txt";
        if (!new File(customerFile).exists()) {
            FileManager.writeToFile(customerFile, "CUS01,Nguyen Anh Kiet,VIP,150", true);
            FileManager.writeToFile(customerFile, "CUS02,Ngo Gia Long,Standard,0", true);
        }

        // 3. Xe cộ (Vehicles): Biển số, Mã KH chủ xe, Loại xe
        String vehicleFile = "vehicles.txt";
        if (!new File(vehicleFile).exists()) {
            FileManager.writeToFile(vehicleFile, "59-A1 12345,CUS01,Honda Civic", true);
            FileManager.writeToFile(vehicleFile, "69-B2 67890,CUS02,Toyota Vios", true);
        }
        
        System.out.println("=== HOAN TAT NAP DU LIEU MAU ===\n");
    }
}