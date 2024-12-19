package custome;

import controller.IssueController;
import model.IssueModel;
import view.IssuesComponent.IssueUpdate;
import view.IssuesComponent.IssuesView;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonRenderedIssues extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private JPanel panel;
    private JButton updateButton;
    private JTable table;
    private IssuesView issuesView;

    public ButtonRenderedIssues(JTable table, IssuesView issuesView) {
        this.table = table;
        this.issuesView = issuesView;
        // Tạo panel chứa các nút
        panel = new JPanel(new FlowLayout());
        updateButton = new JButton("Cập Nhập");

        // Thêm nút vào panel
        panel.add(updateButton);

        // Xử lý sự kiện khi nhấn nút Update
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    try {
                        // Lấy ID từ cột đầu tiên (giả sử chứa Issue ID)
                        Object issueIdObj = table.getValueAt(row, 0);
                        if (issueIdObj == null) {
                            JOptionPane.showMessageDialog(null, "Issue ID is missing!");
                            return;
                        }

                        // Chuyển đổi Issue ID sang kiểu Integer
                        String issueID = issueIdObj.toString();

                        // Lấy thông tin Issue từ controller
                        IssueModel issues = IssueController.getIssueById(issueID);
                        if (issues == null) {
                            JOptionPane.showMessageDialog(null, "Issue not found!");
                            return;
                        }

                        // Mở giao diện cập nhật Issue
                        IssueUpdate issueUpdate = new IssueUpdate(issues, issuesView);
                        issueUpdate.setVisible(true);

                        // Dừng chỉnh sửa để cập nhật bảng
                        fireEditingStopped();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        panel.setBackground(table.getSelectionBackground()); // Cập nhật màu nền khi được chọn
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        // Không gọi fireEditingStopped() ở đây để tránh vòng lặp
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }
}
