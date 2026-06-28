package autowash.cli;

import autowash.storage.FileManager;

public class Main {
    public static void main(String[] args) {
        String dataFile = "data_khach_hang.txt";
        
        FileManager.writeToFile(dataFile, "CUS01, Nguyen Anh Kiet, VIP", false);
        FileManager.writeToFile(dataFile, "CUS02, Ngo Gia Long, Thuong", true);
        
        FileManager.readFromFile(dataFile);
    }
}