package com.mycompany.hotelapp.view;

import com.mycompany.hotelapp.constant.AppColors;
import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.model.Phong;
import com.mycompany.hotelapp.service.RMIClientService;
import com.mycompany.hotelapp.utils.MessageUtil;
import static com.mycompany.hotelapp.utils.UIStyleUtil.applyButtonStyle;
import static com.mycompany.hotelapp.utils.UIStyleUtil.applyLabelStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;

public class PhongPanel extends JPanel {

    private JTextField txtMaPhong, txtSoPhong, txtLoai, txtGia;
    private JComboBox<String> cboMaKS;
    private JButton btnThem, btnXoa, btnLocTheoKS, btnLamMoi, btnSua;
    private JTable table;
    private DefaultTableModel tableModel;

    public PhongPanel() {
        setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, MessageUtil.get("room.form.title"), 0, 0, new Font("Segoe UI", Font.BOLD, 14), AppColors.PRIMARY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Label & Field setup
        JLabel lblId = new JLabel(MessageUtil.get("room.label.id"));
        applyLabelStyle(lblId);
        formPanel.add(lblId);
        formPanel.add(txtMaPhong = new JTextField());

        JLabel lblNo = new JLabel(MessageUtil.get("room.label.number"));
        applyLabelStyle(lblNo);
        formPanel.add(lblNo);
        formPanel.add(txtSoPhong = new JTextField());

        JLabel lblType = new JLabel(MessageUtil.get("room.label.type"));
        applyLabelStyle(lblType);
        formPanel.add(lblType);
        formPanel.add(txtLoai = new JTextField());

        JLabel lblPrice = new JLabel(MessageUtil.get("room.label.price"));
        applyLabelStyle(lblPrice);
        formPanel.add(lblPrice);
        formPanel.add(txtGia = new JTextField());

        JLabel lblHotel = new JLabel(MessageUtil.get("room.label.hotel"));
        applyLabelStyle(lblHotel);
        formPanel.add(lblHotel);
        formPanel.add(cboMaKS = new JComboBox<>());

        // --- Button Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setOpaque(false);

        btnThem = new JButton(MessageUtil.get("room.button.add"));
        btnSua = new JButton(MessageUtil.get("room.button.edit"));
        btnXoa = new JButton(MessageUtil.get("room.button.delete"));
        btnLocTheoKS = new JButton(MessageUtil.get("room.button.filter"));
        btnLamMoi = new JButton(MessageUtil.get("room.button.refresh"));

        applyButtonStyle(btnThem, new Color(46, 204, 113));
        applyButtonStyle(btnSua, new Color(241, 196, 15));
        applyButtonStyle(btnXoa, new Color(231, 76, 60));
        applyButtonStyle(btnLocTheoKS, AppColors.PRIMARY);
        applyButtonStyle(btnLamMoi, AppColors.SECONDARY);

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLocTheoKS);
        btnPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        String[] cols = {
            MessageUtil.get("room.column.id"),
            MessageUtil.get("room.column.number"),
            MessageUtil.get("room.column.type"),
            MessageUtil.get("room.column.price"),
            MessageUtil.get("room.column.hotel_id")
        };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(52, 152, 219, 100)); // Màu chọn hàng nhẹ
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Style Header của Table
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.SECONDARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadKhachSanCombo();
        loadData();
        setupEvents();
    }

    private void loadKhachSanCombo() {
        try {
            // RMI: Gọi trực tiếp hàm getAllKhachSan()
            List<KhachSan> list = RMIClientService.getService().getAllKhachSan();
            cboMaKS.removeAllItems();
            if (list != null) {
                for (KhachSan ks : list) {
                    cboMaKS.addItem(ks.getMaKS() + " - " + ks.getTenKS());
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.load_hotel") + e.getMessage());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            // RMI: Gọi trực tiếp hàm getAllPhong()
            List<Phong> list = RMIClientService.getService().getAllPhong();
            if (list != null) {
                for (Phong p : list) {
                    tableModel.addRow(new Object[]{
                        p.getMaPhong(), p.getSoPhong(), p.getLoai(), p.getGia(), p.getMaKS()
                    });
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + e.getMessage());
        }
    }

    private void setupEvents() {
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMaPhong.setText(tableModel.getValueAt(row, 0).toString());
                txtMaPhong.setEditable(false);
                txtSoPhong.setText(tableModel.getValueAt(row, 1).toString());
                txtLoai.setText(tableModel.getValueAt(row, 2).toString());
                txtGia.setText(tableModel.getValueAt(row, 3).toString());

                String maKS = tableModel.getValueAt(row, 4).toString();
                for (int i = 0; i < cboMaKS.getItemCount(); i++) {
                    if (cboMaKS.getItemAt(i).startsWith(maKS)) {
                        cboMaKS.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        // Thêm phòng bằng RMI
        btnThem.addActionListener(e -> {
            try {
                String selectedKS = (String) cboMaKS.getSelectedItem();
                if (selectedKS == null) {
                    return;
                }

                String maKS = selectedKS.split(" - ")[0];
                Phong p = new Phong(
                        txtMaPhong.getText().trim(),
                        txtSoPhong.getText().trim(),
                        txtLoai.getText().trim(),
                        Double.parseDouble(txtGia.getText().trim()),
                        maKS
                );

                // RMI: Gọi hàm addPhong(p)
                String result = RMIClientService.getService().addPhong(p);
                if ("SUCCESS".equals(result)) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.add"));
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
            }
        });

        // Sửa thông tin phòng bằng RMI
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.select_room"));
                return;
            }

            try {
                String selectedKS = (String) cboMaKS.getSelectedItem();
                if (selectedKS == null) {
                    return;
                }

                String maKS = selectedKS.split(" - ")[0];
                // Lấy dữ liệu từ các TextField
                Phong p = new Phong(
                        txtMaPhong.getText().trim(),
                        txtSoPhong.getText().trim(),
                        txtLoai.getText().trim(),
                        Double.parseDouble(txtGia.getText().trim()),
                        maKS
                );

                // RMI: Gọi hàm updatePhong(p) - Hãy đảm bảo Interface RMI của bạn có hàm này
                String result = RMIClientService.getService().updatePhong(p);

                if ("SUCCESS".equals(result)) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.update"));
                    loadData(); // Tải lại bảng
                } else {
                    JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
            }
        });

        // Xóa phòng bằng RMI
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.select_room"));
                return;
            }

            String maPhong = tableModel.getValueAt(row, 0).toString();
            String confirmMsg = MessageFormat.format(MessageUtil.get("msg.confirm.delete"), maPhong);

            int confirm = JOptionPane.showConfirmDialog(
                    this, confirmMsg, MessageUtil.get("msg.dialog.confirm"),
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // RMI: Gọi hàm deletePhong(maPhong)
                    String result = RMIClientService.getService().deletePhong(maPhong);
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.delete"));
                        clearForm();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
                }
            }
        });

        // Lọc theo Khách Sạn bằng RMI
        btnLocTheoKS.addActionListener(e -> {
            String selectedKS = (String) cboMaKS.getSelectedItem();
            if (selectedKS == null) {
                return;
            }

            String maKS = selectedKS.split(" - ")[0];
            tableModel.setRowCount(0);
            try {
                // RMI: Gọi hàm getPhongByKhachSan(maKS)
                List<Phong> list = RMIClientService.getService().getPhongByKhachSan(maKS);
                if (list != null) {
                    for (Phong p : list) {
                        tableModel.addRow(new Object[]{
                            p.getMaPhong(), p.getSoPhong(), p.getLoai(), p.getGia(), p.getMaKS()
                        });
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
            }
        });

        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData();
            loadKhachSanCombo();
        });
    }

    private void clearForm() {
        txtMaPhong.setText("");
        txtMaPhong.setEditable(true);
        txtSoPhong.setText("");
        txtLoai.setText("");
        txtGia.setText("");
        table.clearSelection();
    }
}
