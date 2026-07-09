package service;

import datastructure.MyLinkedList;
import datastructure.MyQueue;
import model.Booking;
import util.FileManager;

public class BookingService {
    private MyQueue<Booking> bookingQueue;
    private MyLinkedList<Booking> bookingList;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
        this.bookingList = new MyLinkedList<>();
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
        Booking newBooking = new Booking(
                bookingId, 
                "C000", // Default Customer ID for now (Issue 3 / 7 responsibility)
                licensePlate, // Vehicle identifier (Mã xe / Biển số)
                serviceId, 
                "2026-07-10", // Default Date (Issue 6 responsibility)
                "MORNING", // Default Period (Issue 6 responsibility)
                "WAITING", 
                "UNPAID", 
                "NONE", 
                System.currentTimeMillis()
        );
        bookingQueue.enqueue(newBooking);
        bookingList.addLast(newBooking);
        System.out.println("=> Da dat lich! Xe " + licensePlate + " da vao hang doi.");
        
        // Auto-save bookings on change (FR-23)
        FileManager.saveBookings(bookingList);
    }

    // TÍNH NĂNG 3: Đưa xe vào rửa (Lấy ra khỏi đầu hàng - dequeue)
    public void processNextBooking() {
        if (bookingQueue.isEmpty()) {
            System.out.println("=> Khong co xe nao dang cho de xu ly.");
            return;
        }
        Booking nextToWash = bookingQueue.dequeue();
        nextToWash.setStatus("SERVING");

        // Update status in the main bookingList
        int size = bookingList.size();
        for (int i = 0; i < size; i++) {
            Booking b = bookingList.get(i);
            if (b.getBookingId().equalsIgnoreCase(nextToWash.getBookingId())) {
                b.setBookingStatus("SERVING");
                break;
            }
        }
        
        System.out.println("=> DANG XU LY: " + nextToWash.toString());
        
        // Auto-save bookings on change (FR-23)
        FileManager.saveBookings(bookingList);
    }
    
    public MyQueue<Booking> getBookingQueue() {
        return bookingQueue;
    }

    public MyLinkedList<Booking> getBookingList() {
        return bookingList;
    }
}