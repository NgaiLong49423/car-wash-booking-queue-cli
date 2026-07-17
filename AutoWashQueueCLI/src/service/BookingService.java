package service;

import datastructure.MyLinkedList;
import datastructure.MyPriorityQueue;
import datastructure.MyQueue;
import model.Booking;
import model.Customer;
import model.Vehicle;
import model.WashPackage;
import util.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class BookingService {
    private MyQueue<Booking> bookingQueue;
    private MyLinkedList<Booking> bookingList;
    private MyPriorityQueue<Booking> waitlistQueue;
    private boolean autoSave;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
        this.bookingList = new MyLinkedList<>();
        this.waitlistQueue = new MyPriorityQueue<>();
        this.autoSave = true;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    private void saveBookingsIfNeeded() {
        if (autoSave) {
            FileManager.saveBookings(bookingList);
        }
    }

    public void rebuildCurrentQueue(String currentDate, String currentPeriod) {
        rebuildCurrentQueue(currentDate, currentPeriod, null);
    }

    public void rebuildCurrentQueue(String currentDate, String currentPeriod, CustomerService customerService) {
        bookingQueue.clear();
        waitlistQueue.clear();

        if (currentDate == null || currentPeriod == null) {
            return;
        }

        int mainCapacity = getMainCapacity(currentPeriod);
        int waitingCount = 0;

        for (int i = 0; i < bookingList.size(); i++) {
            Booking b = bookingList.get(i);
            boolean sameDate = b.getDate().equalsIgnoreCase(currentDate);
            boolean samePeriod = b.getPeriod().equalsIgnoreCase(currentPeriod);
            boolean waiting = b.getBookingStatus().equalsIgnoreCase("WAITING");

            if (sameDate && samePeriod && waiting) {
                setBookingPriority(b, customerService);
                waitingCount++;
                if (waitingCount <= mainCapacity) {
                    bookingQueue.enqueue(b);
                } else {
                    waitlistQueue.insert(b);
                }
            }
        }
    }

    private void setBookingPriority(Booking booking, CustomerService customerService) {
        if (booking == null || customerService == null) {
            return;
        }

        Customer customer = customerService.findCustomerById(booking.getCustomerId());
        if (customer != null) {
            booking.setPriorityRank(getPriorityRank(customer.getMembershipLevel()));
        }
    }

    public void displayQueue() {
        System.out.println("\n--- HANG DOI RUA XE ---");
        if (bookingQueue.isEmpty()) {
            System.out.println("Hien tai khong co xe nao dang cho.");
            return;
        }
        bookingQueue.display();
        System.out.println("-----------------------");
    }

    public void displayWaitlist() {
        System.out.println("\n--- WAITLIST ---");
        if (waitlistQueue.isEmpty()) {
            System.out.println("Waitlist is empty.");
            return;
        }
        waitlistQueue.display();
        System.out.println("----------------");
    }

    public void addBooking(String bookingId, String licensePlate, String serviceId) {
        Booking newBooking = new Booking(
                bookingId,
                "C000",
                licensePlate,
                serviceId,
                "2026-07-10",
                "MORNING",
                "WAITING",
                "UNPAID",
                "NONE",
                System.currentTimeMillis()
        );
        bookingQueue.enqueue(newBooking);
        bookingList.addLast(newBooking);
        System.out.println("=> Da dat lich! Xe " + licensePlate + " da vao hang doi.");

        saveBookingsIfNeeded();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidPeriod(String period) {
        if (period == null) {
            return false;
        }

        String p = period.trim().toUpperCase();
        return p.equals("MORNING") || p.equals("AFTERNOON") || p.equals("EVENING");
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int getBookingWindowDays(String membershipLevel) {
        if (membershipLevel == null) {
            return 7;
        }

        String level = membershipLevel.trim().toUpperCase();
        if (level.equals("PLATINUM")) {
            return 14;
        }
        if (level.equals("GOLD")) {
            return 12;
        }
        if (level.equals("SILVER")) {
            return 10;
        }

        return 7;
    }

    private int getPriorityRank(String membershipLevel) {
        if (membershipLevel == null) {
            return 1;
        }

        String level = membershipLevel.trim().toUpperCase();
        if (level.equals("PLATINUM")) {
            return 4;
        }
        if (level.equals("GOLD")) {
            return 3;
        }
        if (level.equals("SILVER")) {
            return 2;
        }

        return 1;
    }

    private int getMainCapacity(String period) {
        String p = period.trim().toUpperCase();
        if (p.equals("EVENING")) {
            return 5;
        }

        return 10;
    }

    private int getWaitlistCapacity(String period) {
        String p = period.trim().toUpperCase();
        if (p.equals("EVENING")) {
            return 2;
        }

        return 3;
    }

    private int countWaitingBookings(String date, String period) {
        int count = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking b = bookingList.get(i);
            boolean sameDate = b.getDate().equalsIgnoreCase(date);
            boolean samePeriod = b.getPeriod().equalsIgnoreCase(period);
            boolean waiting = b.getBookingStatus().equalsIgnoreCase("WAITING");

            if (sameDate && samePeriod && waiting) {
                count++;
            }
        }

        return count;
    }

    private int countActiveBookings(String date, String period) {
        int count = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking b = bookingList.get(i);
            boolean sameDate = b.getDate().equalsIgnoreCase(date);
            boolean samePeriod = b.getPeriod().equalsIgnoreCase(period);
            boolean active = b.getBookingStatus().equalsIgnoreCase("WAITING")
                    || b.getBookingStatus().equalsIgnoreCase("SERVING");

            if (sameDate && samePeriod && active) {
                count++;
            }
        }

        return count;
    }

    private String generateNextBookingId() {
        int max = 0;

        for (int i = 0; i < bookingList.size(); i++) {
            String id = bookingList.get(i).getBookingId();
            if (id != null && id.length() > 1 && id.toUpperCase().startsWith("B")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > max) {
                        max = number;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("=> Warning: Skipped invalid booking ID: " + id);
                }
            }
        }

        int next = max + 1;
        if (next < 10) {
            return "B00" + next;
        }
        if (next < 100) {
            return "B0" + next;
        }

        return "B" + next;
    }

    public boolean createBooking(String customerId,
                                 String vehicleInput,
                                 String serviceId,
                                 String date,
                                 String period,
                                 CustomerService customerService,
                                 VehicleService vehicleService,
                                 WashServiceManager washService,
                                 SimulationService simulationService) {
        if (isEmpty(customerId)) {
            System.out.println("=> Error: Customer ID cannot be empty!");
            return false;
        }
        if (isEmpty(vehicleInput)) {
            System.out.println("=> Error: Vehicle ID or license plate cannot be empty!");
            return false;
        }
        if (isEmpty(serviceId)) {
            System.out.println("=> Error: Service ID cannot be empty!");
            return false;
        }
        if (isEmpty(date)) {
            System.out.println("=> Error: Booking date cannot be empty!");
            return false;
        }
        if (!isValidPeriod(period)) {
            System.out.println("=> Error: Period must be MORNING, AFTERNOON, or EVENING!");
            return false;
        }

        String cleanCustomerId = customerId.trim().toUpperCase();
        String cleanServiceId = serviceId.trim().toUpperCase();
        String cleanDate = date.trim();
        String cleanPeriod = period.trim().toUpperCase();

        Customer customer = customerService.findCustomerById(cleanCustomerId);
        if (customer == null) {
            System.out.println("=> Error: Customer not found with ID: " + cleanCustomerId);
            return false;
        }

        Vehicle vehicle = vehicleService.findVehicleByIdOrLicense(vehicleInput);
        if (vehicle == null) {
            System.out.println("=> Error: Vehicle not found: " + vehicleInput);
            return false;
        }

        if (!vehicle.getCustomerId().equalsIgnoreCase(cleanCustomerId)) {
            System.out.println("=> Error: Vehicle does not belong to customer " + cleanCustomerId);
            return false;
        }

        WashPackage service = washService.findServiceById(cleanServiceId);
        if (service == null) {
            System.out.println("=> Error: Service not found with ID: " + cleanServiceId);
            return false;
        }

        if (!washService.isServiceActive(cleanServiceId)) {
            System.out.println("=> Error: Service is not ACTIVE: " + cleanServiceId);
            return false;
        }

        LocalDate bookingDate = parseDate(cleanDate);
        LocalDate currentDate = parseDate(simulationService.getCurrentDateStr());
        if (bookingDate == null) {
            System.out.println("=> Error: Booking date must use format YYYY-MM-DD!");
            return false;
        }
        if (currentDate == null) {
            System.out.println("=> Error: Current simulation date is invalid!");
            return false;
        }
        if (bookingDate.isBefore(currentDate)) {
            System.out.println("=> Error: Booking date cannot be before current simulation date!");
            return false;
        }

        long daysAhead = ChronoUnit.DAYS.between(currentDate, bookingDate);
        int allowedDays = getBookingWindowDays(customer.getMembershipLevel());
        if (daysAhead > allowedDays) {
            System.out.println("=> Error: " + customer.getMembershipLevel() + " can only book within " + allowedDays + " days!");
            return false;
        }

        int totalCapacity = getMainCapacity(cleanPeriod) + getWaitlistCapacity(cleanPeriod);
        int activeCount = countActiveBookings(cleanDate, cleanPeriod);
        if (activeCount >= totalCapacity) {
            System.out.println("=> Error: This period is full including waitlist!");
            return false;
        }

        Booking booking = new Booking(
                generateNextBookingId(),
                cleanCustomerId,
                vehicle.getId(),
                cleanServiceId,
                cleanDate,
                cleanPeriod,
                "WAITING",
                "UNPAID",
                "NONE",
                System.currentTimeMillis()
        );
        booking.setPriorityRank(getPriorityRank(customer.getMembershipLevel()));

        bookingList.addLast(booking);

        boolean isCurrentDate = cleanDate.equalsIgnoreCase(simulationService.getCurrentDateStr());
        boolean isCurrentPeriod = cleanPeriod.equalsIgnoreCase(simulationService.getCurrentPeriodStr());
        int waitingCount = countWaitingBookings(cleanDate, cleanPeriod);

        if (isCurrentDate && isCurrentPeriod && waitingCount <= getMainCapacity(cleanPeriod)) {
            bookingQueue.enqueue(booking);
            System.out.println("=> Booking created in main queue: " + booking.getBookingId());
        } else if (isCurrentDate && isCurrentPeriod) {
            waitlistQueue.insert(booking);
            System.out.println("=> Booking created in waitlist: " + booking.getBookingId());
        } else {
            System.out.println("=> Future booking created: " + booking.getBookingId());
        }

        saveBookingsIfNeeded();
        return true;
    }

    private Booking findServingBooking() {
        for (int i = 0; i < bookingList.size(); i++) {
            Booking b = bookingList.get(i);
            if ("SERVING".equalsIgnoreCase(b.getBookingStatus())) {
                return b;
            }
        }

        return null;
    }

    public void processNextBooking() {
        Booking serving = findServingBooking();
        if (serving != null) {
            System.out.println("=> Error: Booking " + serving.getBookingId() + " is already SERVING!");
            return;
        }

        if (bookingQueue.isEmpty()) {
            System.out.println("=> No waiting booking in main queue.");
            return;
        }

        Booking nextToWash = bookingQueue.dequeue();
        nextToWash.setBookingStatus("SERVING");
        System.out.println("=> Processing booking: " + nextToWash.getBookingId());
        saveBookingsIfNeeded();
    }

    public MyQueue<Booking> getBookingQueue() {
        return bookingQueue;
    }

    public MyPriorityQueue<Booking> getWaitlistQueue() {
        return waitlistQueue;
    }

    public MyLinkedList<Booking> getBookingList() {
        return bookingList;
    }
}
