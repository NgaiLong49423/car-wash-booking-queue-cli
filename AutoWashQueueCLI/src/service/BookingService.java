package service;

import datastructure.MyLinkedList;
import datastructure.MyPriorityQueue;
import datastructure.MyQueue;
import datastructure.MyStack;
import model.Booking;
import model.CompletionRecord;
import model.Customer;
import model.Vehicle;
import model.WaitlistEntry;
import model.WashPackage;
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
        displayMainQueue(null, null, null, null);
    }

    /** Displays the main queue in FIFO order. */
    public void displayMainQueue(String period, CustomerService customerService,
            VehicleService vehicleService, WashServiceManager washService) {
        System.out.println("\n--- MAIN QUEUE (FIFO) ---");
        printCapacity(period);
        printBookingTable(bookingQueue.snapshot(), customerService, vehicleService, washService);
    }

    /** Displays the waitlist from highest to lowest priority without changing it. */
    public void displayWaitlist(String period, CustomerService customerService,
            VehicleService vehicleService, WashServiceManager washService) {
        System.out.println("\n--- WAITLIST (PRIORITY ORDER) ---");
        printCapacity(period);
        MyLinkedList<WaitlistEntry> entries = waitlist.snapshotInPriorityOrder();
        MyLinkedList<Booking> bookings = new MyLinkedList<>();
        for (int i = 0; i < entries.size(); i++) {
            bookings.addLast(entries.get(i).getBooking());
        }
        printBookingTable(bookings, customerService, vehicleService, washService);
    }

    /** Displays WAITING bookings for an inactive or future period. */
    public void displayFutureBookings(String date, String period,
            CustomerService customerService, VehicleService vehicleService,
            WashServiceManager washService, String currentDate, String currentPeriod) {
        if (date == null || date.trim().isEmpty() || !isValidPeriod(period)) {
            System.out.println("=> Error: Enter a valid date and period.");
            return;
        }
        if (date.equalsIgnoreCase(currentDate) && period.equalsIgnoreCase(currentPeriod)) {
            System.out.println("=> This is the current period. Use the queue monitoring options instead.");
            return;
        }

        MyLinkedList<Booking> futureBookings = new MyLinkedList<>();
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            if (date.equalsIgnoreCase(booking.getDate())
                    && period.equalsIgnoreCase(booking.getPeriod())
                    && "WAITING".equalsIgnoreCase(booking.getBookingStatus())) {
                futureBookings.addLast(booking);
            }
        }
        System.out.println("\n--- FUTURE BOOKINGS: " + date + " " + period.toUpperCase() + " ---");
        printBookingTable(futureBookings, customerService, vehicleService, washService);
    }

    /** Displays only WAITING and SERVING bookings that belong to one customer. */
    public void displayCustomerActiveBookings(String customerId,
            CustomerService customerService, VehicleService vehicleService,
            WashServiceManager washService) {
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("=> Error: Customer not found with ID: " + customerId);
            return;
        }

        MyLinkedList<Booking> customerBookings = new MyLinkedList<>();
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            boolean unfinished = "WAITING".equalsIgnoreCase(booking.getBookingStatus())
                    || "SERVING".equalsIgnoreCase(booking.getBookingStatus());
            if (customer.getId().equalsIgnoreCase(booking.getCustomerId()) && unfinished) {
                customerBookings.addLast(booking);
            }
        }
        System.out.println("\n--- ACTIVE BOOKINGS: " + customer.getName() + " ---");
        printBookingTable(customerBookings, customerService, vehicleService, washService);
    }

    private boolean isValidPeriod(String period) {
        return period != null && ("MORNING".equalsIgnoreCase(period)
                || "AFTERNOON".equalsIgnoreCase(period)
                || "EVENING".equalsIgnoreCase(period));
    }

    private void printCapacity(String period) {
        if (!isValidPeriod(period)) {
            return;
        }
        int mainCapacity = "EVENING".equalsIgnoreCase(period) ? 5 : 10;
        int waitlistCapacity = "EVENING".equalsIgnoreCase(period) ? 2 : 3;
        int usedSlots = bookingQueue.size() + waitlist.size();
        System.out.println(period.toUpperCase() + ": " + usedSlots + "/"
                + (mainCapacity + waitlistCapacity) + " slots");
    }

    private void printBookingTable(MyLinkedList<Booking> bookings,
            CustomerService customerService, VehicleService vehicleService,
            WashServiceManager washService) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        System.out.printf("%-10s %-15s %-20s %-12s %-12s%n",
                "BOOKING ID", "LICENSE PLATE", "SERVICE", "TIER", "STATUS");
        System.out.println("-----------------------------------------------------------------------");
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            Vehicle vehicle = findVehicle(booking.getVehicleId(), vehicleService);
            WashPackage washPackage = washService.findServiceById(booking.getServiceId());
            Customer customer = customerService.findCustomerById(booking.getCustomerId());
            String licensePlate = vehicle == null ? booking.getVehicleId() : vehicle.getLicensePlate();
            String serviceName = washPackage == null ? booking.getServiceId() : washPackage.getName();
            String tier = customer == null ? "UNKNOWN" : customer.getMembershipLevel();
            System.out.printf("%-10s %-15s %-20s %-12s %-12s%n", booking.getBookingId(),
                    licensePlate, serviceName, tier, booking.getBookingStatus());
        }
    }

    private Vehicle findVehicle(String vehicleId, VehicleService vehicleService) {
        for (int i = 0; i < vehicleService.getVehicleList().size(); i++) {
            Vehicle vehicle = vehicleService.getVehicleList().get(i);
            if (vehicleId.equalsIgnoreCase(vehicle.getId())
                    || vehicleId.equalsIgnoreCase(vehicle.getLicensePlate())) {
                return vehicle;
            }
        }
        return null;
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

    /** Checks whether a WAITING booking occupies a main-queue position. */
    public boolean isInMainQueue(String bookingId) {
        return bookingQueue.containsBookingById(bookingId);
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
