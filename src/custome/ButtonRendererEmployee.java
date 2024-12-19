package custome;

import controller.EmployeeController;
import model.EmployeeModel;
import view.employeeComponent.EmployeeDetail;
import view.employeeComponent.EmployeeUpdate;
import view.employeeComponent.EmployeeView;

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
    private EmployeeView employeeView;

    public ButtonRendererEmployee(JTable table, EmployeeView employeeView) {
        this.table = table;
        this.employeeView = employeeView;
        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Chi Tiết");
        updateButton = new JButton("Cập Nhập");
        deleteButton = new JButton("Xóa");

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
                    String employeeID = table.getValueAt(row, 0).toString();
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
                    String employeeID =table.getValueAt(row, 0).toString();
                    EmployeeController employeeController = new EmployeeController();
                    EmployeeModel employeeModel = employeeController.getEmployee(employeeID);
                    EmployeeUpdate update = new EmployeeUpdate(employeeModel, employeeView);
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
                        String employeeID = employeeIdObj.toString();
                        System.out.println(employeeID);
                        try {
                            boolean isDeleted = employeeController.deleteEmployee(employeeID);
                            if(isDeleted){
                                JOptionPane.showMessageDialog(panel, "Xóa thành công!");
                                employeeView.loadCustomerData();
                            } else {
                                JOptionPane.showMessageDialog(panel, "Không thể xóa nhân viên!");
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
