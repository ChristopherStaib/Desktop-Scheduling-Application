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
public class City {
    private int cityId, countryId;
    private String city,
                   createDate,
                   createdBy,
                   lastUpdate,
                   lastUpdateBy;

    public City(int cityId, int countryId, String city, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.cityId = cityId;
        this.countryId = countryId;
        this.city = city;
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.createdBy = createdBy;
        this.lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public City(int cityId, String city, int countryId) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
