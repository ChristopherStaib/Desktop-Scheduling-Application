/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2_project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author chris
 */
public class Software2_Project extends Application {

    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        // Locale.setDefault(new Locale("fr")); //sets default for testing 
        ResourceBundle rb = ResourceBundle.getBundle("utils/Nat");
        System.out.println("Language set: " + Locale.getDefault());
        
        Parent main = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginScreen.fxml"));
            loader.setResources(rb);
            main = loader.load();
            
            Scene scene = new Scene(main);
            
            stage.setScene(scene);
            stage.setTitle(rb.getString("LoginTitle"));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }     
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        launch(args);   
    }

}
