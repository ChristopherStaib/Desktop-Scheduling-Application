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
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;
import utils.Information;
import utils.LoginException;

/**
 *
 * @author chris
 */
public class LoginScreen implements Initializable {

    //buttons
    @FXML
    private Button login;
    @FXML
    private Button Exit;
    
    //textfields and passfields
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    
    //labels
    @FXML
    private Label titleLabel;
    
    //anchor pane
    @FXML
    private AnchorPane loginAnchor;

    ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        //set text to allow for internationalization -- en/fr/es
        login.setText(rb.getString("Login"));
        Exit.setText(rb.getString("Exit"));
        titleLabel.setText(rb.getString("Title"));
        usernameField.setPromptText(rb.getString("UserName"));
        passwordField.setPromptText(rb.getString("Password"));
    }

    @FXML
    private void LogInPressed(ActionEvent event) throws SQLException, IOException, LoginException {
        /* Event Handler verifies text from username and password fields and checks if the user and pass from database match,  if yes move to next screen.
           If user and password do not match, it will show an alert and prevent login.
           If fields are empty, the application will inform that the fields are required by showing alert and preventing login */
        
        Connection conn = DBConnection.startConnection();
        String selectStatement = "SELECT * FROM user";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String userName = rs.getString("userName");
            String password = rs.getString("password");
            String userId = rs.getString("userId");

            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("Required"));
                alert.show();
                if (usernameField.getText().isEmpty()) {
                    usernameField.setStyle("-fx-border-color: red;");
                    passwordField.setStyle(null);
                } else {
                    passwordField.setStyle("-fx-border-color: red;");
                    usernameField.setStyle(null);
                }

            } else if (usernameField.getText().equals(userName)) {
                if (passwordField.getText().equals(password)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MenuScreen.fxml"));
                    fxmlLoader.setResources(rb);
                    Parent root1 = (Parent) fxmlLoader.load();
                    
                    //pass user to Profile Screen to retrieve profile information 
                    Information.setUsername(userName);
                    Information.setPassword(password);
                    Information.setUserId(userId);
                    
                    //logger for logins
                    Logger log = Logger.getLogger("logins.txt");
                    FileHandler fh = new FileHandler("logins.txt", true);
                    SimpleFormatter sf = new SimpleFormatter();
                    fh.setFormatter(sf);
                    log.addHandler(fh);
                    
                    log.setLevel(Level.INFO);                    
                    log.log(Level.INFO, Information.getUsername() + " has logged in at: " + LocalDateTime.now());
                    fh.close();
                    
                    Stage stage = new Stage();
                    stage.setTitle(rb.getString("MenuTitle"));
                    stage.setScene(new Scene(root1));
                    stage.show();

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("Warning"));
                    alert.show();
                    throw new LoginException("Login credentials do not match.");
                }
            }
        }

        DBConnection.closeConnection();
    }

    @FXML
    private void ExitPressed(ActionEvent event) {
        //if exit pressed, closes application
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("Alert"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Closing application..");
                Platform.exit();
            }
        });
    }

}
