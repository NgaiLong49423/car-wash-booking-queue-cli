package service;

import datastructure.MyLinkedList;
import datastructure.MyMap;
import datastructure.MyPriorityQueue;
import datastructure.MyQueue;
import datastructure.MyStack;
import model.Booking;
import model.BookingActionResult;
import model.CompletionRecord;
import model.Customer;
import model.History;
import model.PeriodActivationResult;
import model.Vehicle;
import model.WaitlistEntry;
import model.WashPackage;
import util.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class BookingService {
    private MyQueue<Booking> bookingQueue;
    private MyLinkedList<Booking> bookingList;
    private MyStack<CompletionRecord> completionStack;
    private MyPriorityQueue<WaitlistEntry> waitlist;
    private MyMap<String, Integer> bookingWindows;
    private int highestHistoricalBookingId;

    public BookingService() {
        this.bookingQueue = new MyQueue<>();
        this.bookingList = new MyLinkedList<>();
        this.completionStack = new MyStack<>();
        this.waitlist = new MyPriorityQueue<>();
        this.bookingWindows = new MyMap<>();
        this.highestHistoricalBookingId = 0;
        bookingWindows.put("MEMBER", 7);
        bookingWindows.put("SILVER", 10);
        bookingWindows.put("GOLD", 12);
        bookingWindows.put("PLATINUM", 14);
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
        int usedSlots = bookingQueue.size() + waitlist.size();
        System.out.println(period.toUpperCase() + ": " + usedSlots + "/"
                + (getMainCapacity(period) + getWaitlistCapacity(period)) + " slots");
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

    /**
     * Rebuilds the active period queues from persisted WAITING bookings.
     * Main Queue keeps file/creation order while overflow is restored into the
     * priority-based Waitlist.
     */
    public void rebuildCurrentQueues(String date, String period, CustomerService customerService,
            WashServiceManager washService) {
        allocatePeriod(date, period, customerService, washService);
    }

    /** Allocates all WAITING bookings for one period by tier and creation time. */
    public PeriodActivationResult activatePeriod(String date, String period,
            CustomerService customerService, WashServiceManager washService) {
        if (date == null || !isValidPeriod(period) || customerService == null || washService == null) {
            return PeriodActivationResult.failure("Current date, period, customer, or service data is invalid.");
        }

        PeriodActivationResult allocation = allocatePeriod(date, period, customerService, washService);
        String message = "Activated " + date + " " + period.toUpperCase()
                + ": " + allocation.getMainQueueCount() + " booking(s) in Main Queue, "
                + allocation.getWaitlistCount() + " booking(s) in Waitlist.";
        if (allocation.getOverflowCount() > 0) {
            message += " Warning: " + allocation.getOverflowCount()
                    + " booking(s) exceeded capacity and were not allocated.";
        }
        return new PeriodActivationResult(true, message, allocation.getMainQueueCount(),
                allocation.getWaitlistCount(), allocation.getOverflowCount());
    }

    private PeriodActivationResult allocatePeriod(String date, String period,
            CustomerService customerService, WashServiceManager washService) {
        bookingQueue.clear();
        waitlist.clear();
        if (date == null || !isValidPeriod(period) || customerService == null || washService == null) {
            return PeriodActivationResult.failure("Cannot allocate an invalid service period.");
        }

        MyPriorityQueue<WaitlistEntry> candidates = new MyPriorityQueue<>();
        int overflowCount = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            if (!date.equals(booking.getDate())
                    || !period.equalsIgnoreCase(booking.getPeriod())
                    || !"WAITING".equalsIgnoreCase(booking.getBookingStatus())) {
                continue;
            }
            Customer customer = customerService.findCustomerById(booking.getCustomerId());
            if (customer == null) {
                overflowCount++;
                continue;
            }
            candidates.insert(new WaitlistEntry(booking, getTierPriority(customer)));
        }

        int availableMainSlots = getMainCapacity(period) - countServingBookings(date, period);
        if (availableMainSlots < 0) {
            availableMainSlots = 0;
        }
        int usedMinutes = getOccupiedServiceMinutes(date, period, washService);
        int periodTotalMinutes = getPeriodTotalMinutes(period);
        while (!candidates.isEmpty()) {
            WaitlistEntry candidate = candidates.poll();
            Booking booking = candidate.getBooking();
            int duration = getServiceDuration(booking, washService);
            boolean fitsMainQueue = bookingQueue.size() < availableMainSlots
                    && duration > 0 && usedMinutes + duration <= periodTotalMinutes;
            if (fitsMainQueue) {
                bookingQueue.enqueue(booking);
                usedMinutes += duration;
            } else if (waitlist.size() < getWaitlistCapacity(period)) {
                waitlist.insert(candidate);
            } else {
                overflowCount++;
            }
        }

        return new PeriodActivationResult(true, "", bookingQueue.size(), waitlist.size(), overflowCount);
    }

    /** Creates and places one booking according to FR-05 through FR-09. */
    public boolean createBooking(String customerId, String vehicleInput, String serviceId,
            String date, String period, CustomerService customerService,
            VehicleService vehicleService, WashServiceManager washService,
            SimulationService simulationService) {
        if (isBlank(customerId) || isBlank(vehicleInput) || isBlank(serviceId) || isBlank(date)) {
            System.out.println("=> Error: Customer, vehicle, service, and booking date are required.");
            return false;
        }
        if (!isValidPeriod(period)) {
            System.out.println("=> Error: Period must be MORNING, AFTERNOON, or EVENING.");
            return false;
        }

        String normalizedCustomerId = customerId.trim().toUpperCase();
        String normalizedServiceId = serviceId.trim().toUpperCase();
        String normalizedDate = date.trim();
        String normalizedPeriod = period.trim().toUpperCase();

        Customer customer = customerService.findCustomerById(normalizedCustomerId);
        if (customer == null) {
            System.out.println("=> Error: Customer not found with ID: " + normalizedCustomerId);
            return false;
        }

        Vehicle vehicle = vehicleService.findVehicleByIdOrLicense(vehicleInput.trim());
        if (vehicle == null) {
            System.out.println("=> Error: Vehicle not found: " + vehicleInput.trim());
            return false;
        }
        if (!normalizedCustomerId.equalsIgnoreCase(vehicle.getCustomerId())) {
            System.out.println("=> Error: Vehicle " + vehicle.getLicensePlate()
                    + " does not belong to customer " + normalizedCustomerId + ".");
            return false;
        }

        WashPackage washPackage = washService.findServiceById(normalizedServiceId);
        if (washPackage == null) {
            System.out.println("=> Error: Service not found with ID: " + normalizedServiceId);
            return false;
        }
        if (!washService.isServiceActive(normalizedServiceId)) {
            System.out.println("=> Error: Service " + normalizedServiceId + " is not active.");
            return false;
        }

        LocalDate bookingDate = parseDate(normalizedDate);
        LocalDate currentDate = parseDate(simulationService.getCurrentDateStr());
        if (bookingDate == null) {
            System.out.println("=> Error: Booking date must be a valid date in YYYY-MM-DD format.");
            return false;
        }
        if (currentDate == null) {
            System.out.println("=> Error: Current simulation date is not configured correctly.");
            return false;
        }
        if (bookingDate.isBefore(currentDate)) {
            System.out.println("=> Error: Booking date cannot be before the current simulation date.");
            return false;
        }

        long daysAhead = ChronoUnit.DAYS.between(currentDate, bookingDate);
        int allowedDays = getBookingWindowDays(customer.getMembershipLevel());
        if (daysAhead > allowedDays) {
            System.out.println("=> Error: " + customer.getMembershipLevel() + " customers can book up to "
                    + allowedDays + " days in advance.");
            return false;
        }

        String currentPeriod = simulationService.getCurrentPeriodStr();
        if (bookingDate.equals(currentDate) && isEarlierPeriod(normalizedPeriod, currentPeriod)) {
            System.out.println("=> Error: The selected period has already passed for the current date.");
            return false;
        }

        boolean activeCurrentPeriod = simulationService.isCurrentPeriodActivated()
                && normalizedDate.equals(simulationService.getCurrentDateStr())
                && currentPeriod != null && normalizedPeriod.equalsIgnoreCase(currentPeriod);
        int totalCapacity = getMainCapacity(normalizedPeriod) + getWaitlistCapacity(normalizedPeriod);
        if (countOpenBookings(normalizedDate, normalizedPeriod) >= totalCapacity) {
            System.out.println("=> Error: " + normalizedPeriod + " on " + normalizedDate
                    + " is full, including its waitlist.");
            return false;
        }
        int reservedMinutes = getReservedPeriodMinutes(normalizedDate, normalizedPeriod, washService);
        if (reservedMinutes + washPackage.getDuration() > getPeriodTotalMinutes(normalizedPeriod)) {
            System.out.println("=> Error: " + normalizedPeriod + " on " + normalizedDate
                    + " does not have enough service time for this booking.");
            return false;
        }

        if (activeCurrentPeriod) {
            rebuildCurrentQueues(normalizedDate, normalizedPeriod, customerService, washService);
        }

        Booking booking = new Booking(generateNextBookingId(), normalizedCustomerId, vehicle.getId(),
                normalizedServiceId, normalizedDate, normalizedPeriod, "WAITING", "UNPAID", "NONE",
                System.currentTimeMillis());

        String location;
        if (activeCurrentPeriod) {
            int occupiedMainSlots = bookingQueue.size() + countServingBookings(normalizedDate, normalizedPeriod);
            int remainingMinutes = getRemainingServiceMinutes(normalizedDate, normalizedPeriod, washService);
            if (occupiedMainSlots < getMainCapacity(normalizedPeriod)
                    && washPackage.getDuration() <= remainingMinutes) {
                bookingQueue.enqueue(booking);
                location = "Main Queue of the active period";
            } else if (waitlist.size() < getWaitlistCapacity(normalizedPeriod)) {
                addToWaitlist(booking, customer);
                location = "Waitlist of the active period";
            } else {
                System.out.println("=> Error: " + normalizedPeriod + " on " + normalizedDate
                        + " is full, including its waitlist.");
                return false;
            }
        } else {
            location = "Future Booking list, awaiting period activation";
        }

        bookingList.addLast(booking);
        FileManager.saveBookings(bookingList);
        System.out.println("=> Booking created successfully. ID: " + booking.getBookingId()
                + ", Status: WAITING, Location: " + location + ".");
        return true;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private LocalDate parseDate(String date) {
        if (isBlank(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int getBookingWindowDays(String membershipLevel) {
        Integer days = membershipLevel == null ? null : bookingWindows.get(membershipLevel.trim().toUpperCase());
        return days == null ? 7 : days;
    }

    private int getMainCapacity(String period) {
        return "EVENING".equalsIgnoreCase(period) ? 5 : 10;
    }

    private int getWaitlistCapacity(String period) {
        return "EVENING".equalsIgnoreCase(period) ? 2 : 3;
    }

    public int getPeriodTotalMinutes(String period) {
        if ("MORNING".equalsIgnoreCase(period)) return 300;
        if ("AFTERNOON".equalsIgnoreCase(period)) return 240;
        if ("EVENING".equalsIgnoreCase(period)) return 180;
        return 0;
    }

    /** Returns remaining minutes after COMPLETED, SERVING, and Main Queue bookings. */
    public int getRemainingServiceMinutes(String date, String period,
            WashServiceManager washService) {
        int remaining = getPeriodTotalMinutes(period)
                - getOccupiedServiceMinutes(date, period, washService);
        return Math.max(remaining, 0);
    }

    private int getOccupiedServiceMinutes(String date, String period,
            WashServiceManager washService) {
        int usedMinutes = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && occupiesServiceTime(booking)) {
                usedMinutes += getServiceDuration(booking, washService);
            }
        }
        return usedMinutes;
    }

    private int getReservedPeriodMinutes(String date, String period,
            WashServiceManager washService) {
        int reservedMinutes = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            String status = booking.getBookingStatus();
            boolean reservesTime = "WAITING".equalsIgnoreCase(status)
                    || "SERVING".equalsIgnoreCase(status)
                    || "COMPLETED".equalsIgnoreCase(status);
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && reservesTime) {
                reservedMinutes += getServiceDuration(booking, washService);
            }
        }
        return reservedMinutes;
    }

    private boolean occupiesServiceTime(Booking booking) {
        String status = booking.getBookingStatus();
        return "COMPLETED".equalsIgnoreCase(status)
                || "SERVING".equalsIgnoreCase(status)
                || ("WAITING".equalsIgnoreCase(status)
                && isInMainQueue(booking.getBookingId()));
    }

    private int getServiceDuration(Booking booking, WashServiceManager washService) {
        if (booking == null || washService == null) return 0;
        WashPackage washPackage = washService.findServiceById(booking.getServiceId());
        return washPackage == null ? 0 : washPackage.getDuration();
    }

    private int countOpenBookings(String date, String period) {
        int count = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            String status = booking.getBookingStatus();
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && ("WAITING".equalsIgnoreCase(status) || "SERVING".equalsIgnoreCase(status))) {
                count++;
            }
        }
        return count;
    }

    private int countServingBookings(String date, String period) {
        int count = 0;
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            if (date.equals(booking.getDate()) && period.equalsIgnoreCase(booking.getPeriod())
                    && "SERVING".equalsIgnoreCase(booking.getBookingStatus())) {
                count++;
            }
        }
        return count;
    }

    private boolean isEarlierPeriod(String requestedPeriod, String currentPeriod) {
        return currentPeriod != null && periodOrder(requestedPeriod) < periodOrder(currentPeriod);
    }

    private int periodOrder(String period) {
        if ("MORNING".equalsIgnoreCase(period)) return 1;
        if ("AFTERNOON".equalsIgnoreCase(period)) return 2;
        if ("EVENING".equalsIgnoreCase(period)) return 3;
        return Integer.MAX_VALUE;
    }

    private String generateNextBookingId() {
        int maxId = highestHistoricalBookingId;
        for (int i = 0; i < bookingList.size(); i++) {
            String id = bookingList.get(i).getBookingId();
            int numericId = parseBookingId(id);
            if (numericId > maxId) {
                maxId = numericId;
            }
        }
        return String.format("B%03d", maxId + 1);
    }

    private int parseBookingId(String id) {
        if (id == null || id.length() < 2 || !id.toUpperCase().startsWith("B")) {
            return 0;
        }
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    /** Starts the first WAITING booking in Main Queue while enforcing one SERVING booking. */
    public BookingActionResult processNextBooking() {
        Booking currentServing = findServingBooking();
        if (currentServing != null) {
            return BookingActionResult.failure("Booking " + currentServing.getBookingId()
                    + " is already being served. Complete or cancel it before processing another booking.");
        }
        if (bookingQueue.isEmpty()) {
            return BookingActionResult.failure("There is no waiting booking in Main Queue to process.");
        }
        Booking nextToWash = bookingQueue.peek();
        if (nextToWash == null || !"WAITING".equalsIgnoreCase(nextToWash.getBookingStatus())) {
            return BookingActionResult.failure("Main Queue contains invalid booking state data.");
        }
        bookingQueue.dequeue();
        nextToWash.setBookingStatus("SERVING");
        FileManager.saveBookings(bookingList);
        return new BookingActionResult(true, "Now serving booking " + nextToWash.getBookingId() + ".",
                nextToWash, 0.0);
    }

    /** Confirms CASH or BANKING payment for the single SERVING booking. */
    public BookingActionResult confirmPayment(String paymentMethod, WashServiceManager washService) {
        if (!"CASH".equalsIgnoreCase(paymentMethod) && !"BANKING".equalsIgnoreCase(paymentMethod)) {
            return BookingActionResult.failure("Payment method must be CASH or BANKING.");
        }

        Booking servingBooking = findServingBooking();
        if (servingBooking == null) {
            return BookingActionResult.failure("There is no SERVING booking to pay.");
        }
        if ("PAID".equalsIgnoreCase(servingBooking.getPaymentStatus())) {
            return BookingActionResult.failure("Booking " + servingBooking.getBookingId()
                    + " has already been paid.");
        }

        WashPackage washPackage = washService.findServiceById(servingBooking.getServiceId());
        if (washPackage == null) {
            return BookingActionResult.failure("The booking references a missing wash service.");
        }

        String normalizedMethod = paymentMethod.toUpperCase();
        servingBooking.setPaymentStatus("PAID");
        servingBooking.setPaymentMethod(normalizedMethod);
        FileManager.saveBookings(bookingList);
        return new BookingActionResult(true, "Payment confirmed for booking "
                + servingBooking.getBookingId() + " via " + normalizedMethod + ".",
                servingBooking, washPackage.getPrice());
    }

    public boolean displayServingPaymentDetails(CustomerService customerService,
            VehicleService vehicleService, WashServiceManager washService) {
        Booking booking = findServingBooking();
        if (booking == null) {
            System.out.println("=> There is no SERVING booking to pay.");
            return false;
        }
        Customer customer = customerService.findCustomerById(booking.getCustomerId());
        Vehicle vehicle = findVehicle(booking.getVehicleId(), vehicleService);
        WashPackage washPackage = washService.findServiceById(booking.getServiceId());
        if (customer == null || vehicle == null || washPackage == null) {
            System.out.println("=> The SERVING booking references missing customer, vehicle, or service data.");
            return false;
        }

        System.out.println("\n--- PAYMENT DETAILS ---");
        System.out.println("Booking  : " + booking.getBookingId());
        System.out.println("Customer : " + customer.getName() + " (" + customer.getId() + ")");
        System.out.println("Vehicle  : " + vehicle.getLicensePlate());
        System.out.println("Service  : " + washPackage.getName());
        System.out.printf("Amount   : %.0f VND%n", washPackage.getPrice());
        return true;
    }

    public Booking findServingBooking() {
        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            if ("SERVING".equalsIgnoreCase(booking.getBookingStatus())) {
                return booking;
            }
        }
        return null;
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

    /** Keeps newly generated booking IDs unique across active bookings and history. */
    public void synchronizeBookingSequence(MyLinkedList<History> historyList) {
        highestHistoricalBookingId = 0;
        if (historyList == null) {
            return;
        }
        for (int i = 0; i < historyList.size(); i++) {
            int numericId = parseBookingId(historyList.get(i).getBookingId());
            if (numericId > highestHistoricalBookingId) {
                highestHistoricalBookingId = numericId;
            }
        }
    }

    public MyPriorityQueue<WaitlistEntry> getWaitlist() {
        return waitlist;
    }

    public void clearCurrentQueues() {
        bookingQueue.clear();
        waitlist.clear();
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

    public void recordCompletion(Booking completedBooking, MyLinkedList<Booking> promotedBookings) {
        completionStack.push(new CompletionRecord(completedBooking, promotedBookings));
    }

    /**
     * Promotes every waitlist booking that fits the remaining time and physical
     * Main Queue slots. Entries that do not fit are restored with their priority.
     */
    public MyLinkedList<Booking> promoteFittingWaitlistBookings(String date, String period,
            WashServiceManager washService) {
        MyLinkedList<Booking> promotedBookings = new MyLinkedList<>();
        MyLinkedList<WaitlistEntry> entries = drainWaitlist();
        MyLinkedList<WaitlistEntry> retainedEntries = new MyLinkedList<>();
        int remainingMinutes = getRemainingServiceMinutes(date, period, washService);
        int availableSlots = getMainCapacity(period)
                - countServingBookings(date, period) - bookingQueue.size();

        for (int i = 0; i < entries.size(); i++) {
            WaitlistEntry entry = entries.get(i);
            Booking booking = entry.getBooking();
            int duration = getServiceDuration(booking, washService);
            boolean matchesPeriod = date.equals(booking.getDate())
                    && period.equalsIgnoreCase(booking.getPeriod());
            if (matchesPeriod && availableSlots > 0 && duration > 0
                    && duration <= remainingMinutes) {
                bookingQueue.enqueue(booking);
                promotedBookings.addLast(booking);
                remainingMinutes -= duration;
                availableSlots--;
            } else {
                retainedEntries.addLast(entry);
            }
        }
        restoreWaitlist(retainedEntries);
        return promotedBookings;
    }

    public CompletionRecord peekLastCompletion() {
        return completionStack.peek();
    }

    public CompletionRecord popLastCompletion() {
        return completionStack.pop();
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
