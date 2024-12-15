package custome;

import controller.IssueController;
import controller.ServiceController;
import controller.SupplierController;
import model.IssueModel;
import model.SupplierModel;
import model.TourModel;
import view.IssuesComponent.IssueUpdate;
import view.SupplierComponent.SupplierDetail;
import view.SupplierComponent.SupplierUpdate;
import view.SupplierComponent.SupplierView;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ButtonRenderedSupplier extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private  SupplierController supplierController;
    private  SupplierModel supplierModel;
    private SupplierDetail supplierDetail;
    private SupplierUpdate supplierUpdate;
    private List<TourModel> tourList;
    private JPanel panel;
    private JButton detailButton, updateButton;
    private JTable table;
    private ServiceController serviceController;
    private SupplierView supplierView;
    public ButtonRenderedSupplier(JTable table, SupplierView supplierView) throws SQLException {
        this.table = table;
        this.serviceController = new ServiceController();
        this.supplierView = supplierView;
        panel = new JPanel(new FlowLayout());

        // Create buttons
        detailButton = new JButton("Chi tiết");
        updateButton = new JButton("Cập nhập");
        // Add buttons to panel
        panel.add(detailButton);
        panel.add(updateButton);

        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    Object supplierIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int supplierID = (supplierIdObj instanceof Integer) ? (Integer) supplierIdObj : Integer.parseInt(supplierIdObj.toString());
                    supplierController = new SupplierController();
                    supplierModel = supplierController.getSupplier(supplierID);
                    supplierDetail = new SupplierDetail(supplierModel);
                    supplierDetail.setVisible(true);

                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if(row >=0 ){
                    Object supplierIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int supplierID = (supplierIdObj instanceof Integer) ? (Integer) supplierIdObj : Integer.parseInt(supplierIdObj.toString());
                    supplierController = new SupplierController();
                    supplierModel = supplierController.getSupplier(supplierID);
                    supplierUpdate = new SupplierUpdate(supplierModel, supplierView);
                    supplierUpdate.setVisible(true);
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
