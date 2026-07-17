package service;

import datastructure.MyLinkedList;
import datastructure.MyQueue;
import datastructure.MyStack;
import model.Booking;
import model.CompletionRecord;
import util.FileManager;

public class BookingService {
    private MyQueue<Booking> bookingQueue;
    private MyLinkedList<Booking> bookingList;
    private MyStack<Booking> bookingStack;
    private MyStack<CompletionRecord> completionStack;
    private MyLinkedList<Booking> waitlist;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
        this.bookingList = new MyLinkedList<>();
        this.bookingStack = new MyStack<>();
        this.completionStack = new MyStack<>();
        this.waitlist = new MyLinkedList<>();
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

    public MyLinkedList<Booking> getWaitlist() {
        return waitlist;
    }

    /** Registers a WAITING booking in the waitlist managed by the activation flow. */
    public void addToWaitlist(Booking booking) {
        if (booking != null) {
            waitlist.addLast(booking);
        }
    }

    public void recordCompletion(Booking completedBooking, Booking promotedBooking) {
        completionStack.push(new CompletionRecord(completedBooking, promotedBooking));
    }
    
    public Booking completeBooking(String bookingID){
        Booking b = null;
        if (bookingList.isEmpty()) {
            System.out.println("=> Khong co xe nao dang cho de xu ly.");
        }else{
            int size = bookingList.size();
            for (int i = 0; i < size; i++) {
                b = bookingList.get(i);
                if (b.getBookingId().equals(bookingID)) {
                    if (b.getBookingStatus().equalsIgnoreCase("serving")
                            && b.getPaymentStatus().equalsIgnoreCase("paid")) {
                        bookingStack.clear();
                        bookingStack.push(b);
                        b.setStatus("Completed");
                        FileManager.saveBookings(bookingList);
                        break;
                    } else if (!b.getBookingStatus().equalsIgnoreCase("serving")) {
                        System.out.println("Xe chua duoc xu ly");
                    } else {
                        System.out.println("Chua tra phi");
                    }
                }
            }
        }
        return b;
    }
    
    public void cancelBooking(String bookingID){
        if(bookingList.isEmpty() && bookingQueue.isEmpty()){
            System.out.println("=> Khong co xe nao dang cho de xu ly.");
        }else{
            int size = bookingList.size();
            for(int i=0; i<size; i++){
                Booking b = bookingList.get(i);
                if (b.getBookingId().equals(bookingID)) {
                    if (!b.getBookingStatus().equalsIgnoreCase("completed")) {
                        b.setStatus("Cancelled");
                        break;
                    } else {
                        System.out.println("Luot dat nay da duoc hoan thanh");
                    }
                }
            }
            bookingQueue.dequeueNodeByID(bookingID);
            
            FileManager.saveBookings(bookingList);
        }
    }
    
    public Booking undoCompletion(){
        if(bookingStack.isEmpty()){
            System.out.println("Khong co gi de undo");
            return null;
        }else{
            Booking b = bookingStack.pop();
            int size = bookingList.size();
            for(int i=0; i<size; i++){
                Booking currentServ = bookingList.get(i);
                if(currentServ.getBookingStatus().equalsIgnoreCase("Serving")){
                    currentServ.setBookingStatus("Waiting");
                    bookingQueue.enqueueFront(currentServ);
                    break;
                }
            }
            b.setBookingStatus("Serving");
            FileManager.saveBookings(bookingList);
            return b;
        }
    }
}
