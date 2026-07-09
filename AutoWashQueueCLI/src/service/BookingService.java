package service;

import datastructure.MyQueue;
import model.Booking;

public class BookingService {
    // Khai báo Hàng đợi (Queue) tự cài đặt của Leader
    private MyQueue<Booking> bookingQueue;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
    }

    // TÍNH NĂNG 1: Xem các xe đang xếp hàng
    public void displayQueue() {
        System.out.println("\n--- HANG DOI RUA XE ---");
        if (bookingQueue.isEmpty()) {
            System.out.println("Hien tai khong co xe nao dang cho.");
            return;
        }
        bookingQueue.display();
        System.out.println("-----------------------");
    }

    // TÍNH NĂNG 2: Xếp xe vào hàng đợi (Thêm vào cuối - enqueue)
    public void addBooking(String bookingId, String licensePlate, String serviceId) {
        // CẬP NHẬT: Đã truyền thêm trạng thái "Dang cho" vào tham số thứ 4
        Booking newBooking = new Booking(bookingId, licensePlate, serviceId, "Dang cho");
        bookingQueue.enqueue(newBooking);
        System.out.println("=> Da dat lich! Xe " + licensePlate + " da vao hang doi.");
    }

    // TÍNH NĂNG 3: Đưa xe vào rửa (Lấy ra khỏi đầu hàng - dequeue)
    public void processNextBooking() {
        if (bookingQueue.isEmpty()) {
            System.out.println("=> Khong co xe nao dang cho de xu ly.");
            return;
        }
        Booking nextToWash = bookingQueue.dequeue();
        nextToWash.setStatus("Dang rua");
        System.out.println("=> DANG XU LY: " + nextToWash.toString());
    }
    
    public datastructure.MyQueue<model.Booking> getBookingQueue() {
    return bookingQueue;
}
}