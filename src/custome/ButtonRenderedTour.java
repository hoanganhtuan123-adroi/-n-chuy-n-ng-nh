package custome;

import controller.TourController;
import model.CustomerModel;
import model.TourModel;
import view.CustomerDetail;
import view.CustomerUpdate;
import view.TourComponent.TourDetail;
import view.TourComponent.TourUpdate;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ButtonRenderedTour extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private List<TourModel> tourList;
    private JPanel panel;
    private JButton detailButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;
    private TourController tourController;

    public ButtonRenderedTour(JTable table){
       this.table = table;
        this.tourController = new TourController();
       panel = new JPanel(new FlowLayout());

       // Create buttons
       detailButton = new JButton("Detail");
       updateButton = new JButton("Update");
       deleteButton = new JButton("Delete");

       // Add buttons to panel
       panel.add(detailButton);
       panel.add(updateButton);
       panel.add(deleteButton);

        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("detail button clicked");
                int row = table.getSelectedRow();
                if (row >= 0) {
                    TourModel tours = null;
                    // Lấy dữ liệu từ hàng đã chọn
                    Object tourIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                    int tourID = (tourIdObj instanceof Integer) ? (Integer) tourIdObj : Integer.parseInt(tourIdObj.toString());
                    tours  = tourController.showTourDetails(tourID);

                    TourDetail tourDetail = new TourDetail(tours);
                    tourDetail.setVisible(true);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    TourModel tours = null;
                    // Lấy dữ liệu từ hàng đã chọn
                    Object tourIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                    int tourID = (tourIdObj instanceof Integer) ? (Integer) tourIdObj : Integer.parseInt(tourIdObj.toString());
                    tours  = tourController.showTourDetails(tourID);

                    TourUpdate tourUpdate = new TourUpdate(tours);
                    tourUpdate.setVisible(true);
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
                       Object tourIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là customerId
                       int tourID = (tourIdObj instanceof Integer) ? (Integer) tourIdObj : Integer.parseInt(tourIdObj.toString());
                       try {
                           boolean isDeleted = tourController.deleteTour(tourID);
                           if(isDeleted){
                               JOptionPane.showMessageDialog(panel, "Tour deleted successfully");
                           } else {
                               JOptionPane.showMessageDialog(panel, "Unable to delete tour");
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
