package custome;

import controller.EmployeeController;
import model.EmployeeModel;
import view.employeeComponent.EmployeeDetail;
import view.employeeComponent.EmployeeUpdate;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonRendererEmployee extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JPanel panel;
    private JButton detailButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;

    public ButtonRendererEmployee(JTable table) {
        this.table = table;

        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Detail");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        // Add buttons to panel
        panel.add(detailButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        // Add action listeners for buttons
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ hàng đã chọn
                    int employeeID = Integer.parseInt(table.getValueAt(row, 0).toString());
                    EmployeeController employeeController = new EmployeeController();
                    EmployeeModel employeeModel = employeeController.getEmployee(employeeID);
                    EmployeeDetail employeeDetail = new EmployeeDetail(employeeModel);
                    employeeDetail.setVisible(true);
                }
               }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ hàng đã chọn
                    int employeeID = Integer.parseInt(table.getValueAt(row, 0).toString());
                    EmployeeController employeeController = new EmployeeController();
                    EmployeeModel employeeModel = employeeController.getEmployee(employeeID);
                    EmployeeUpdate update = new EmployeeUpdate(employeeModel);
                    update.setVisible(true);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có chắc chắn muốn xóa dữ liệu này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if(result == JOptionPane.YES_OPTION){
                    EmployeeController employeeController = new EmployeeController();
                    int row = table.getSelectedRow();
                    if(row >= 0){
                        Object employeeIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                        int employeeID = (employeeIdObj instanceof Integer) ? (Integer) employeeIdObj : Integer.parseInt(employeeIdObj.toString());
                        try {
                            boolean isDeleted = employeeController.deleteEmployee(employeeID);
                            if(isDeleted){
                                JOptionPane.showMessageDialog(panel, "Employee deleted successfully");
                            } else {
                                JOptionPane.showMessageDialog(panel, "Unable to delete employee");
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
