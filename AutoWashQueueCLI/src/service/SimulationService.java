package service;

import datastructure.MyLinkedList;
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
        int size = periods.size();
        for (int i = 0; i < size; i++) {
            Period p = periods.get(i);
            if ("ACTIVATED".equalsIgnoreCase(p.getStatus())) {
                return p.getPeriodName();
            }
        }
        return null;
    }

    public void setCurrentDate(String dateStr) {
        try {
            // Validate date format YYYY-MM-DD
            LocalDate.parse(dateStr);
            
            // Apply to all periods
            int size = periods.size();
            for (int i = 0; i < size; i++) {
                periods.get(i).setDate(dateStr);
            }
            
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

        boolean found = false;
        int size = periods.size();
        for (int i = 0; i < size; i++) {
            Period p = periods.get(i);
            if (p.getPeriodName().equalsIgnoreCase(periodName)) {
                p.setStatus("ACTIVATED");
                found = true;
            } else {
                p.setStatus("NOT_ACTIVATED");
            }
        }

        if (found) {
            System.out.println("=> Successfully set current period to: " + periodName);
            FileManager.savePeriods(periods); // Auto-save
        } else {
            // If the list was somehow missing the periods, we can add them here, but we assume periods.txt is pre-populated
            System.out.println("=> Error: Period data is missing or corrupted. Cannot set period.");
        }
    }
}
