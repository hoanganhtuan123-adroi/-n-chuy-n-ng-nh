package custome;

import controller.IssueController;
import controller.ServiceController;
import model.IssueModel;
import model.ServiceModel;
import model.TourModel;
import view.IssuesComponent.IssueUpdate;
import view.ServiceComponent.ServiceUpdate;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ButtonRenderedIssues extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private List<TourModel> tourList;
    private JPanel panel;
    private JButton updateButton;
    private JTable table;
    private ServiceController serviceController;

    public ButtonRenderedIssues(JTable table){
        this.table = table;
        this.serviceController = new ServiceController();
        panel = new JPanel(new FlowLayout());

        // Create buttons
        updateButton = new JButton("Update");

        // Add buttons to panel
        panel.add(updateButton);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    IssueModel issues = null;
                    Object issueIdObj = table.getValueAt(row, 0); // Giả sử cột đầu tiên là serviceId
                    int issueID = (issueIdObj instanceof Integer) ? (Integer) issueIdObj : Integer.parseInt(issueIdObj.toString());
                    issues = IssueController.getIssueById(issueID);
                    IssueUpdate issueUpdate = new IssueUpdate(issues);
                    issueUpdate.setVisible(true);
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
