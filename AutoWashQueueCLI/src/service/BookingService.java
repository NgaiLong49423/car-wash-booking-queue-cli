package service;

import datastructure.MyLinkedList;
import datastructure.MyPriorityQueue;
import datastructure.MyQueue;
import datastructure.MyStack;
import model.Booking;
import model.CompletionRecord;
import model.Customer;
import model.WaitlistEntry;
import util.FileManager;

public class BookingService {
    private MyQueue<Booking> bookingQueue;
    private MyLinkedList<Booking> bookingList;
    private MyStack<CompletionRecord> completionStack;
    private MyPriorityQueue<WaitlistEntry> waitlist;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
        this.bookingList = new MyLinkedList<>();
        this.completionStack = new MyStack<>();
        this.waitlist = new MyPriorityQueue<>();
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

    public MyPriorityQueue<WaitlistEntry> getWaitlist() {
        return waitlist;
    }

    /** Registers a WAITING booking in the Max Heap waitlist. */
    public void addToWaitlist(Booking booking, Customer customer) {
        if (booking != null && customer != null) {
            waitlist.insert(new WaitlistEntry(booking, getTierPriority(customer)));
        }
    }

    public void recordCompletion(Booking completedBooking, Booking promotedBooking) {
        completionStack.push(new CompletionRecord(completedBooking, promotedBooking));
    }

    public WaitlistEntry peekHighestPriorityWaitlist(String date, String period) {
        MyLinkedList<WaitlistEntry> entries = drainWaitlist();
        WaitlistEntry selected = selectHighestPriority(entries, date, period);
        restoreWaitlist(entries);
        return selected;
    }

    public Booking pollHighestPriorityWaitlist(String date, String period) {
        MyLinkedList<WaitlistEntry> entries = drainWaitlist();
        WaitlistEntry selected = selectHighestPriority(entries, date, period);
        if (selected != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i) == selected) {
                    entries.remove(i);
                    break;
                }
            }
        }
        restoreWaitlist(entries);
        return selected == null ? null : selected.getBooking();
    }

    /** Removes a specific booking from the waitlist, if it is present. */
    public boolean removeFromWaitlist(String bookingId) {
        MyLinkedList<WaitlistEntry> entries = drainWaitlist();
        boolean removed = false;
        for (int i = 0; i < entries.size(); i++) {
            if (bookingId.equalsIgnoreCase(entries.get(i).getBooking().getBookingId())) {
                entries.remove(i);
                removed = true;
                break;
            }
        }
        restoreWaitlist(entries);
        return removed;
    }

    private WaitlistEntry selectHighestPriority(MyLinkedList<WaitlistEntry> entries, String date, String period) {
        WaitlistEntry selected = null;
        for (int i = 0; i < entries.size(); i++) {
            WaitlistEntry entry = entries.get(i);
            Booking booking = entry.getBooking();
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && (selected == null || entry.compareTo(selected) > 0)) {
                selected = entry;
            }
        }
        return selected;
    }

    private MyLinkedList<WaitlistEntry> drainWaitlist() {
        MyLinkedList<WaitlistEntry> entries = new MyLinkedList<>();
        while (!waitlist.isEmpty()) {
            entries.addLast(waitlist.poll());
        }
        return entries;
    }

    private void restoreWaitlist(MyLinkedList<WaitlistEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            waitlist.insert(entries.get(i));
        }
    }

    private int getTierPriority(Customer customer) {
        if ("PLATINUM".equalsIgnoreCase(customer.getMembershipLevel())) return 4;
        if ("GOLD".equalsIgnoreCase(customer.getMembershipLevel())) return 3;
        if ("SILVER".equalsIgnoreCase(customer.getMembershipLevel())) return 2;
        return 1;
    }
}
