/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Calendar;
import utils.DBConnection;
import utils.DBQuery;
import utils.Information;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class CalendarScreenController implements Initializable {

    //Labels
    @FXML
    private Label calendarTitleLabel;

    //Tableview for Calendar
    @FXML
    private TableView<Calendar> calendarTable;
    @FXML
    private TableColumn<Calendar, Integer> apptIdCol;
    @FXML
    private TableColumn<Calendar, Integer> custIdCol;
    @FXML
    private TableColumn<Calendar, Integer> userIdCol;
    @FXML
    private TableColumn<Calendar, String> custNameCol;
    @FXML
    private TableColumn<Calendar, String> userCol;
    @FXML
    private TableColumn<Calendar, String> typeCol;
    @FXML
    private TableColumn<Calendar, String> startCol;
    @FXML
    private TableColumn<Calendar, String> endCol;

    //Buttons
    @FXML
    private Button exitButton;
    @FXML
    private Button filterAppointmentButton;
    @FXML
    private Button filterScheduleButton;
    @FXML
    private Button filterCustomerButton;
    @FXML
    private Button resetButton;
    ResourceBundle rb;

    //Radio Buttons
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        try {
            Connection conn = DBConnection.startConnection();

            // select appointment first then for each customerId, pull the customername
            // select appointment then for each userId, pull the Username
            String selectAppointmentStatement = "SELECT * FROM appointment";

            DBQuery.setPreparedStatement(conn, selectAppointmentStatement);
            PreparedStatement apptPs = DBQuery.getPreparedStatement();

            apptPs.execute();

            ResultSet apptRs = apptPs.getResultSet();

            while (apptRs.next()) {
                int appointmentId = Integer.parseInt(apptRs.getString("appointmentId"));
                int customerId = Integer.parseInt(apptRs.getString("customerId"));
                int userId = Integer.parseInt(apptRs.getString("userId"));
                String type = apptRs.getString("type");

                String start = apptRs.getString("start").replaceAll("\\.\\d+", "");
                String end = apptRs.getString("end").replaceAll("\\.\\d+", "");

                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date startDate = utcFormat.parse(start);
                Date endDate = utcFormat.parse(end);

                DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentFormat.setTimeZone(TimeZone.getDefault());

                String inputStart = currentFormat.format(startDate);
                String inputEnd = currentFormat.format(endDate);

                String selectCustomerStatement = "SELECT customerName FROM customer WHERE customerId = ?";

                DBQuery.setPreparedStatement(conn, selectCustomerStatement);
                PreparedStatement custPs = DBQuery.getPreparedStatement();

                custPs.setString(1, Integer.toString(customerId));

                custPs.execute();

                ResultSet custRs = custPs.getResultSet();

                if (custRs.next()) {
                    String customerName = custRs.getString("customerName");

                    String selectUserStatement = "SELECT userName FROM user WHERE userId = ?";

                    DBQuery.setPreparedStatement(conn, selectUserStatement);
                    PreparedStatement userPs = DBQuery.getPreparedStatement();

                    userPs.setString(1, Integer.toString(userId));

                    userPs.execute();

                    ResultSet userRs = userPs.getResultSet();

                    if (userRs.next()) {
                        String userName = userRs.getString("userName");

                        Calendar newCalendar = new Calendar(appointmentId, customerId, userId, customerName, userName, type, inputStart, inputEnd); //create new instance of calendar then add to calendar
                        Calendar.addToCalendar(newCalendar);
                    }

                }

            }
            DBConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //setup for tableview columns
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        calendarTable.setItems(Calendar.getCalendar());

        //check if appointment is within 15 minutes for current logged in user
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15 = now.plusMinutes(15);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //check all calendar objects for time to see if within 15 minutes of users login
        for (Calendar calendar : Calendar.getCalendar()) {
            LocalDateTime formattedStart = LocalDateTime.parse(calendar.getStart(), df);
            if (formattedStart.isAfter(now) && formattedStart.isBefore(nowPlus15) && calendar.getUserId() == Integer.parseInt(Information.getUserId())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have an appointment for " + "(" + calendar.getCustomerName() + ")" + " within the next 15 minutes!");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);
                stage.toFront();

            }
        }
    }

    @FXML
    private void exitScreen(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("Alert"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Closing Calendar Screen...");
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

                //clear all observablelists here
                Calendar.clearCalendar();
            }
        });

    }

    @FXML
    private void weeklyButtonSelected(ActionEvent event) {
        //change title and tableview to show filtered list - weekly
        weekRadioButton.setSelected(true);
        monthRadioButton.setSelected(false);
        calendarTitleLabel.setText("Calendar - By Week");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusWeek = now.plusWeeks(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //filtered list used to set calendar where rows are within 1 week of current time
        FilteredList<Calendar> filteredData = new FilteredList<>(Calendar.getCalendar());
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), formatter);

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlusWeek);

        });
        calendarTable.setItems(filteredData);
    }

    @FXML
    private void monthlyButtonSelected(ActionEvent event) {
        //change title and tableview to show filtered list -- monthly
        weekRadioButton.setSelected(false);
        monthRadioButton.setSelected(true);
        calendarTitleLabel.setText("Calendar - By Month");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusMonth = now.plusMonths(1);

        //filtered list used to set calendar where rows are within 1 month of current time
        FilteredList<Calendar> filteredData = new FilteredList<>(Calendar.getCalendar());
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), formatter);
            return rowDate.isAfter(now) && rowDate.isBefore(nowPlusMonth);

        });
        calendarTable.setItems(filteredData);
    }

    @FXML
    private void filterAppointmentAction(ActionEvent event) throws SQLException {
        /*
        Function to filter calendar by appointment, gets count and type of appointments that are within 1 month of LocalTime.now() After filtering, must reset Calendar to filter again    
         */

        monthRadioButton.setSelected(true);
        calendarTitleLabel.setText("Calendar - By Month");

        Connection conn = DBConnection.startConnection();

        String selectTypeStatement = "SELECT COUNT(type), type FROM appointment WHERE start > ? AND start < ? GROUP BY type ORDER BY COUNT(type) DESC;";

        DBQuery.setPreparedStatement(conn, selectTypeStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusMonth = now.plusMonths(1);

        ps.setString(1, now.toString());
        ps.setString(2, nowPlusMonth.toString());

        ps.execute();

        Calendar.clearCalendar();

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String amt = rs.getString(1);
            String type = rs.getString(2);

            Calendar newCalendar = new Calendar(amt, type);
            Calendar.addToCalendar(newCalendar);
        }

        calendarTable.getColumns().clear();

        TableColumn<Calendar, String> typeNumberCol = new TableColumn<>("#");
        typeNumberCol.setMinWidth(130);
        typeNumberCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Calendar, String> typeCol = new TableColumn<>("Type");
        typeCol.setMinWidth(130);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        calendarTable.getColumns().addAll(typeNumberCol, typeCol);

        calendarTable.setItems(Calendar.getCalendar());
        DBConnection.closeConnection();

    }

    @FXML
    private void filterScheduleAction(ActionEvent event) {
        /*
        Function to filter Calendar by Schedule for user inputted, Case Sensitive 
         */
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filter by User");
        dialog.setHeaderText("Filter Calendar by User");
        dialog.setContentText("Please enter a User's name (case sensitive):");

        dialog.showAndWait().ifPresent(response -> {
            ObservableList<Calendar> scheduleList = Calendar.lookupUserCalendar(response);
            Calendar.clearCalendar();
            Calendar.addAllToCalendar(scheduleList);
            calendarTable.setItems(Calendar.getCalendar());
        });

    }

    @FXML
    private void filterCustomerAction(ActionEvent event) {
        /*
        Function to filter Calendar by appointments for customer inputted, Case Sensitive 
         */
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filter by Customer");
        dialog.setHeaderText("Filter Calendar by Customer");
        dialog.setContentText("Please enter a customer's name (case sensitive):");

        dialog.showAndWait().ifPresent(response -> {
            ObservableList<Calendar> scheduleList = Calendar.lookupCustCalendar(response);
            Calendar.clearCalendar();
            Calendar.addAllToCalendar(scheduleList);
            calendarTable.setItems(Calendar.getCalendar());
        });
    }

    @FXML
    private void resetAction(ActionEvent event) throws IOException {
        /*
        Easiest way to get back to original screen - Reloads the FXML to initialize everything again        
         */

        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        //clear all observablelists here
        Calendar.clearCalendar();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/CalendarScreen.fxml"));
        fxmlLoader.setResources(rb);
        Parent root1 = (Parent) fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Calendar Screen");
        newStage.setScene(new Scene(root1));
        newStage.show();
        System.out.println("Calendar Screen Accessed.");
    }
}
