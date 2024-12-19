package custome;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class IssueTableRenderer extends DefaultTableCellRenderer {
    private static final String DELETED = "DELETED";
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Lấy giá trị từ cột thứ 5, kiểm tra null trước khi gọi toString()
        Object employeeValue = table.getModel().getValueAt(row, 5);
        String employee = (employeeValue != null) ? employeeValue.toString() : "";

        if (employee.contains(DELETED)) {
            c.setForeground(Color.RED);  // Màu xám cho nhân viên đã xóa
            c.setFont(c.getFont().deriveFont(Font.ITALIC));
        } else {
            c.setForeground(Color.BLACK); // Bình thường cho nhân viên còn tồn tại
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
        }
        return c;
    }
}
