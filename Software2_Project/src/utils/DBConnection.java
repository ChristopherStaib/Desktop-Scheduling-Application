/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author chris
 */
public class DBConnection {

    //JDBC url parts
    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR_NAME = ":mysql:";
    private static final String IP_ADDRESS = "//3.227.166.251/U06Qpg";

    //concat url
    private static final String JDBC_URL = PROTOCOL + VENDOR_NAME + IP_ADDRESS;

    //driver and connection interface refernce
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String USERNAME = "U06Qpg";
    private static final String PASSWORD = "53688838743";

    public static Connection startConnection() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
            conn = (Connection) DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connection Successful");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
