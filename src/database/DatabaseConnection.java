/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

//import com.sun.jdi.connect.spi.Connection;
//import com.mysql.cj.jdbc.ConnectionImpl;
import java.sql.*;


/**
 *
 * @author admin
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/travel_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hoanganhtuan123";
    public static Connection getConnection() {
    	Connection c = null;
    	try {
    		DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
    		c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    		System.out.println("Connection successful!");
    		
    	} catch(SQLException e) {
    		System.out.println("Connection failed!");
    		e.printStackTrace();
    	}
    	
    	return c;
    }
}
