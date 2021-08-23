/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chris
 */
public class Customer {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private int customerId, addressId, active;
    private String customerName,
            createDate,
            createdBy,
            lastUpdate,
            lastUpdateBy;

    public Customer(int customerId, int addressId, int active, String customerName, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.active = active;
        this.customerName = customerName;
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.createdBy = createdBy;
        this.lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.lastUpdateBy = lastUpdateBy;
    }

    public Customer(int customerId, String customerName, int addressId, int active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }

    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static void addCustomer(Customer customer) {
        allCustomers.add(customer);
    }

    public static void deleteCustomer(Customer customer) {
        allCustomers.remove(customer);
    }

    public static void clearCustomers() {
        allCustomers.clear(); 
    }
}
