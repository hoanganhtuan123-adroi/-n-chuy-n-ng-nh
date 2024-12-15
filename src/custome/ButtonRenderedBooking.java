package custome;

import controller.BookingController;
import model.BookingModel;
import view.BookingComponent.BookingDetail;
import view.BookingComponent.BookingUpdate;
import view.BookingComponent.BookingView;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonRenderedBooking extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private JPanel panel;
    private JButton updateButton, detailButton, deleteButton;
    private JTable table;
    private BookingController bookingController;
    private BookingModel bookingModel;
    private BookingDetail bookingDetail;
    private BookingUpdate bookingUpdate;
    public ButtonRenderedBooking(JTable table){
        this.table = table;
        this.bookingController = new BookingController();
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
                if (row >= 0) {
                    Object bookingIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int bookingID = (bookingIdObj instanceof Integer) ? (Integer) bookingIdObj : Integer.parseInt(bookingIdObj.toString());
                    try {
                        bookingModel = bookingController.getBooking(bookingID);
                        bookingDetail = new BookingDetail(bookingModel);
                        bookingDetail.setVisible(true);
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
                if (row >= 0) {
                    Object bookingIdObj = table.getValueAt(row, 0);
                    int bookingID = (bookingIdObj instanceof Integer) ? (Integer) bookingIdObj : Integer.parseInt(bookingIdObj.toString());
                    try {
                        bookingModel = bookingController.getBooking(bookingID);
                        bookingUpdate = new BookingUpdate(bookingModel);
                        bookingUpdate.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
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