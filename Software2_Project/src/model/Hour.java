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
public class Hour {

    private String hour;
    
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Hour(String hour) {
        this.hour = hour;
    }
    public String toString() {
        return hour;
    }
}
