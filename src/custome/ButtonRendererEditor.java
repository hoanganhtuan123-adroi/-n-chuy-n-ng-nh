package custome;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import controller.CustomerController;
import controller.EmployeeController;
import model.CustomerModel;
import view.CustomerComponent.CustomerDetail;
import view.CustomerComponent.CustomerUpdate;
import view.CustomerComponent.CustomerView;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private List<CustomerModel> customersList;
    private JPanel panel;
    private JButton detailButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;
    private CustomerView customerView;

    public ButtonRendererEditor(JTable table, CustomerView customerView) throws SQLException {
        this.table = table;
        this.customerView = customerView;
        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Chi tiết");
        updateButton = new JButton("Câp nhập");
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
                    String customerName = (String) table.getValueAt(row, 1);
                    String email = (String) table.getValueAt(row, 2);
                    Object phoneObj = table.getValueAt(row, 3);
                    String phone = phoneObj != null ? phoneObj.toString() : "";
                    String address = (String) table.getValueAt(row, 4);

                    // Hiển thị JFrame mới với thông tin chi tiết
                    CustomerDetail detailFrame = new CustomerDetail((JFrame) SwingUtilities.getWindowAncestor(table), customerName, email, phone, address);
                    detailFrame.setVisible(true);
                }
               }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
//                    int customerId = customer.getCustomer_id();
                    // Lấy dữ liệu từ hàng đã chọn
                    Object customerIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                    int customerId = (customerIdObj instanceof Integer) ? (Integer) customerIdObj : Integer.parseInt(customerIdObj.toString());
                    String customerName = (String) table.getValueAt(row, 1);
                    String email = (String) table.getValueAt(row, 2);
                    Object phoneObj = table.getValueAt(row, 3);
                    String phone = phoneObj != null ? phoneObj.toString() : "";
                    String address = (String) table.getValueAt(row, 4);

                    // Hiển thị JFrame mới với thông tin chi tiết
                    CustomerUpdate updateFrame = new CustomerUpdate(customerView, customerName, email, phone, address, Integer.valueOf(customerId));
                    updateFrame.setVisible(true);
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
                    CustomerController customerController = new CustomerController();
                    int row = table.getSelectedRow();
                    if(row >= 0){
                        Object customerIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                        int customerId = (customerIdObj instanceof Integer) ? (Integer) customerIdObj : Integer.parseInt(customerIdObj.toString());
                        boolean isDeleted = customerController.deleteCustomer(customerId);

                        if(isDeleted){
                            JOptionPane.showMessageDialog(table, "Delete successful");
                        } else {
                            JOptionPane.showMessageDialog(table, "Delete fail ");
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
