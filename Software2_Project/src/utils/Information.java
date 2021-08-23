/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author chris
 */
public class Information {

    private static String username = "";
    private static String password = "";
    private static String addressId = "";
    private static String cityId = "";
    private static String city = "";
    private static String countryId = "";
    private static String userId = "";

    public static String getAddressId() {
        return addressId;
    }

    public static void setAddressId(String addressId) {
        Information.addressId = addressId;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        Information.userId = userId;
    }

    public static void setCountryId(String countryId) {
        Information.countryId = countryId;
    }

    public static void setCity(String city) {
        Information.city = city;
    }

    public static void setCityId(String cityId) {
        Information.cityId = cityId;
    }

    public static void setUsername(String username) {
        Information.username = username;
    }

    public static void setPassword(String password) {
        Information.password = password;
    }

    public static void setAddressID(String addressId) {
        Information.addressId = addressId;
    }

    public static String getUsername() {
        return Information.username;
    }

    public static String getPassword() {
        return Information.password;
    }

    public static String getAddressID() {
        return Information.addressId;
    }

    public static String getCityId() {
        return Information.cityId;
    }

    public static String getCity() {
        return city;
    }

    public static String getCountryId() {
        return countryId;
    }
}
