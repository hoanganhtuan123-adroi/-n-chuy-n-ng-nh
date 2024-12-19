package custome;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineTableCell extends JTextArea implements TableCellRenderer {
    public MultiLineTableCell() {
        setLineWrap(true); // Cho phép xuống dòng
        setWrapStyleWord(true); // Xuống dòng theo từ
        setOpaque(true); // Đảm bảo nền ô được hiển thị
        setMargin(new Insets(10, 10, 10, 10));
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        // Hiển thị dữ liệu của ô
        setText(value == null ? "" : value.toString());

        // Thiết lập màu sắc khi ô được chọn
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // Tự động điều chỉnh độ cao của hàng dựa trên nội dung
//        int rowHeight = (int) getPreferredSize().getHeight();
//        if (table.getRowHeight(row) < rowHeight) {
//            table.setRowHeight(row, rowHeight);
//        }

        return this;
    }

}
