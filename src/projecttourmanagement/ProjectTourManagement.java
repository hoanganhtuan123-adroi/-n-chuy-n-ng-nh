/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projecttourmanagement;

import controller.AuthController;

import java.sql.*;

import model.AccountModel;
import view.LoginView;
import view.MainView;

/**
 * @author admin
 */
public class ProjectTourManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
//     Connection con =  DatabaseConnection.getConnection();
        MainView mainView = new MainView();
//  Login Page
        AccountModel accountModel = new AccountModel();
        LoginView loginView = new LoginView();
        AuthController authController = new AuthController(accountModel, loginView, mainView);
        loginView.display(true);

//    MainView mainView = new MainView();
//    mainView.display();

//        CustomerView cv = new CustomerView();
//        CustomerModel customerModel = new CustomerModel();
//        CustomerController customerController = new CustomerController(customerModel, cv);
//        cv.display();
//        customerController.loadCustomer();
    }

}
