package autowash.cli;

import autowash.storage.DataSeeder;
import autowash.storage.FileManager;

public class Main {
    public static void main(String[] args) {
        // 1. Chạy nạp dữ liệu mẫu
        DataSeeder.initializeData();
        
        // 2. In thử ra xem file đã có dữ liệu chưa
        FileManager.readFromFile("services.txt");
        System.out.println();
        FileManager.readFromFile("customers.txt");
    }
}