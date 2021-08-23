/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author chris
 */
public class Country {
    private int countryId;
    private String country,
                   createDate,
                   createdBy,
                   lastUpdate,
                   lastUpdateBy;

    public Country(int countryId, String country, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.createdBy = createdBy;
        this.lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.lastUpdateBy = lastUpdateBy;
    }
    public Country (int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    
    
}
