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

    // Feature 1: Display the current booking queue.
    public void displayQueue() {
        System.out.println("\n--- BOOKING QUEUE ---");
        if (bookingQueue.isEmpty()) {
            System.out.println("There are no bookings waiting to be served.");
            return;
        }
        bookingQueue.display();
        System.out.println("-----------------------");
    }

    // Feature 2: Add a booking to the end of the queue.
    public void addBooking(String bookingId, String licensePlate, String serviceId) {
        Booking newBooking = new Booking(
                bookingId, 
                "C000", // Default Customer ID for now (Issue 3 / 7 responsibility)
                licensePlate, // Vehicle identifier (vehicle ID or license plate)
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
        System.out.println("=> Booking created. Vehicle " + licensePlate + " was added to the queue.");
        
        // Auto-save bookings on change (FR-23)
        FileManager.saveBookings(bookingList);
    }

    // Feature 3: Start processing the booking at the front of the queue.
    public void processNextBooking() {
        if (bookingQueue.isEmpty()) {
            System.out.println("=> There is no waiting booking to process.");
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
        
        System.out.println("=> NOW SERVING: " + nextToWash.toString());
        
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
            System.out.println("=> There are no bookings in the system.");
        }else{
            int size = bookingList.size();
            for (int i = 0; i < size; i++) {
                b = bookingList.get(i);
                if (b.getBookingId().equals(bookingID)) {
                    if (b.getBookingStatus().equalsIgnoreCase("serving")
                            && b.getPaymentStatus().equalsIgnoreCase("paid")) {
                        bookingStack.clear();
                        bookingStack.push(b);
                        b.setStatus("COMPLETED");
                        FileManager.saveBookings(bookingList);
                        break;
                    } else if (!b.getBookingStatus().equalsIgnoreCase("serving")) {
                        System.out.println("The booking has not been processed yet.");
                    } else {
                        System.out.println("The booking has not been paid.");
                    }
                }
            }
        }
        return b;
    }
    
    public void cancelBooking(String bookingID){
        if(bookingList.isEmpty() && bookingQueue.isEmpty()){
            System.out.println("=> There are no bookings in the system.");
        }else{
            int size = bookingList.size();
            for(int i=0; i<size; i++){
                Booking b = bookingList.get(i);
                if (b.getBookingId().equals(bookingID)) {
                    if (!b.getBookingStatus().equalsIgnoreCase("completed")) {
                        b.setStatus("CANCELLED");
                        break;
                    } else {
                        System.out.println("This booking has already been completed.");
                    }
                }
            }
            bookingQueue.dequeueNodeByID(bookingID);
            
            FileManager.saveBookings(bookingList);
        }
    }
    
    public Booking undoCompletion(){
        if(bookingStack.isEmpty()){
            System.out.println("There is no completed booking to undo.");
            return null;
        }else{
            Booking b = bookingStack.pop();
            int size = bookingList.size();
            for(int i=0; i<size; i++){
                Booking currentServ = bookingList.get(i);
                if(currentServ.getBookingStatus().equalsIgnoreCase("Serving")){
                    currentServ.setBookingStatus("WAITING");
                    bookingQueue.enqueueFront(currentServ);
                    break;
                }
            }
            b.setBookingStatus("SERVING");
            FileManager.saveBookings(bookingList);
            return b;
        }
    }
}
