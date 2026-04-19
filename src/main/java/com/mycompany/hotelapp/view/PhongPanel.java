package com.mycompany.hotelapp.view;

import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.model.Phong;
import com.mycompany.hotelapp.service.RMIClientService;
import com.mycompany.hotelapp.utils.MessageUtil;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder(MessageUtil.get("room.form.title")));

        formPanel.add(new JLabel(MessageUtil.get("room.label.id")));
        txtMaPhong = new JTextField();
        formPanel.add(txtMaPhong);

        formPanel.add(new JLabel(MessageUtil.get("room.label.number")));
        txtSoPhong = new JTextField();
        formPanel.add(txtSoPhong);

        formPanel.add(new JLabel(MessageUtil.get("room.label.type")));
        txtLoai = new JTextField();
        formPanel.add(txtLoai);

        formPanel.add(new JLabel(MessageUtil.get("room.label.price")));
        txtGia = new JTextField();
        formPanel.add(txtGia);

        formPanel.add(new JLabel(MessageUtil.get("room.label.hotel")));
        cboMaKS = new JComboBox<>();
        formPanel.add(cboMaKS);

        // --- Button Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnThem = new JButton(MessageUtil.get("room.button.add"));
        btnSua = new JButton(MessageUtil.get("room.button.edit"));
        btnXoa = new JButton(MessageUtil.get("room.button.delete"));
        btnLocTheoKS = new JButton(MessageUtil.get("room.button.filter"));
        btnLamMoi = new JButton(MessageUtil.get("room.button.refresh"));

        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnSua);
        btnPanel.add(btnLocTheoKS);
        btnPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // --- Table ---
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
