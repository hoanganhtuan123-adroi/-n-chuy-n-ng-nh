package model;

import java.sql.Date;

public class EmployeeModel {
    private String employee_id;
    private int account_id;
    private String account;
    private String employee_first_name;
    private String employee_last_name;
    private String employee_full_name;
    private String employee_address;
    private String employee_phone;
    private String employee_email;
    private String employee_position;
    private String employee_department;
    private Date employee_date;
    private String employee_salary;

    public EmployeeModel(String id, String name, String fullName, String email, String phone, String address, String position, String department) {
        this.employee_id = id;
        this.account = name;
        this.employee_full_name = fullName;
        this.employee_email = email;
        this.employee_phone = phone;
        this.employee_address = address;
        this.employee_position = position;
        this.employee_department = department;
    }

    public EmployeeModel(String employeID, String firstName, String lastName, String email, String phone, String address, String department, String salary, String position, Date hireDay, String account) {
        this.employee_id = employeID;
        this.employee_first_name = firstName;
        this.employee_last_name = lastName;
        this.employee_email = email;
        this.employee_phone = phone;
        this.employee_address = address;
        this.employee_position = position;
        this.employee_department = department;
        this.employee_salary = salary;
        this.employee_date = hireDay;
        this.account = account;
    }

    public EmployeeModel(String employeeId, String accountName, String employeeFirstName, String employeeLastName, String employeeEmail, String employeePhone, String employeeAddress, String employeePosition, String employeeDepartment, String salary, Date hireDate) {
        this.employee_id = employeeId;
        this.account = accountName;
        this.employee_first_name = employeeFirstName;
        this.employee_last_name = employeeLastName;
        this.employee_email = employeeEmail;
        this.employee_phone = employeePhone;
        this.employee_address = employeeAddress;
        this.employee_position = employeePosition;
        this.employee_department = employeeDepartment;
        this.employee_salary = salary;
        this.employee_date = hireDate;
    }

    public String getEmployee_full_name() {
        return employee_full_name;
    }

    public void setEmployee_full_name(String employee_full_name) {
        this.employee_full_name = employee_full_name;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmployee_first_name() {
        return employee_first_name;
    }

    public void setEmployee_first_name(String employee_first_name) {
        this.employee_first_name = employee_first_name;
    }

    public String getEmployee_last_name() {
        return employee_last_name;
    }

    public void setEmployee_last_name(String employee_last_name) {
        this.employee_last_name = employee_last_name;
    }

    public String getEmployee_address() {
        return employee_address;
    }

    public void setEmployee_address(String employee_address) {
        this.employee_address = employee_address;
    }

    public String getEmployee_phone() {
        return employee_phone;
    }

    public void setEmployee_phone(String employee_phone) {
        this.employee_phone = employee_phone;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

    public String getEmployee_position() {
        return employee_position;
    }

    public void setEmployee_position(String employee_position) {
        this.employee_position = employee_position;
    }

    public String getEmployee_department() {
        return employee_department;
    }

    public void setEmployee_department(String employee_department) {
        this.employee_department = employee_department;
    }

    public Date getEmployee_date() {
        return employee_date;
    }

    public void setEmployee_date(Date employee_date) {
        this.employee_date = employee_date;
    }

    public String getEmployee_salary() {
        return employee_salary;
    }

    public void setEmployee_salary(String employee_salary) {
        this.employee_salary = employee_salary;
    }
}
