package service;

import datastructure.MyLinkedList;
import model.Booking;
import model.CompletionRecord;
import model.Customer;
import model.History;
import model.UndoResult;
import util.FileManager;

/** Implements FR-21: undo the latest successful booking completion. */
public class UndoService {
    private final BookingService bookingService;
    private final CompletionService completionService;
    private final CustomerService customerService;
    private final MyLinkedList<History> historyList;

    public UndoService(BookingService bookingService, CompletionService completionService,
            CustomerService customerService, MyLinkedList<History> historyList) {
        this.bookingService = bookingService;
        this.completionService = completionService;
        this.customerService = customerService;
        this.historyList = historyList;
    }

    public UndoResult undoLastCompletion() {
        CompletionRecord record = bookingService.peekLastCompletion();
        if (record == null) {
            return UndoResult.failure("There is no completed booking available to undo.");
        }

        Booking completedBooking = record.getCompletedBooking();
        if (completedBooking == null || !"COMPLETED".equalsIgnoreCase(completedBooking.getBookingStatus())) {
            return UndoResult.failure("The latest completion record is no longer safe to undo.");
        }
        Customer completedCustomer = customerService.findCustomerById(completedBooking.getCustomerId());
        int historyIndex = findHistoryIndex(completedBooking.getBookingId());
        if (completedCustomer == null || historyIndex < 0) {
            return UndoResult.failure("The latest completion is missing customer or history data.");
        }

        MyLinkedList<Booking> promotedBookings = record.getPromotedBookings();
        for (int i = 0; i < promotedBookings.size(); i++) {
            Booking promotedBooking = promotedBookings.get(i);
            Customer promotedCustomer = customerService.findCustomerById(promotedBooking.getCustomerId());
            if (promotedCustomer == null
                    || !"WAITING".equalsIgnoreCase(promotedBooking.getBookingStatus())
                    || !bookingService.isInMainQueue(promotedBooking.getBookingId())) {
                return UndoResult.failure("The promoted booking has changed and cannot be returned safely.");
            }
        }

        bookingService.popLastCompletion();
        completedBooking.setBookingStatus("SERVING");
        historyList.remove(historyIndex);

        MyLinkedList<Booking> returnedBookings = new MyLinkedList<Booking>();
        for (int i = 0; i < promotedBookings.size(); i++) {
            Booking promotedBooking = promotedBookings.get(i);
            Customer promotedCustomer = customerService.findCustomerById(promotedBooking.getCustomerId());
            bookingService.getBookingQueue().removeBookingById(promotedBooking.getBookingId());
            bookingService.addToWaitlist(promotedBooking, promotedCustomer);
            returnedBookings.addLast(promotedBooking);
        }

        completionService.recalculateLoyalty(completedCustomer);
        FileManager.saveBookings(bookingService.getBookingList());
        FileManager.saveHistories(historyList);
        FileManager.saveCustomers(customerService.getCustomerList());

        String message = "Restored booking " + completedBooking.getBookingId()
                + " to SERVING with payment status " + completedBooking.getPaymentStatus() + ".";
        return new UndoResult(true, message, completedBooking, returnedBookings);
    }

    private int findHistoryIndex(String bookingId) {
        for (int i = 0; i < historyList.size(); i++) {
            if (bookingId.equalsIgnoreCase(historyList.get(i).getBookingId())) {
                return i;
            }
        }
        return -1;
    }
}
