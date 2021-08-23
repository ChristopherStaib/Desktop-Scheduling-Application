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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.City;
import model.Country;
import utils.DBConnection;
import utils.DBQuery;
import model.Customer;
import utils.Information;
import utils.InvalidCustomerException;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ExistingCustomerScreenController implements Initializable {

    //Textfields
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
    private TextField activeTextfield;
    @FXML
    private TextField countryIdTextfield;
    @FXML
    private TextField customerNameTextfield;
    @FXML
    private TextField cityTextfield;
    @FXML
    private TextField countryTextfield;

    //Labels
    @FXML
    private Label newCustTitle;
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
    private Label customerNameLabel;
    @FXML
    private Label tableMessage;
    @FXML
    private Label cityLabel;
    @FXML
    private Label activeLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label countryIdLabel;

    //Buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button deleteButton;

    //Customer tableview
    @FXML
    private TableView<Customer> existingCustTable;
    @FXML
    private TableColumn<Customer, Integer> custIDColumn;
    @FXML
    private TableColumn<Customer, String> custNameColumn;

    ResourceBundle rb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        try {
            Connection conn = DBConnection.startConnection();

            String selectCustomerStatement = "SELECT * FROM customer";

            DBQuery.setPreparedStatement(conn, selectCustomerStatement);
            PreparedStatement selectCustomer = DBQuery.getPreparedStatement();

            selectCustomer.execute();

            ResultSet rs = selectCustomer.getResultSet();

            while (rs.next()) {
                // create new instances of existing customer in observablelist using Customer Class
                int customerId = Integer.parseInt(rs.getString("customerId"));
                String customerName = rs.getString("customerName");
                int addressId = Integer.parseInt(rs.getString("addressId"));
                int active = Integer.parseInt(rs.getString("active"));

                Customer newCustomer = new Customer(customerId, customerName, addressId, active); //create new instance of customer using overloaded constructor
                Customer.addCustomer(newCustomer); //add to observablelist
            }

            DBConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ExistingCustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set customer tableview
        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        existingCustTable.setItems(Customer.getAllCustomers());

        existingCustTable.getSelectionModel().selectedItemProperty().addListener((obs, notSelected, selectedCustomer) -> { //lambda used to target selecteditem in list and populate textfields
            if (selectedCustomer != null) {
                addressIdTextfield.setText(Integer.toString(selectedCustomer.getAddressId()));
                activeTextfield.setText(Integer.toString(selectedCustomer.getActive()));
                customerNameTextfield.setText(selectedCustomer.getCustomerName());

                try {
                    Connection conn = DBConnection.startConnection();

                    String selectAddressStatement = "SELECT * FROM address WHERE addressId = ?";

                    DBQuery.setPreparedStatement(conn, selectAddressStatement);
                    PreparedStatement selectAddress = DBQuery.getPreparedStatement();

                    String addressId = Integer.toString(selectedCustomer.getAddressId());

                    selectAddress.setString(1, addressId);
                    selectAddress.execute();

                    ResultSet addressRs = selectAddress.getResultSet();

                    while (addressRs.next()) {
                        String address = addressRs.getString("address");
                        String address2 = addressRs.getString("address2");
                        int cityId = Integer.parseInt(addressRs.getString("cityId"));
                        String postalCode = addressRs.getString("postalCode");
                        String phone = addressRs.getString("phone");
                        addressTextfield.setText(address);
                        address2Textfield.setText(address2);
                        cityIdTextfield.setText(Integer.toString(cityId));
                        postalCodeTextfield.setText(postalCode);
                        phoneTextfield.setText(phone);

                    }

                    String selectCityStatement = "SELECT * FROM city WHERE cityId = ?";

                    DBQuery.setPreparedStatement(conn, selectCityStatement);
                    PreparedStatement selectCity = DBQuery.getPreparedStatement();

                    String cityId = cityIdTextfield.getText();
                    selectCity.setString(1, cityId);

                    selectCity.execute();

                    ResultSet cityRs = selectCity.getResultSet();

                    while (cityRs.next()) {
                        String city = cityRs.getString("city");
                        String countryId = cityRs.getString("countryId");
                        cityTextfield.setText(city);
                        countryIdTextfield.setText(countryId);
                    }

                    String selectCountryStatement = "SELECT * FROM country WHERE countryId = ?";

                    DBQuery.setPreparedStatement(conn, selectCountryStatement);
                    PreparedStatement selectCountry = DBQuery.getPreparedStatement();

                    String countryId = countryIdTextfield.getText();
                    selectCountry.setString(1, countryId);

                    selectCountry.execute();

                    ResultSet countryRs = selectCountry.getResultSet();

                    while (countryRs.next()) {
                        String country = countryRs.getString("country");
                        countryTextfield.setText(country);
                    }
                    DBConnection.closeConnection();

                } catch (SQLException ex) {
                    Logger.getLogger(ExistingCustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    @FXML
    private void exitScreen(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("Alert"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Closing Existing Customer Screen...");
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
        //remove all from observablelist to refresh for screen
        Customer.clearCustomers();
    }

    @FXML
    private void saveChanges(ActionEvent event) throws SQLException, InvalidCustomerException {
        Customer selectedCustomer = existingCustTable.getSelectionModel().getSelectedItem();

        if (!customerNameTextfield.getText().isEmpty()
                && !addressTextfield.getText().isEmpty()
                && !cityTextfield.getText().isEmpty()
                && !postalCodeTextfield.getText().isEmpty()
                && !countryTextfield.getText().isEmpty()
                && !phoneTextfield.getText().isEmpty()
                && selectedCustomer != null) {
            Connection conn = DBConnection.startConnection();
            //check if country was changed compared to current country for city - if changed create new country record and update city with country
            String selectCountryStatement = "SELECT countryId, country FROM country WHERE country = ?";

            DBQuery.setPreparedStatement(conn, selectCountryStatement);
            PreparedStatement countryPs = DBQuery.getPreparedStatement();

            String country = countryTextfield.getText();

            countryPs.setString(1, country);

            countryPs.execute();

            ResultSet countryRs = countryPs.getResultSet();

            if (countryRs.next()) {
                //exists - create country instance that stores current countryId from DB
                int newCountryId = Integer.parseInt(countryRs.getString("countryId"));
                Information.setCountryId(Integer.toString(newCountryId));

            } else {
                //does not - create country instance that stores current countryId and countryName from textfields
                String selectLastCountryStatement = "SELECT countryId FROM country ORDER BY countryId DESC LIMIT 1";

                DBQuery.setPreparedStatement(conn, selectLastCountryStatement);
                PreparedStatement lastCountryPs = DBQuery.getPreparedStatement();

                lastCountryPs.execute();

                ResultSet lastCountryRs = lastCountryPs.getResultSet();

                while (lastCountryRs.next()) {
                    //store last countryId + 1 to Information static helper class to use for new country record
                    int countryId = Integer.parseInt(lastCountryRs.getString("countryId"));
                    countryId = countryId + 1;
                    String countryIdString = Integer.toString(countryId);
                    Information.setCountryId(countryIdString);
                }
                int newCountryId = Integer.parseInt(Information.getCountryId());
                String newCountryName = countryTextfield.getText();
                Country newCountry = new Country(newCountryId, newCountryName);

                String insertCountryStatement = "INSERT INTO country(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,?)";
                //insert country record with newCountry instances information

                DBQuery.setPreparedStatement(conn, insertCountryStatement);
                PreparedStatement insertCountryPs = DBQuery.getPreparedStatement();

                String countryIdText = Integer.toString(newCountry.getCountryId());
                String countryText = countryTextfield.getText();
                String createDateText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                String createdByText = Information.getUsername();
                String lastUpdateText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                String lastUpdateByText = Information.getUsername();

                insertCountryPs.setString(1, countryIdText);
                insertCountryPs.setString(2, countryText);
                insertCountryPs.setString(3, createDateText);
                insertCountryPs.setString(4, createdByText);
                insertCountryPs.setString(5, lastUpdateText);
                insertCountryPs.setString(6, lastUpdateByText);

                insertCountryPs.execute();

                if (insertCountryPs.getUpdateCount() > 0) {
                    System.out.println(insertCountryPs.getUpdateCount() + "rows(s) created -- country");
                }

            }

            //check if city was changed compared to current city for address - if changed create new city record and update address record with city
            String selectCityStatement = "SELECT cityId, city FROM city WHERE city = ?";

            DBQuery.setPreparedStatement(conn, selectCityStatement);
            PreparedStatement selectCityPs = DBQuery.getPreparedStatement();

            String city = cityTextfield.getText();

            selectCityPs.setString(1, city);

            selectCityPs.execute();

            ResultSet selectCityRs = selectCityPs.getResultSet();

            if (selectCityRs.next()) {
                //exists - create city instance that stores current cityId and cityName from DB
                int newCityId = Integer.parseInt(selectCityRs.getString("cityId"));
                String cityName = selectCityRs.getString("city");
                int countryId = Integer.parseInt(Information.getCountryId());
                System.out.println("CountryId: " + Information.getCountryId());
                Information.setCityId(Integer.toString(newCityId));
                System.out.println("CityId: " + Information.getCityId());
                City newCity = new City(newCityId, cityName, countryId);

                //if Information.getCountryId() does not match textfield for countryID, update city record to match countryID from Information class
                if (!Information.getCountryId().equals(countryIdTextfield.getText())) {
                    String updateCityStatement = "UPDATE city SET countryId = ? WHERE cityId = ?";

                    DBQuery.setPreparedStatement(conn, updateCityStatement);
                    PreparedStatement updateCityPs = DBQuery.getPreparedStatement();

                    String countryIdText = Information.getCountryId();
                    String cityIdText = Information.getCityId();

                    updateCityPs.setString(1, countryIdText);
                    updateCityPs.setString(2, cityIdText);

                    updateCityPs.execute();

                    System.out.println("Updated City record with new countryId");
                }

            } else {
                //does not - create city instance that stores current cityId and cityName from textfields
                String selectLastCityStatement = "SELECT cityId FROM city ORDER BY cityId DESC LIMIT 1";

                DBQuery.setPreparedStatement(conn, selectLastCityStatement);
                PreparedStatement lastCityPs = DBQuery.getPreparedStatement();

                lastCityPs.execute();

                ResultSet lastCityRs = lastCityPs.getResultSet();

                while (lastCityRs.next()) {
                    int cityId = Integer.parseInt(lastCityRs.getString("cityId"));
                    cityId = cityId + 1;
                    String cityIdString = Integer.toString(cityId);
                    Information.setCityId(cityIdString);
                }
                int newCityId = Integer.parseInt(Information.getCityId());
                String newCityName = cityTextfield.getText();
                int countryId = Integer.parseInt(Information.getCountryId());
                City newCity = new City(newCityId, newCityName, countryId);

                String insertCityStatement = "INSERT INTO city(cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,?,?)";

                DBQuery.setPreparedStatement(conn, insertCityStatement);
                PreparedStatement insertCityPs = DBQuery.getPreparedStatement();

                String cityIdText = Integer.toString(newCity.getCityId());
                String cityText = cityTextfield.getText();

                String countryIdText = Information.getCountryId();
                String createDateText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                String createdByText = Information.getUsername();
                String lastUpdateText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                String lastUpdateByText = Information.getUsername();

                insertCityPs.setString(1, cityIdText);
                insertCityPs.setString(2, cityText);
                insertCityPs.setString(3, countryIdText);
                insertCityPs.setString(4, createDateText);
                insertCityPs.setString(5, createdByText);
                insertCityPs.setString(6, lastUpdateText);
                insertCityPs.setString(7, lastUpdateByText);

                insertCityPs.execute();

                if (insertCityPs.getUpdateCount() > 0) {
                    System.out.println(insertCityPs.getUpdateCount() + "rows(s) created -- city");
                }
            }

            //update address record
            String updateAddressStatement = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";

            DBQuery.setPreparedStatement(conn, updateAddressStatement);
            PreparedStatement addressPs = DBQuery.getPreparedStatement();

            String addressIdText = addressIdTextfield.getText();
            String addressText = addressTextfield.getText();
            String address2Text = addressTextfield.getText();
            String cityIdText = Information.getCityId();
            String postalCodeText = postalCodeTextfield.getText();
            String phoneText = phoneTextfield.getText();
            String lastUpdateText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
            String lastUpdateByText = Information.getUsername();

            addressPs.setString(1, addressText);
            addressPs.setString(2, address2Text);
            addressPs.setString(3, cityIdText);
            addressPs.setString(4, postalCodeText);
            addressPs.setString(5, phoneText);
            addressPs.setString(6, lastUpdateText);
            addressPs.setString(7, lastUpdateByText);
            addressPs.setString(8, addressIdText);

            addressPs.execute();

            //updates customer record from textfields
            String updateCustomerStatement = "UPDATE customer SET customerId = ?, customerName = ?, addressId = ?, active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?";

            DBQuery.setPreparedStatement(conn, updateCustomerStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            String customerId = Integer.toString(selectedCustomer.getCustomerId());
            String customerName = customerNameTextfield.getText();
            String addressId = addressIdTextfield.getText();
            String active = activeTextfield.getText();
            String lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
            String lastUpdateBy = Information.getUsername();

            ps.setString(1, customerId);
            ps.setString(2, customerName);
            ps.setString(3, addressId);
            ps.setString(4, active);
            ps.setString(5, lastUpdate);
            ps.setString(6, lastUpdateBy);
            ps.setString(7, customerId);

            ps.execute();

            DBConnection.closeConnection();
            //close screen and clear observablelist
            System.out.println("Closing Existing Customer Screen...");
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            Customer.clearCustomers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer and fill out all required fields to proceed.");
            alert.show();
            throw new InvalidCustomerException("Not all fields filled out. Complete all fields to continue");
        }

    }

    @FXML
    private void deleteChanges(ActionEvent event) {
        System.out.println("Delete button pressed");
        Customer selectedCustomer = existingCustTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a customer to delete first.");
            alert.show();
        } else {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this customer?");
            deleteAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        //delete records here for customer and addresss

                        Connection conn = DBConnection.startConnection();

                        String deleteCustomerStatement = "DELETE FROM customer WHERE customerId = ?";

                        DBQuery.setPreparedStatement(conn, deleteCustomerStatement);
                        PreparedStatement customerPs = DBQuery.getPreparedStatement();

                        String customerId = Integer.toString(selectedCustomer.getCustomerId());

                        customerPs.setString(1, customerId);

                        customerPs.execute();
                        String deleteAddressStatement = "DELETE FROM address WHERE addressId = ?";

                        DBQuery.setPreparedStatement(conn, deleteAddressStatement);
                        PreparedStatement addressPs = DBQuery.getPreparedStatement();

                        String addressId = Integer.toString(selectedCustomer.getAddressId());

                        addressPs.setString(1, addressId);
                        addressPs.execute();

                        Customer.deleteCustomer(selectedCustomer);
                        existingCustTable.setItems(Customer.getAllCustomers());
                        System.out.println("CustomerID " + selectedCustomer.getCustomerId() + " was deleted.");
                        System.out.println("AddressID " + selectedCustomer.getAddressId() + " was deleted.");

                    } catch (SQLException ex) {
                        Logger.getLogger(ExistingCustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please make sure all records associated with the customer you are trying to delete are deleted first. (appointments)");
                        alert.show();
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.setAlwaysOnTop(true);
                        stage.toFront();
                    }
                }
                DBConnection.closeConnection();
            });
        }
    }

}
