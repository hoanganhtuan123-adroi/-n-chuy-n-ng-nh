package custome;

import controller.PaymentController;
import database.DatabaseConnection;
import model.PaymentModel;
import view.PaymentComponent.PaymentDetail;
import view.PaymentComponent.PaymentUpdate;
import view.PaymentComponent.PaymentView;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class ButtonRenderedPayment extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private PaymentController paymentController;
    private JPanel panel;
    private JButton updateButton, detailButton, deleteButton;
    private JTable table;
    private PaymentView paymentView;
    private Connection con;

    public ButtonRenderedPayment(JTable table, PaymentView paymentView) throws SQLException {
        this.table = table;
        this.paymentController = new PaymentController();
        this.paymentView = paymentView;
        con = DatabaseConnection.getConnection();
        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Chi Tiết");
        updateButton = new JButton("Cập Nhập");
        deleteButton = new JButton("Xóa");

        // Add buttons to panel
        panel.add(detailButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    String paymentID = table.getValueAt(row, 0).toString();
                    try {
                        if(paymentController.getPayment(paymentID)==null){
                            JOptionPane.showMessageDialog(null, "Khách hàng chưa thanh toán");
                        } else {
                            PaymentModel paymentModel = paymentController.getPayment(paymentID);
                            PaymentDetail paymentDetail = new PaymentDetail(paymentModel);
                            paymentDetail.setVisible(true);
                        }

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

                if (row >= 0) { // Kiểm tra xem người dùng có chọn hàng nào không
                    Object payIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là paymentID
                    String paymentID = (payIdObj != null) ? payIdObj.toString() : null; // Xử lý giá trị null cho paymentID

                    Object tourNameObj = table.getValueAt(row, 2); // Giả sử cột thứ 3 là tourName
                    String tourName = (tourNameObj != null) ? tourNameObj.toString() : "Chưa chọn"; // Gán mặc định là "Chưa chọn" nếu null

                    Object cusNameObj = table.getValueAt(row, 1);
                    String cusName = (cusNameObj != null) ? cusNameObj.toString() : null;

                    try {
                        // Lấy paymentModel từ controller
                        if (paymentController.getPayment(paymentID) == null ) {
                            if(tourName.equals("Chưa chọn")){
                                int customerID = paymentController.getCustomerIdByCusName(cusName);
                                PaymentModel paymentModel = paymentController.getPaymentByCustomerIDAndTourNull(customerID, cusName);
                                PaymentUpdate paymentUpdate = new PaymentUpdate(paymentModel, paymentView);
                                paymentUpdate.setVisible(true);
                            } else {
                                // Trường hợp paymentID không tồn tại
                                int tourID = paymentController.getTourIdByName(con,tourName);
                                int customerID = paymentController.getCustomerIdByTourNameAndTourIdAndCusName(tourName, tourID, cusName);
                                if(customerID != 0){
                                    PaymentModel paymentModel = paymentController.getPaymentByCustomerIDAndTourId(customerID, tourID);
                                    PaymentUpdate paymentUpdate = new PaymentUpdate(paymentModel, paymentView);
                                    paymentUpdate.setVisible(true);
                            }

                            }
                        } else if (paymentController.getPayment(paymentID) != null) {
                            // Trường hợp paymentID tồn tại
                            PaymentModel paymentModel = paymentController.getPayment(paymentID);
                            PaymentUpdate paymentUpdate = new PaymentUpdate(paymentModel, paymentView);
                            paymentUpdate.setVisible(true);
                        } else {
                            // Trường hợp khác (có thể thêm xử lý tùy nhu cầu)
                            JOptionPane.showMessageDialog(null, "Vui lòng chọn thông tin hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        // Xử lý lỗi truy vấn SQL
                        JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi truy vấn dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    // Trường hợp chưa chọn hàng nào
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng trong bảng để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
                    System.out.println(row);
                    if(row >= 0){
                        Object payIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                        String payID = payIdObj.toString();

                           try {
                               if(!payID.equals("Chưa có giao dịch")){
                                   boolean isSuccess =  paymentController.deletePayment(payID);
                                   if(isSuccess){
                                       JOptionPane.showMessageDialog(null, "Xóa thanh toán thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                       paymentView.loadCustomerData();
                                   } else {
                                       JOptionPane.showMessageDialog(null, "Không thể xóa!", "Failed", JOptionPane.ERROR_MESSAGE);
                                   }
                               } else {
                                   JOptionPane.showMessageDialog(null, "Không thể xóa vì người dùng chưa thực hiện giao dịch!", "Failed", JOptionPane.ERROR_MESSAGE);
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
