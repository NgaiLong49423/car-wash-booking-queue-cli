package autowash.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static void checkAndCreateFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("He thong tu dong tao file moi: " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Loi khi tao file: " + e.getMessage());
        }
    }

    public static void writeToFile(String filePath, String data, boolean append) {
        checkAndCreateFile(filePath); 
        
        try {
            FileWriter fw = new FileWriter(filePath, append);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(data);
            bw.newLine(); 
            
            bw.close(); 
            System.out.println("Da ghi du lieu thanh cong vao: " + filePath);
            
        } catch (IOException e) {
            System.out.println("Loi ghi file: " + e.getMessage());
        }
    }

    public static void readFromFile(String filePath) {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            
            System.out.println("--- Du lieu dang doc tu " + filePath + " ---");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Khong tim thay file: " + filePath + ". He thong se tu tao khi ban ghi du lieu.");
        } catch (IOException e) {
            System.out.println("Loi doc file: " + e.getMessage());
        }
    }
}