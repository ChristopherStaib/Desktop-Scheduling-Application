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
public class User {
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private int userId, active;
    private String userName, 
                   password,
                   createDate,
                   createdBy,
                   lastUpdate, 
                   lastUpdatedBy;

    public User(int userId, int active, String userName, String password, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
        this.active = active;
        this.userName = userName;
        this.password = password;
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.createdBy = createdBy;
        this.lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }
    public static void addUser(User user) {
        allUsers.add(user);
    }
    public static void deleteUser(User user) {
        allUsers.remove(user);
    }
    public static void clearUsers() {
        allUsers.clear();
    }
}
