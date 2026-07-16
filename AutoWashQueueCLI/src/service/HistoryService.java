package service;

import datastructure.MyLinkedList;
import model.History;

public class HistoryService {

    public void displayGlobalHistory(MyLinkedList<History> historyList) {
        if (historyList.isEmpty()) {
            System.out.println("No history records found.");
            return;
        }

        System.out.println("\n=============================================== GLOBAL HISTORY REPORT ===============================================");
        System.out.printf("%-15s | %-12s | %-20s | %-15s | %-25s | %-20s | %-12s | %-15s\n", 
                "Booking ID", "Customer ID", "Customer Name", "License Plate", "Service Name", "Completed Time", "Amount Paid", "Loyalty Points");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < historyList.size(); i++) {
            History h = historyList.get(i);
            System.out.printf("%-15s | %-12s | %-20s | %-15s | %-25s | %-20s | %-12.0f | %-15d\n",
                    h.getBookingId(),
                    h.getCustomerId(),
                    h.getCustomerName(),
                    h.getPlateNumber(),
                    h.getServiceName(),
                    h.getCompletedTime(),
                    h.getAmountPaid(),
                    h.getLoyaltyPointsEarned());
        }
        System.out.println("=====================================================================================================================================================");
    }

    public void displayCustomerHistory(MyLinkedList<History> historyList, String customerId) {
        if (historyList.isEmpty()) {
            System.out.println("No history records found.");
            return;
        }

        boolean found = false;
        System.out.println("\n=========================================== CUSTOMER HISTORY REPORT: " + customerId.toUpperCase() + " ===========================================");
        System.out.printf("%-15s | %-12s | %-20s | %-15s | %-25s | %-20s | %-12s | %-15s\n", 
                "Booking ID", "Customer ID", "Customer Name", "License Plate", "Service Name", "Completed Time", "Amount Paid", "Loyalty Points");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < historyList.size(); i++) {
            History h = historyList.get(i);
            if (h.getCustomerId().equalsIgnoreCase(customerId)) {
                found = true;
                System.out.printf("%-15s | %-12s | %-20s | %-15s | %-25s | %-20s | %-12.0f | %-15d\n",
                        h.getBookingId(),
                        h.getCustomerId(),
                        h.getCustomerName(),
                        h.getPlateNumber(),
                        h.getServiceName(),
                        h.getCompletedTime(),
                        h.getAmountPaid(),
                        h.getLoyaltyPointsEarned());
            }
        }

        if (!found) {
            System.out.println("No history records found for Customer ID: " + customerId);
        }
        System.out.println("=====================================================================================================================================================");
    }
}
