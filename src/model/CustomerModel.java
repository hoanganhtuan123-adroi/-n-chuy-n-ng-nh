/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class CustomerModel {
    private int customer_id;
    private int account_id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Timestamp created_at, updated_at;

    public CustomerModel() {super();};

    public CustomerModel(int customer_id, int account_id , String customer_name, String email, String phone, String address, Timestamp created_at, Timestamp updated_at ) {
        this.customer_id = customer_id;
        this.name = customer_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.account_id = account_id;
    }

    public CustomerModel(int accountID, String name, String email, String phone, String address) {
        this.account_id = accountID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public CustomerModel(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public CustomerModel(int id, int acId, String name, String email, String phone, String address) {
        this.customer_id = id;
        this.account_id = acId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getAccount_id(){return account_id;};

    public void setAccount_id(int account_id){this.account_id = account_id;}

    public int getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_name() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setCustomer_name(String customer_name) {
        this.name = customer_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }


    
}
