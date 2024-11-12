package custome;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.CustomerController;
import model.CustomerModel;
import view.CustomerDetail;
import view.CustomerUpdate;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private List<CustomerModel> customersList;
    private JPanel panel;
    private JButton detailButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;

    public ButtonRendererEditor(JTable table) {
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
                JOptionPane.showMessageDialog(table, "Update button clicked for row " + row);
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
                    CustomerUpdate updateFrame = new CustomerUpdate(customerName, email, phone, address, Integer.valueOf(customerId));
                    updateFrame.setVisible(true);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0 ){
                    Object customerIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                    int customerId = (customerIdObj instanceof Integer) ? (Integer) customerIdObj : Integer.parseInt(customerIdObj.toString());
                    CustomerController customerController = new CustomerController();
                    boolean isDeleted = customerController.deleteCustomer(customerId);
                    if(isDeleted){
                        JOptionPane.showMessageDialog(table, "Delete successful");
                    } else {
                        JOptionPane.showMessageDialog(table, "Delete fail ");
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
