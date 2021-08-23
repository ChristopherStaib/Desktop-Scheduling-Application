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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Hour;
import model.Minute;
import model.Period;
import model.User;
import utils.DBConnection;
import utils.DBQuery;
import utils.Information;
import utils.InvalidAppointmentException;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class NewAppointmentScreenController implements Initializable {

    //Tableview for Customer
    @FXML
    private TableView<Customer> customerTableview;
    @FXML
    private TableColumn<Customer, Integer> custIDCol;
    @FXML
    private TableColumn<Customer, String> custNameCol;

    //Tableview for User
    @FXML
    private TableView<User> userTableview;
    @FXML
    private TableColumn<User, Integer> userIdCol;
    @FXML
    private TableColumn<User, String> usernameCol;

    //Textfields
    @FXML
    private TextField typeTextfield;
    @FXML
    private TextField apptIdTextfield;

    //Buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button exitButton;

    //DatePicker
    @FXML
    private DatePicker datePicker;

    //Labels
    @FXML
    private Label datePickerLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label newAppointmentTitle;
    @FXML
    private Label apptIdLabel;
    @FXML
    private Label endTimeLabel;
    @FXML
    private Label startTimeLabel;

    //Comboboxes for Start Time
    @FXML
    private ComboBox<Hour> startHourBox;
    @FXML
    private ComboBox<Minute> startMinuteBox;
    @FXML
    private ComboBox<Period> startAmOrPm;

    //Comboboxes for End Time
    @FXML
    private ComboBox<Hour> endHourBox;
    @FXML
    private ComboBox<Minute> endMinuteBox;
    @FXML
    private ComboBox<Period> endAmOrPm;

    ResourceBundle rb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        try {
            //select all customers
            Connection conn = DBConnection.startConnection();

            String selectCustomerStatement = "SELECT * FROM customer";

            DBQuery.setPreparedStatement(conn, selectCustomerStatement);
            PreparedStatement selectCustomer = DBQuery.getPreparedStatement();

            selectCustomer.execute();

            ResultSet rs = selectCustomer.getResultSet();

            while (rs.next()) {
                int customerId = Integer.parseInt(rs.getString("customerId"));
                String customerName = rs.getString("customerName");

                Customer newCustomer = new Customer(customerId, customerName); //create new instance of customer using overloaded constructor
                Customer.addCustomer(newCustomer); //add to observablelist
            }

            String selectUserStatement = "SELECT * FROM user"; //select all users records

            DBQuery.setPreparedStatement(conn, selectUserStatement);
            PreparedStatement selectUser = DBQuery.getPreparedStatement();

            selectUser.execute();

            ResultSet userRs = selectUser.getResultSet();

            while (userRs.next()) {
                int userId = Integer.parseInt(userRs.getString("userId"));
                String userName = userRs.getString("userName");

                User newUser = new User(userId, userName); //create new instance of user using overloaded constructor
                User.addUser(newUser); //add to observablelist
            }

            DBConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set cust table
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTableview.setItems(Customer.getAllCustomers());

        //set user table
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userTableview.setItems(User.getAllUsers());

        //using for loop to set start and end hour boxes with values from 1-12
        for (int i = 1; i <= 12; i++) {
            Hour hour = new Hour(Integer.toString(i));
            startHourBox.getItems().add(hour);
            endHourBox.getItems().add(hour);
        }

        //new minute instances to set minute boxes
        Minute minute = new Minute(":00");
        Minute minute2 = new Minute(":15");
        Minute minute3 = new Minute(":30");
        Minute minute4 = new Minute(":45");

        //set minute boxes with above minute instances
        startMinuteBox.getItems().addAll(minute, minute2, minute3, minute4);
        endMinuteBox.getItems().addAll(minute, minute2, minute3, minute4);

        //new period instances for AM and PM respectively
        Period am = new Period("AM");
        Period pm = new Period("PM");

        //set Period Comboboxes with values above
        startAmOrPm.getItems().addAll(am, pm);
        endAmOrPm.getItems().addAll(am, pm);

        //Period is selected as AM by default to avoid a null check
        startAmOrPm.getSelectionModel().select(am);
        endAmOrPm.getSelectionModel().select(am);

        //datePicker set to now to avoid a null check
        datePicker.setValue(LocalDate.now());
        datePicker.setShowWeekNumbers(false);

    }

    @FXML
    private void saveAction(ActionEvent event) throws SQLException, InvalidAppointmentException, ParseException {
        //selects for 2 tables
        Customer selectedCustomer = customerTableview.getSelectionModel().getSelectedItem();
        User selectedUser = userTableview.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) { //check if customer has a value selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "Customer is not selected. Please select a Customer to continue.");
            alert.show();
        } else if (selectedUser == null) { //check if user has a value selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "User is not selected. Please select a User to continue.");
            alert.show();
        } else if (typeTextfield.getText().isEmpty()) { //null check for type
            Alert alert = new Alert(Alert.AlertType.WARNING, "Type field is empty. Please enter a type to continue.");
            alert.show();
        } else {
            Connection conn = DBConnection.startConnection();

            String insertAppointmentStatement = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            DBQuery.setPreparedStatement(conn, insertAppointmentStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            String customerId = Integer.toString(selectedCustomer.getCustomerId());
            String userId = Integer.toString(selectedUser.getUserId());
            String type = typeTextfield.getText();

            Hour selectedStartHour = startHourBox.getValue();
            Minute selectedStartMin = startMinuteBox.getValue();
            Period selectedStartPeriod = startAmOrPm.getValue();

            Hour selectedEndHour = endHourBox.getValue();
            Minute selectedEndMin = endMinuteBox.getValue();
            Period selectedEndPeriod = endAmOrPm.getValue();

            //check to make sure all boxes selected
            if (selectedStartHour != null
                    && selectedStartMin != null
                    && selectedEndHour != null
                    && selectedEndMin != null) {

                /*
                Proceeding if else statements are a check on if period is AM or PM for respective start and end hours, if PM need to be able to convert to military time to get a proper time to input
                for SQL later, if PM selected need to change current hour, if AM selected and single digit hour, need to add 0 before number, etc.
                 */
                String txtStartHour = "";
                if (selectedStartPeriod.getPeriod().equals("AM")) {
                    if (Integer.parseInt(selectedStartHour.getHour()) < 10) {
                        txtStartHour = "0" + selectedStartHour.getHour();
                    } else if (Integer.parseInt(selectedStartHour.getHour()) == 12) {
                        txtStartHour = "00";
                    } else {
                        txtStartHour = selectedStartHour.getHour();
                    }
                } else if (selectedStartPeriod.getPeriod().equals("PM")) {
                    if (Integer.parseInt(selectedStartHour.getHour()) < 12) {
                        int intStartHour = Integer.parseInt(selectedStartHour.getHour()) + 12;
                        txtStartHour = Integer.toString(intStartHour);
                    } else {
                        txtStartHour = selectedStartHour.getHour();
                    }
                }
                String txtEndHour = "";
                if (selectedEndPeriod.getPeriod().equals("AM")) {
                    if (Integer.parseInt(selectedEndHour.getHour()) < 10) {
                        txtEndHour = "0" + selectedEndHour.getHour();
                    } else if (Integer.parseInt(selectedEndHour.getHour()) == 12) {
                        txtEndHour = "00";
                    } else {
                        txtEndHour = selectedEndHour.getHour();
                    }
                } else if (selectedEndPeriod.getPeriod().equals("PM")) {
                    if (Integer.parseInt(selectedEndHour.getHour()) < 12) {
                        int intEndHour = Integer.parseInt(selectedEndHour.getHour()) + 12;
                        txtEndHour = Integer.toString(intEndHour);

                    } else {
                        txtEndHour = selectedEndHour.getHour();
                    }
                }

                String localDate = datePicker.getValue().toString();

                //concats to make a complete time similar to datetimeformatter below yyyy-MM-dd HH:mm:ss
                String txtStartTime = localDate + " " + txtStartHour + selectedStartMin + ":" + "00";
                String txtEndTime = localDate + " " + txtEndHour + selectedEndMin + ":" + "00";

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                //convert to LDT
                LocalDateTime ldtStart = LocalDateTime.parse(txtStartTime, df);
                LocalDateTime ldtEnd = LocalDateTime.parse(txtEndTime, df);

                //LDT for business hours
                LocalDateTime ldtOpen = LocalDateTime.parse(localDate + " " + "07:59:00", df);
                LocalDateTime ldtClose = LocalDateTime.parse(localDate + " " + "17:01:00", df);

                //check if within business hours
                if (ldtStart.isAfter(ldtOpen) && ldtStart.isBefore(ldtClose)
                        && ldtEnd.isBefore(ldtClose) && ldtEnd.isAfter(ldtOpen)) {

                    String selectTimesStatement = "SELECT * FROM appointment WHERE userId = ?";

                    DBQuery.setPreparedStatement(conn, selectTimesStatement);
                    PreparedStatement selectTimePs = DBQuery.getPreparedStatement();

                    selectTimePs.setString(1, Integer.toString(selectedUser.getUserId()));

                    selectTimePs.execute();

                    ResultSet selectTimeRs = selectTimePs.getResultSet();

                    boolean isApptException = false;
                    while (selectTimeRs.next()) {
                         //grabbed results from appointments
                        String start = selectTimeRs.getString("start");
                        String end = selectTimeRs.getString("end");

                        //set standard that time from SQL is in UTC
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        Date sqlStartDate = utcFormat.parse(start);
                        Date sqlEndDate = utcFormat.parse(end);

                        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        currentFormat.setTimeZone(TimeZone.getDefault());

                        String txtSqlStartDate = currentFormat.format(sqlStartDate);
                        String txtSqlEndDate = currentFormat.format(sqlEndDate);

                        //separate time from date to compare
                        LocalDate ldSqlStartDate = LocalDate.parse(txtSqlStartDate.substring(0, 10));
                        LocalDate ldSqlEndDate = LocalDate.parse(txtSqlEndDate.substring(0, 10));

                        LocalTime sqlStartTime = LocalTime.parse(txtSqlStartDate.substring(11));
                        LocalTime sqlEndTime = LocalTime.parse(txtSqlEndDate.substring(11));

                        LocalTime startTime = LocalTime.parse(txtStartHour + selectedStartMin + ":" + "00");
                        LocalTime endTime = LocalTime.parse(txtEndHour + selectedEndMin + ":" + "00");

                        //if date is same from input and sql, need to compare the times
                        if (ldSqlStartDate.toString().equals(localDate) && ldSqlEndDate.toString().equals(localDate)) {
                            try {
                                //function called to check for overlap in appointment times
                                checkAppointments(sqlStartTime, sqlEndTime, startTime, endTime);
                            } catch (InvalidAppointmentException ex) { //catches custom exception for overlapping
                                System.out.println("Appointment overlap caught.");
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment overlaps another. Please select another time.");
                                alert.show();
                                isApptException = true;
                            }
                        }
                    }

                    if (!isApptException) { //if no exception, continue with this code
                        ZoneId zid = ZoneId.systemDefault();

                        //converting times from current time for user to UTC for ease of use
                        ZonedDateTime zdtStart = ldtStart.atZone(zid);
                        ZonedDateTime zdtEnd = ldtEnd.atZone(zid);

                        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
                        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));

                        ldtStart = utcStart.toLocalDateTime();
                        ldtEnd = utcEnd.toLocalDateTime();

                        String startSqlTime = ldtStart.toString();
                        String endSqlTime = ldtEnd.toString();

                        String createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                        String createdBy = Information.getUsername();
                        String lastUpdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).format(LocalDateTime.now());
                        String lastUpdateBy = Information.getUsername();
                        String notNeeded = "not needed";

                        ps.setString(1, customerId);
                        ps.setString(2, userId);
                        ps.setString(3, notNeeded);
                        ps.setString(4, notNeeded);
                        ps.setString(5, notNeeded);
                        ps.setString(6, notNeeded);
                        ps.setString(7, type);
                        ps.setString(8, notNeeded);
                        ps.setString(9, startSqlTime);
                        ps.setString(10, endSqlTime);
                        ps.setString(11, createDate);
                        ps.setString(12, createdBy);
                        ps.setString(13, lastUpdate);
                        ps.setString(14, lastUpdateBy);

                        ps.execute();

                        DBConnection.closeConnection();

                        System.out.println("Closing New Appointment Screen...");
                        final Node source = (Node) event.getSource();
                        final Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();

                        Customer.clearCustomers();
                        User.clearUsers();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Time is not within business hours 8am - 5pm");
                    alert.show();
                    throw new InvalidAppointmentException("This time is not within business hours.");
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please make sure all fields are completed to continue.");
                alert.show();
            }

        }

    }

    @FXML
    private void exitAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("Alert"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Closing New Appointment Screen...");
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                Customer.clearCustomers();
                User.clearUsers();
            }
        });

    }

    public void checkAppointments(LocalTime sqlStart, LocalTime sqlEnd, LocalTime inputStart, LocalTime inputEnd) throws InvalidAppointmentException {
        /*
        Function to check if appointments overlap takes 4 localtimes and checks if input is going to overlap an existing appointment, if so throws error and prevents code from executing
         */
        if (!((inputStart.isBefore(sqlStart) && inputEnd.isBefore(sqlStart)) || (inputStart.isAfter(sqlEnd) && inputEnd.isAfter(sqlEnd)))) {
            throw new InvalidAppointmentException("Appointment overlaps with previous appointments. A new time frame must be selected.");
        }
    }
}
