/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AccountModel;
import view.LoginView;
import view.MainView;
/**
 *
 * @author admin
 */
public class AuthController {
    private final AccountModel accountModel;
    private final LoginView loginView;
    private final MainView mainView;
    public AuthController(AccountModel accountModel, LoginView loginView, MainView mainView){
        this.accountModel = accountModel;
        this.loginView = loginView;
        this.mainView = mainView;
        this.loginView.setLoginButtonActionListener(new loginListener());
    }
    public class loginListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            try {
                if(accountModel.validateLogin(username, password)){
                    accountModel.setIsLogin(true);
                    loginView.unDisplay();
                    mainView.display();
                    loginView.showMess("Login Success");
                } else {
                    accountModel.setIsLogin(false);
                    System.out.println("Login Failed");
                    loginView.showMess("Login Failed");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
    }
    
}
