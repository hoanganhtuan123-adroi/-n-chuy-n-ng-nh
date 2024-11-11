package custome;

import controller.TourController;
import model.CustomerModel;
import model.TourModel;
import view.CustomerDetail;
import view.CustomerUpdate;
import view.TourComponent.TourDetail;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                System.out.println("update button clicked");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete button clicked");
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
