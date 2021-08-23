/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;
import utils.Information;
import utils.InvalidCustomerException;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class NewCustomerScreenController implements Initializable {

    //textfields
    @FXML
    private TextField addressIdTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField address2Textfield;
    @FXML
    private TextField cityIdTextfield;
    @FXML
    private TextField postalCodeTextfield;
    @FXML
    private TextField phoneTextfield;
    @FXML
    private TextField customerIDTextfield;
    @FXML
    private TextField customerNameTextfield;
    @FXML
    private TextField cityTextfield;
    @FXML
    private TextField countryTextfield;

    //labels
    @FXML
    private Label addressIDLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label address2Label;
    @FXML
    private Label cityIdLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label newCustTitle;
    @FXML
    private Label customerIDLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;

    //buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button exitButton;

    ResourceBundle rb;

    /**
     * Pass user information to Screen if exists
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //try {
        this.rb = rb;

        //setPromptText
        addressIdTextfield.setPromptText(rb.getString("AddressID"));
        addressTextfield.setPromptText(rb.getString("Address"));
        address2Textfield.setPromptText(rb.getString("Address2"));
        cityIdTextfield.setPromptText(rb.getString("CityID"));
        postalCodeTextfield.setPromptText(rb.getString("PostalCode"));
        phoneTextfield.setPromptText(rb.getString("Phone"));
        customerIDTextfield.setPromptText("customerID");
        customerNameTextfield.setPromptText("customerName");
        //setText - Internationalization
        addressIDLabel.setText(rb.getString("AddressID"));
        addressLabel.setText(rb.getString("Address"));
        address2Label.setText(rb.getString("Address2"));
        cityIdLabel.setText(rb.getString("CityID"));
        postalCodeLabel.setText(rb.getString("PostalCode"));
        phoneLabel.setText(rb.getString("Phone"));
        saveButton.setText(rb.getString("Save"));
        exitButton.setText(rb.getString("Exit"));
        customerIDLabel.setText("customerID");
        customerNameLabel.setText("customerName"); 
    }

    @FXML
    private void saveChanges(ActionEvent event) throws SQLException, InvalidCustomerException {
        //null check
        if (!customerNameTextfield.getText().isEmpty()
                && !addressTextfield.getText().isEmpty()
                && !cityTextfield.getText().isEmpty()
                && !postalCodeTextfield.getText().isEmpty()
                && !countryTextfield.getText().isEmpty()
                && !phoneTextfield.getText().isEmpty()) {
            Connection conn = DBConnection.startConnection();

            //sql statements
            String insertCustomerStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,?,?)";

            String selectCountryStatement = "SELECT countryId FROM country WHERE country = ?";
            String selectLastCountryIDStatement = "SELECT countryId FROM country ORDER BY countryId DESC LIMIT 1";
            String insertCountryStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?)";

            String selectCityStatement = "SELECT cityId FROM city WHERE city = ?";
            String selectLastCityIDStatement = "SELECT cityId FROM city ORDER BY cityId DESC LIMIT 1";
            String insertCityStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,?)";

            String selectLastAddressIDStatement = "SELECT addressId FROM address ORDER BY addressId DESC LIMIT 1";
            String insertAddressStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,?,?,?,?)";

            //global variables
            String createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
            String createdBy = Information.getUsername();
            String lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
            String lastUpdateBy = Information.getUsername();

            //country variables 
            String country = countryTextfield.getText();

            //check if match for country exists      
            DBQuery.setPreparedStatement(conn, selectCountryStatement);
            PreparedStatement selectCountry = DBQuery.getPreparedStatement();

            selectCountry.setString(1, country);

            selectCountry.execute();

            ResultSet countryRs = selectCountry.getResultSet();

            /*
        Logic here: if country exists in country table, take countryId and use that for city record,
        if not create a new country record and save the countryId for the city record
             */
            if (countryRs.next()) {
                String countryId = countryRs.getString("countryId");
                Information.setCountryId(countryId);
            } else {
                //insert country record
                DBQuery.setPreparedStatement(conn, insertCountryStatement);
                PreparedStatement insertCountry = DBQuery.getPreparedStatement();

                //setup ps - execute
                insertCountry.setString(1, country);
                insertCountry.setString(2, createDate);
                insertCountry.setString(3, createdBy);
                insertCountry.setString(4, lastUpdate);
                insertCountry.setString(5, lastUpdateBy);

                insertCountry.execute();

                DBQuery.setPreparedStatement(conn, selectLastCountryIDStatement);
                PreparedStatement selectLastCountry = DBQuery.getPreparedStatement();

                selectLastCountry.execute();

                ResultSet lastCountryRs = selectLastCountry.getResultSet();

                while (lastCountryRs.next()) {
                    int countryIdInt = Integer.parseInt(countryRs.getString("countryID"));
                    countryIdInt = countryIdInt + 1;
                    String countryID = String.valueOf(countryIdInt);
                    Information.setCountryId(countryID);

                }
            }

            //city variables
            String city = cityTextfield.getText();
            String countryId = Information.getCountryId();

            //check if city record exists
            DBQuery.setPreparedStatement(conn, selectCityStatement);
            PreparedStatement selectCity = DBQuery.getPreparedStatement();

            selectCity.setString(1, city);

            selectCity.execute();

            ResultSet cityRs = selectCity.getResultSet();

            /*
        Logic here: if city exists in city table, take cityId and use that for address record,
        if not create a new city record and save the cityId for the address record
             */
            if (cityRs.next()) {
                String cityId = cityRs.getString("cityId");
                Information.setCityId(cityId);
            } else {
                //insert city record
                DBQuery.setPreparedStatement(conn, insertCityStatement);
                PreparedStatement insertCity = DBQuery.getPreparedStatement();

                //setup ps statement - city
                insertCity.setString(1, city);
                insertCity.setString(2, countryId);
                insertCity.setString(3, createDate);
                insertCity.setString(4, createdBy);
                insertCity.setString(5, lastUpdate);
                insertCity.setString(6, lastUpdateBy);

                insertCity.execute();

                DBQuery.setPreparedStatement(conn, selectLastCityIDStatement);
                PreparedStatement selectLastCityId = DBQuery.getPreparedStatement();

                selectLastCityId.execute();

                ResultSet lastCityIdRs = selectLastCityId.getResultSet();

                while (lastCityIdRs.next()) {
                    String cityId = lastCityIdRs.getString("cityId");
                    Information.setCityId(cityId);
                }
            }

            //create address record -- check if exists
            //insertAddress preparation and execution
            DBQuery.setPreparedStatement(conn, insertAddressStatement);
            PreparedStatement psAddress = DBQuery.getPreparedStatement();

            //variables for preparedStatement
            String address = addressTextfield.getText();
            String address2 = address2Textfield.getText();
            String cityId = Information.getCityId();
            String postalCode = postalCodeTextfield.getText();
            String phone = phoneTextfield.getText();

            psAddress.setString(1, address);
            psAddress.setString(2, address2);
            psAddress.setString(3, cityId);
            psAddress.setString(4, postalCode);
            psAddress.setString(5, phone);
            psAddress.setString(6, createDate);
            psAddress.setString(7, createdBy);
            psAddress.setString(8, lastUpdate);
            psAddress.setString(9, lastUpdateBy);

            psAddress.execute();

            //select statement setup and execution
            DBQuery.setPreparedStatement(conn, selectLastAddressIDStatement);
            PreparedStatement psSelect = DBQuery.getPreparedStatement();

            psSelect.execute();
            ResultSet rs = psSelect.getResultSet();

            while (rs.next()) {
                String addressId = rs.getString("addressId");
                Information.setAddressID(addressId);
            }

            //create customer record -- check if exists
            //insert customer setup and execution
            DBQuery.setPreparedStatement(conn, insertCustomerStatement);
            PreparedStatement psCustomer = DBQuery.getPreparedStatement();

            String customerName = customerNameTextfield.getText();
            String active = "1";
            String addressId = Information.getAddressID();

            psCustomer.setString(1, customerName);
            psCustomer.setString(2, addressId);
            psCustomer.setString(3, active);
            psCustomer.setString(4, createDate);
            psCustomer.setString(5, createdBy);
            psCustomer.setString(6, lastUpdate);
            psCustomer.setString(7, lastUpdateBy);

            psCustomer.execute();

            //close connection to DB
            DBConnection.closeConnection();

            //close window
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There are empty fields, please fill out all fields to continue.");
            alert.show();
            throw new InvalidCustomerException("Empty fields are present for customer. All fields required to continue.");
        }
    }

    @FXML
    private void exitScreen(ActionEvent event) {
        /*
            Alert user prompting if they are sure they want to exit, if yes then exits. Translates to 3 languages : en/es/fr
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("Alert"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Closing New Customer Screen...");
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
    }

}
