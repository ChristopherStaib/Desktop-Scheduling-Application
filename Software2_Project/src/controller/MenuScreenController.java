/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class MenuScreenController implements Initializable {
    @FXML
    private Button calendarButton;
    @FXML
    private Button appointmentButton;
    @FXML
    private Label menuLabel;
    @FXML
    private Button logoutButton;
    ResourceBundle rb;
    @FXML
    private Button customerButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;

        //was going to do internationalization for all pages, but requirement only states login screen
        calendarButton.setText(rb.getString("Calendar"));
        appointmentButton.setText(rb.getString("Appointments"));
        customerButton.setText("Customer"); 
        menuLabel.setText(rb.getString("Menu"));
        logoutButton.setText(rb.getString("Logout"));       
    }


    @FXML
    private void CalendarClick(ActionEvent event) throws IOException {
        //move to Calendar Screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/CalendarScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Calendar Screen");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    System.out.println("Calendar Screen Accessed.");
    }

    @FXML
    private void AppointmentClick(ActionEvent event) {
        ButtonType newAppointment = new ButtonType("New", ButtonBar.ButtonData.OK_DONE);
        ButtonType existingAppointment = new ButtonType("Existing", ButtonBar.ButtonData.FINISH);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to access an existing or new appointment?", newAppointment, existingAppointment);
        alert.setTitle("Please choose between New or Existing.");
        alert.showAndWait().ifPresent(response -> { //lambda used to check if appointment is new or existing and point to correct screen with custom buttontypes
            if (response == newAppointment) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewAppointmentScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("New Appointment Screen");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    System.out.println("New Appointment Screen Accessed.");
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ExistingAppointmentScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Existing Appointment Screen");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    System.out.println("Existing Appointment Screen Accessed.");
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void LogoutClick(ActionEvent event) {
        //show alert asking if sure to exit, then move to login screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("LogoutWarning"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Logging out...");
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
    }

    @FXML
    private void customerClick(ActionEvent event) throws IOException {
        //move to Customer Screen
        ButtonType newCustomer = new ButtonType("New", ButtonBar.ButtonData.OK_DONE);
        ButtonType existingCustomer = new ButtonType("Existing", ButtonBar.ButtonData.FINISH);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to access an existing or new customer?", newCustomer, existingCustomer);
        alert.setTitle("Please choose between New or Existing.");
        alert.showAndWait().ifPresent(response -> { //lambda used to check if customer is new or existing and point to correct screen with custom buttontypes
            if (response == newCustomer) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewCustomerScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("New Customer Screen");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    System.out.println("New Customer Screen Accessed.");
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ExistingCustomerScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Existing Customer Screen");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    System.out.println("Existing Customer Screen Accessed.");
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
}
