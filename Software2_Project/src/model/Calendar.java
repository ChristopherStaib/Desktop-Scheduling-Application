/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public class Calendar {

    public static ObservableList<Calendar> allCalendar = FXCollections.observableArrayList();
    private int appointmentId, customerId, userId;
    private String customerName, userName, type, start, end, amount;

    public Calendar(int appointmentId, int customerId, int userId, String customerName, String userName, String type, String start, String end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.customerName = customerName;
        this.userName = userName;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public Calendar(String amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static ObservableList<Calendar> getCalendar() {
        return allCalendar;
    }
    public static void addToCalendar(Calendar calendar) {
       allCalendar.add(calendar);
    }
    public static void clearCalendar() {
        allCalendar.clear();
    }
    public static ObservableList<Calendar> lookupUserCalendar(String searchUser) {
        ObservableList<Calendar> searchedList = FXCollections.observableArrayList();
        for (Calendar calendar : allCalendar) {
            if(calendar.getUserName().equals(searchUser)) {
                searchedList.add(calendar);
            }
        }
        return searchedList;
    }
    public static ObservableList<Calendar> lookupCustCalendar(String searchCust) {
        ObservableList<Calendar> searchedList = FXCollections.observableArrayList();
        for (Calendar calendar : allCalendar) {
            if(calendar.getCustomerName().equals(searchCust)) {
                searchedList.add(calendar);
            }
        }
        return searchedList;
    }
    public static ObservableList<Calendar> addAllToCalendar(ObservableList<Calendar> newList) {
        allCalendar = newList;
        return allCalendar;
    }
}
