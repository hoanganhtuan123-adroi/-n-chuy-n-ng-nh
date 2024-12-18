/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.TourController;
import custome.ButtonRenderedTour;
import model.TourModel;
import view.TourComponent.TourAddNew;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * @author admin
 */
public class TourView extends javax.swing.JFrame {

    /**
     * Creates new form TourView
     */
    private TourController tourController;

    public TourView() throws SQLException {
        tourController = new TourController();
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loadData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnAddNew = new javax.swing.JButton();
        jBoxSearch = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý tour");
        jTableModel = new DefaultTableModel(new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
        },
                new String [] {
                        "Tour ID ", "Package ID", "Tên Tour", "Điểm đến", "Ngày đi", "Ngày kết thúc", "Điểm xuất phát", "Giá", "Tên gói", "Action"
                }) {
            Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                    false, true, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        jTable1.setModel(jTableModel);

        jTable1.getColumn("Action").setCellRenderer(new ButtonRenderedTour(jTable1, TourView.this));
        jTable1.getColumn("Action").setCellEditor(new ButtonRenderedTour(jTable1, TourView.this));


        jTable1.setRowHeight(40);

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(20); // Tour ID
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(40); // Package ID
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(180); // Tour Name
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80); // Desti
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(80); // Start
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(80); // End
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(80); // Dep
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(65); // Price
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(100); // PC_NAME
        jTable1.getColumnModel().getColumn(9).setPreferredWidth(230); // Action

        // Căn giữa cho tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Áp dụng centerRenderer cho tất cả các cột
        for (int i = 0; i < jTable1.getColumnCount() - 1; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        jScrollPane1.setViewportView(jTable1);

        jTextField1.setToolTipText("");
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTours();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTours();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTours();
            }

            private void searchTours() {
                // Gọi hàm tìm kiếm vào Model (Controller) với sự kiểm tra ngoại lệ
                try {
                    handleSearchTour();
                } catch (SQLException ex) {
                    // Xử lý lỗi, có thể hiển thị thông báo hoặc làm gì đó
                    JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleSearchTour();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnAddNew.setText("Thêm mới");
        btnAddNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TourAddNew tourAddNew = new TourAddNew(TourView.this);
                    tourAddNew.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        jBoxSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tên tour", "Điểm đến", "Tên gói" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(jLabel3)
                    .addComponent(btnAddNew)
                    .addComponent(jBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void handleSearchTour() throws SQLException {
        String searchName = jTextField1.getText().trim();
        switch (jBoxSearch.getSelectedItem().toString().trim()) {
            case "Tên tour" : setTableData(tourController.searchToursByName(searchName)); break;
            case "Điểm đến" : setTableData(tourController.searchToursByDestination(searchName)); break;
            case "Tên gói" : setTableData(tourController.searchToursByPackage(searchName)); break;
            default: break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TourView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TourView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TourView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TourView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TourView().setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setTableData(List<TourModel> tours) {
        jTableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (TourModel tour : tours) {
            Object[] row = {
                    tour.getTour_id(),
                    tour.getPackage_id(),
                    tour.getTour_name(),
                    tour.getDestination(),
                    tour.getStart_date(),
                    tour.getEnd_date(),
                    tour.getDeparture_location(),
                    String.format("%,d", tour.getPrices()) + " VND",
                    tour.getPackageName(),
            };
            jTableModel.addRow(row);
        }
    }

    public void loadData() throws SQLException {
        List<TourModel> tours = tourController.getAllTours();
        setTableData(tours);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> jBoxSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.table.DefaultTableModel jTableModel;
    // End of variables declaration//GEN-END:variables
}
