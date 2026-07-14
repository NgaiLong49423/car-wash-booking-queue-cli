/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import datastructure.MyLinkedList;
import datastructure.MyQueue;
import datastructure.MyStack;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Booking;
import model.Customer;
import model.History;
import model.Vehicle;
import model.WashPackage;
import util.FileManager;
/**
 *
 * @author Admin
 */
public class HistoryService {
    
    public void addHistory(Booking b, Customer c, WashPackage w, Vehicle v, MyLinkedList<History> historyList){
        LocalDateTime ld = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String completeTime = ld.format(formatter);
        History h = new History(b.getBookingId(), b.getCustomerId(), c.getName(), v.getLicensePlate(), w.getName(), completeTime, w.getPrice(), (int)w.getPrice()/1000);
        historyList.addLast(h);
        FileManager.saveHistories(historyList);
    }
    
    public void removeHistory(Booking b, MyLinkedList<History> historyList){
        int size = historyList.size();
        for(int i=0; i<size; i++){
            History h = historyList.get(i);
            if(h.getBookingId().equals(b.getBookingId())){
                historyList.remove(i);
                break;
            }
        }
        FileManager.saveHistories(historyList);
    }
}
