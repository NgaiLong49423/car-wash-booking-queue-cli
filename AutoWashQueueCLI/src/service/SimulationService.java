package service;

import datastructure.MyLinkedList;
import model.PeriodActivationResult;
import model.Period;
import util.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SimulationService {
    private MyLinkedList<Period> periods;

    public SimulationService(MyLinkedList<Period> periods) {
        this.periods = periods;
    }

    public void displayCurrentTime() {
        System.out.println("\n--- CURRENT SIMULATION TIME ---");
        if (periods.isEmpty()) {
            System.out.println("No simulation time configured. Please set the date and period.");
            System.out.println("-------------------------------");
            return;
        }
        
        String date = getCurrentDateStr();
        String period = getCurrentPeriodStr();
        System.out.println("Date   : " + (date != null ? date : "NOT SET"));
        System.out.println("Period : " + (period != null ? period : "NOT SET"));
        System.out.println("Status : " + (isCurrentPeriodActivated() ? "ACTIVATED" : "NOT_ACTIVATED"));
        System.out.println("-------------------------------");
    }

    public String getCurrentDateStr() {
        if (!periods.isEmpty()) {
            // All periods should have the same date
            return periods.get(0).getDate();
        }
        return null;
    }

    public String getCurrentPeriodStr() {
        return periods.isEmpty() ? null : periods.get(0).getPeriodName();
    }

    public void setCurrentDate(String dateStr) {
        try {
            // Validate date format YYYY-MM-DD
            LocalDate.parse(dateStr);
            
            String currentPeriod = getCurrentPeriodStr();
            if (currentPeriod == null) {
                currentPeriod = "MORNING";
            }
            ensureDatePeriods(dateStr);
            moveCurrentPeriodToFront(dateStr, currentPeriod);
            
            System.out.println("=> Successfully set current date to: " + dateStr);
            FileManager.savePeriods(periods); // Auto-save
        } catch (DateTimeParseException e) {
            System.out.println("=> Error: Invalid date format. Please use YYYY-MM-DD (e.g., 2026-07-10) and ensure the date is valid.");
        }
    }

    public void setCurrentPeriod(int choice) {
        String periodName = "";
        switch (choice) {
            case 1: periodName = "MORNING"; break;
            case 2: periodName = "AFTERNOON"; break;
            case 3: periodName = "EVENING"; break;
            default: 
                System.out.println("=> Error: Invalid period choice!");
                return;
        }

        String currentDate = getCurrentDateStr();
        if (currentDate != null) {
            ensureDatePeriods(currentDate);
            moveCurrentPeriodToFront(currentDate, periodName);
            System.out.println("=> Successfully set current period to: " + periodName);
            FileManager.savePeriods(periods); // Auto-save
        } else {
            System.out.println("=> Error: Set the current simulation date before selecting a period.");
        }
    }

    public boolean isCurrentPeriodActivated() {
        return isPeriodActivated(getCurrentDateStr(), getCurrentPeriodStr());
    }

    public boolean isPeriodActivated(String date, String periodName) {
        Period period = findPeriod(date, periodName);
        return period != null && period.isActive();
    }

    public PeriodActivationResult activateCurrentPeriod(BookingService bookingService,
            CustomerService customerService, WashServiceManager washService) {
        String currentDate = getCurrentDateStr();
        String currentPeriod = getCurrentPeriodStr();
        if (currentDate == null || currentPeriod == null) {
            return PeriodActivationResult.failure("Current simulation date and period must be configured first.");
        }

        Period period = findPeriod(currentDate, currentPeriod);
        if (period == null) {
            return PeriodActivationResult.failure("Current period data is missing or corrupted.");
        }
        if (period.isActive()) {
            return PeriodActivationResult.failure(currentDate + " " + currentPeriod
                    + " has already been activated.");
        }

        PeriodActivationResult result = bookingService.activatePeriod(
                currentDate, currentPeriod, customerService, washService);
        if (!result.isSuccessful()) {
            return result;
        }

        period.setStatus("ACTIVATED");
        FileManager.savePeriods(periods);
        FileManager.saveBookings(bookingService.getBookingList());
        return result;
    }

    private Period findPeriod(String date, String periodName) {
        if (date == null || periodName == null) {
            return null;
        }
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            if (date.equals(period.getDate()) && periodName.equalsIgnoreCase(period.getPeriodName())) {
                return period;
            }
        }
        return null;
    }

    private void ensureDatePeriods(String date) {
        ensurePeriod(date, "MORNING");
        ensurePeriod(date, "AFTERNOON");
        ensurePeriod(date, "EVENING");
    }

    private void ensurePeriod(String date, String periodName) {
        if (findPeriod(date, periodName) == null) {
            periods.addLast(new Period(date, periodName, "NOT_ACTIVATED"));
        }
    }

    /**
     * The first record in periods.txt is the persisted simulation cursor. The
     * status remains independent and records whether that date/period was activated.
     */
    private void moveCurrentPeriodToFront(String date, String periodName) {
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            if (date.equals(period.getDate()) && periodName.equalsIgnoreCase(period.getPeriodName())) {
                periods.remove(i);
                periods.addFirst(period);
                return;
            }
        }
    }
}
