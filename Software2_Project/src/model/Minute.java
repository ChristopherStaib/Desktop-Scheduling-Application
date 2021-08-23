/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author chris
 */
public class Minute {

    private String minute;

    public Minute(String minute) {
        this.minute = minute;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String toString() {
        return minute;
    }
}
