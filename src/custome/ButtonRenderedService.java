package custome;

import controller.EmployeeController;
import controller.ServiceController;
import controller.TourController;
import model.ServiceModel;
import model.TourModel;
import view.ServiceComponent.ServiceUpdate;
import view.TourComponent.TourDetail;
import view.TourComponent.TourUpdate;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import view.ServiceComponent.ServiceView;

public class ButtonRenderedService extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private List<TourModel> tourList;
    private JPanel panel;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;
    private ServiceController serviceController;
    private ServiceView serviceView;

    public ButtonRenderedService(JTable table, ServiceView serviceView){
       this.table = table;
        this.serviceController = new ServiceController();
        this.serviceView = serviceView;
       panel = new JPanel(new FlowLayout());

       // Create buttons
       updateButton = new JButton("Chi Tiết");
       deleteButton = new JButton("Xóa");

       // Add buttons to panel
       panel.add(updateButton);
       panel.add(deleteButton);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    ServiceModel services = null;
                    // Lấy dữ liệu từ hàng đã chọn
                    Object serviceIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int serviceID = (serviceIdObj instanceof Integer) ? (Integer) serviceIdObj : Integer.parseInt(serviceIdObj.toString());
                    services = serviceController.getItemService(serviceID);

                    ServiceUpdate serviceUpdate = new ServiceUpdate(services, serviceView);
                    serviceUpdate.setVisible(true);
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
                    if(row >= 0){
                        Object serviceIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                        int serviceID = (serviceIdObj instanceof Integer) ? (Integer) serviceIdObj : Integer.parseInt(serviceIdObj.toString());
                        try {
                            boolean isDeleted = serviceController.deleteService(serviceID);
                            if(isDeleted){
                                JOptionPane.showMessageDialog(null, "Employee deleted successfully");
                            } else {
                                JOptionPane.showMessageDialog(null, "Unable to delete employee");
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
