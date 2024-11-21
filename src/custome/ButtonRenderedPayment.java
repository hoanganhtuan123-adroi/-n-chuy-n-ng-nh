package custome;

import controller.CustomerController;
import controller.PaymentController;
import model.PaymentModel;
import view.IssuesComponent.IssueUpdate;
import view.PaymentComponent.PaymentDetail;
import view.PaymentComponent.PaymentUpdate;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonRenderedPayment extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private PaymentController paymentController;
    private JPanel panel;
    private JButton updateButton, detailButton, deleteButton;
    private JTable table;

    public ButtonRenderedPayment(JTable table){
        this.table = table;
        this.paymentController = new PaymentController();
        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Details");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        // Add buttons to panel
        panel.add(detailButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    Object payIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int issueID = (payIdObj instanceof Integer) ? (Integer) payIdObj : Integer.parseInt(payIdObj.toString());
                    try {
                        PaymentModel paymentModel = paymentController.getPayment(issueID);
                        PaymentDetail paymentDetail = new PaymentDetail(paymentModel);
                        paymentDetail.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    Object payIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int payID = (payIdObj instanceof Integer) ? (Integer) payIdObj : Integer.parseInt(payIdObj.toString());
                    try {
                        PaymentModel paymentModel = paymentController.getPayment(payID);
                        PaymentUpdate paymentUpdate = new PaymentUpdate(paymentModel);
                        paymentUpdate.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
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
                    int row = table.getSelectedRow();
                    if(row > 0){
                        Object payIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                        int payID = (payIdObj instanceof Integer) ? (Integer) payIdObj : Integer.parseInt(payIdObj.toString());
                        try {
                            boolean isSuccess =  paymentController.deletePayment(payID);
                            if(isSuccess){
                                JOptionPane.showMessageDialog(null, "Payment deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Payment deleted failed", "Failed", JOptionPane.ERROR_MESSAGE);
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
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}
