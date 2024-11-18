package controller;

import database.DatabaseConnection;
import model.EmployeeModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {
    public static List<EmployeeModel> getAllEmployee() {
        List<EmployeeModel> listEmployee = new ArrayList<EmployeeModel>();
        String query = "SELECT e.employee_id, a.username ,concat(e.first_name, ' ', e.last_name) as full_name, e.email, e.phone, e.address, e.position, e.department from employees as e left join accounts as a on e.account_id = a.account_id";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String name = rs.getString("username");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String position = rs.getString("position");
                String department = rs.getString("department");
                listEmployee.add(new EmployeeModel(id, name, fullName, email, phone, address, position, department));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listEmployee;
    }

    public boolean addEmployee(EmployeeModel employee) {
        String query = "INSERT INTO accounts(username, password, email, phone, role, status) VALUES(?,'123456', ?,?, 'employee', 'active')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement acc_pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(false);
            acc_pstmt.setString(1, employee.getAccount());
            acc_pstmt.setString(2, employee.getEmployee_email());
            acc_pstmt.setString(3, employee.getEmployee_phone());
            acc_pstmt.executeUpdate();

            ResultSet rs = acc_pstmt.getGeneratedKeys();
            int account_id = 0;
            if (rs.next()) {
                account_id = rs.getInt(1);
            }

            String employee_query = "INSERT INTO employees (account_id, first_name, last_name, email, phone, address, position, department, hire_date, salary)\n" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement emp_pstmt = con.prepareStatement(employee_query)) {
                emp_pstmt.setInt(1, account_id);
                emp_pstmt.setString(2, employee.getEmployee_first_name());
                emp_pstmt.setString(3, employee.getEmployee_last_name());
                emp_pstmt.setString(4, employee.getEmployee_email());
                emp_pstmt.setString(5, employee.getEmployee_phone());
                emp_pstmt.setString(6, employee.getEmployee_address());
                emp_pstmt.setString(7, employee.getEmployee_position());
                emp_pstmt.setString(8, employee.getEmployee_department());
                emp_pstmt.setDate(9, employee.getEmployee_date());
                emp_pstmt.setString(10, employee.getEmployee_salary());
                int row = emp_pstmt.executeUpdate();

                con.setAutoCommit(true);
                return row > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public EmployeeModel getEmployee(int employeeID) {
        EmployeeModel employee = null;
        String query = ""
                + "SELECT e.employee_id, a.username, "
                + "e.first_name, e.last_name, "
                + "e.email, e.phone, "
                + "e.address, e.position, e.salary, e.hire_date,"
                + "e.department, e.created_at "
                + "FROM employees AS e "
                + "LEFT JOIN accounts AS a ON e.account_id = a.account_id "
                + "WHERE e.employee_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ptsmt = con.prepareStatement(query);
        ) {
            ptsmt.setInt(1, employeeID);
            ResultSet rs = ptsmt.executeQuery();
            if (rs.next()) {
                int employee_id = rs.getInt("employee_id");
                String accountName = rs.getString("username");
                String employee_first_name = rs.getString("first_name");
                String employee_last_name = rs.getString("last_name");
                String employee_email = rs.getString("email");
                String employee_phone = rs.getString("phone");
                String employee_address = rs.getString("address");
                String employee_position = rs.getString("position");
                String employee_department = rs.getString("department");
                String salary = rs.getString("salary");
                Date hireDate = rs.getDate("hire_date");
                employee = new EmployeeModel(employee_id, accountName, employee_first_name, employee_last_name,
                        employee_email, employee_phone, employee_address, employee_position,
                        employee_department, salary, hireDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    public boolean updateEmployee(EmployeeModel employee) throws SQLException {
        boolean isSeccess = false;
        try (Connection con = DatabaseConnection.getConnection()) {
            isSeccess = false;
            String isExistAccountID = "SELECT account_id from employees where employee_id = ?";
            PreparedStatement check_acc = con.prepareStatement(isExistAccountID);
            check_acc.setInt(1, employee.getEmployee_id());
            ResultSet rs = check_acc.executeQuery();
            // If employee doesn't have an account
            if (rs.next() == false) {
                String insertNewAcc = "INSERT INTO accounts(username, password, email, phone, role, status) VALUES(?,'123456', ?,?, 'employee', 'active')";
                PreparedStatement acc_pstmt = con.prepareStatement(insertNewAcc);

                acc_pstmt.setString(1, employee.getAccount());
                acc_pstmt.setString(2, employee.getEmployee_email());
                acc_pstmt.setString(3, employee.getEmployee_phone());
                acc_pstmt.executeUpdate();

                ResultSet rs_acc = acc_pstmt.getGeneratedKeys();
                int account_id = 0;
                if (rs_acc.next()) {
                    account_id = rs_acc.getInt(1);
                }

                String insertIDIntoEmployee = "UPDATE employees SET account_id=? WHERE employee_id=? VALUES (?,?)";
                PreparedStatement emp_pstmt = con.prepareStatement(insertIDIntoEmployee);
                emp_pstmt.setInt(1, account_id);
                emp_pstmt.setInt(2, employee.getEmployee_id());

                String updateQuery = "UPDATE employees AS e" +
                        "LEFT JOIN accounts AS a ON e.account_id = a.account_id " +
                        "SET e.account_id = a.account_id," +
                        "e.first_name = ?," +
                        "    e.last_name = ?," +
                        "    e.email = ?," +
                        "    e.phone = ?," +
                        "    e.address = ?," +
                        "    e.position = ?," +
                        "    e.hire_date = ?," +
                        "    e.salary = ?," +
                        "    e.department = ?," +
                        "    a.username = ?," +
                        "     a.email = ?," +
                        "    a.phone = ? " +
                        "    WHERE e.employee_id = ?;";
                PreparedStatement update = con.prepareStatement(updateQuery);
                update.setString(1, employee.getEmployee_first_name());
                update.setString(2, employee.getEmployee_last_name());
                update.setString(3, employee.getEmployee_email());
                update.setString(4, employee.getEmployee_phone());
                update.setString(5, employee.getEmployee_address());
                update.setString(6, employee.getEmployee_position());
                update.setString(7, employee.getEmployee_date().toString());
                update.setString(8, employee.getEmployee_salary());
                update.setString(9, employee.getEmployee_department());
                update.setString(10, employee.getAccount());
                update.setString(11, employee.getEmployee_email());
                update.setString(12, employee.getEmployee_phone());
                update.setInt(13, employee.getEmployee_id());
                int affectedRow = update.executeUpdate();
                isSeccess = true;
                return affectedRow > 0;
            } else {
                String updateQuery = "UPDATE employees AS e\n" +
                        "LEFT JOIN accounts AS a ON e.account_id = a.account_id \n" +
                        "SET e.account_id = a.account_id,\n" +
                        "\te.first_name = ?,\n" +
                        "    e.last_name = ?,\n" +
                        "    e.email = ?,\n" +
                        "    e.phone = ?,\n" +
                        "    e.address = ?,\n" +
                        "    e.position = ?,\n" +
                        "    e.hire_date = ?,\n" +
                        "    e.salary = ?,\n" +
                        "    e.department = ?,\n" +
                        "    a.username = ?,\n" +
                        "     a.email = ?,\n" +
                        "    a.phone = ?\n" +
                        "    WHERE e.employee_id = ?;";

                try (PreparedStatement update = con.prepareStatement(updateQuery)) {
                    update.setString(1, employee.getEmployee_first_name());
                    update.setString(2, employee.getEmployee_last_name());
                    update.setString(3, employee.getEmployee_email());
                    update.setString(4, employee.getEmployee_phone());
                    update.setString(5, employee.getEmployee_address());
                    update.setString(6, employee.getEmployee_position());
                    update.setString(7, employee.getEmployee_date().toString());
                    update.setString(8, employee.getEmployee_salary());
                    update.setString(9, employee.getEmployee_department());
                    update.setString(10, employee.getAccount());
                    update.setString(11, employee.getEmployee_email());
                    update.setString(12, employee.getEmployee_phone());
                    update.setInt(13, employee.getEmployee_id());
                    int affectedRow = update.executeUpdate();
                    isSeccess = true;
                    return affectedRow > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSeccess;
    }

    public boolean deleteEmployee(int employeeId) throws SQLException {
        boolean isDetele = false;
        String query = "DELETE FROM employees WHERE employee_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            int affectedRow = pstmt.executeUpdate();
            if(affectedRow > 0){
                isDetele = true;
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDetele;
    }

    public List<EmployeeModel> searchEmployeeByFullName(String fullNameSearch) {
        List<EmployeeModel> listEmployee = new ArrayList<>();
        String query = "SELECT e.employee_id, a.username, " +
                "CONCAT(e.first_name, ' ', e.last_name) AS full_name, " +
                "e.email, e.phone, e.address, e.position, e.department " +
                "FROM employees AS e " +
                "LEFT JOIN accounts AS a ON e.account_id = a.account_id " +
                "WHERE CONCAT(e.first_name, ' ', e.last_name) LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "%" + fullNameSearch + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("employee_id");
                    String username = rs.getString("username");
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String position = rs.getString("position");
                    String department = rs.getString("department");
                    listEmployee.add(new EmployeeModel(id, username, fullName, email, phone, address, position, department));
                    con.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listEmployee;
    }


}




