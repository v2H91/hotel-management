package com.mycompany.hotelapp.view;

import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.service.RMIClientService;
import com.mycompany.hotelapp.utils.MessageUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;

public class KhachSanPanel extends JPanel {

    private JTextField txtMaKS, txtTenKS, txtSoSao, txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable table;
    private DefaultTableModel tableModel;

    public KhachSanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder(MessageUtil.get("hotel.form.title")));

        formPanel.add(new JLabel(MessageUtil.get("hotel.label.id")));
        txtMaKS = new JTextField();
        formPanel.add(txtMaKS);

        formPanel.add(new JLabel(MessageUtil.get("hotel.label.name")));
        txtTenKS = new JTextField();
        formPanel.add(txtTenKS);

        formPanel.add(new JLabel(MessageUtil.get("hotel.label.stars")));
        txtSoSao = new JTextField();
        formPanel.add(txtSoSao);

        formPanel.add(new JLabel(MessageUtil.get("hotel.label.description")));
        txtMoTa = new JTextField();
        formPanel.add(txtMoTa);

        // --- Button Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnThem   = new JButton(MessageUtil.get("hotel.button.add"));
        btnSua    = new JButton(MessageUtil.get("hotel.button.edit"));
        btnXoa    = new JButton(MessageUtil.get("hotel.button.delete"));
        btnLamMoi = new JButton(MessageUtil.get("hotel.button.refresh"));
        
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // --- Table ---
        String[] columns = {
            MessageUtil.get("hotel.column.id"), 
            MessageUtil.get("hotel.column.name"), 
            MessageUtil.get("hotel.column.stars"), 
            MessageUtil.get("hotel.column.description")
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
        setupEvents();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            // RMI: Gọi trực tiếp phương thức lấy toàn bộ khách sạn
            List<KhachSan> list = RMIClientService.getService().getAllKhachSan();
            if (list != null) {
                for (KhachSan ks : list) {
                    tableModel.addRow(new Object[]{
                        ks.getMaKS(), ks.getTenKS(), ks.getSoSao(), ks.getMoTa()
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
                txtMaKS.setText(tableModel.getValueAt(row, 0).toString());
                txtTenKS.setText(tableModel.getValueAt(row, 1).toString());
                txtSoSao.setText(tableModel.getValueAt(row, 2).toString());
                txtMoTa.setText(tableModel.getValueAt(row, 3).toString());
                txtMaKS.setEditable(false);
            }
        });

        // Nút Thêm (RMI)
        btnThem.addActionListener(e -> {
            if (!validateForm()) return;
            try {
                KhachSan ks = getFormData();
                String result = RMIClientService.getService().addKhachSan(ks);
                if ("SUCCESS".equals(result)) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.add"));
                    clearForm(); loadData();
                } else {
                    JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
            }
        });

        // Nút Sửa (RMI)
        btnSua.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.select_hotel"));
                return;
            }
            if (!validateForm()) return;
            try {
                KhachSan ks = getFormData();
                String result = RMIClientService.getService().updateKhachSan(ks);
                if ("SUCCESS".equals(result)) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.edit"));
                    clearForm(); loadData();
                } else {
                    JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
            }
        });

        // Nút Xóa (RMI)
        btnXoa.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.select_hotel"));
                return;
            }
            String maKS = txtMaKS.getText();
            String confirmMsg = MessageFormat.format(MessageUtil.get("msg.confirm.delete_hotel"), maKS);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, confirmMsg, MessageUtil.get("msg.dialog.confirm"),
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String result = RMIClientService.getService().deleteKhachSan(maKS);
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, MessageUtil.get("msg.success.delete"));
                        clearForm(); loadData();
                    } else if ("FAIL_HAS_ROOM".equals(result)) {
                        JOptionPane.showMessageDialog(this, 
                            MessageUtil.get("msg.error.has_room"),
                            MessageUtil.get("msg.dialog.error"), 
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, result, MessageUtil.get("msg.dialog.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, MessageUtil.get("msg.dialog.error") + ": " + ex.getMessage());
                }
            }
        });

        btnLamMoi.addActionListener(e -> { clearForm(); loadData(); });
    }

    private KhachSan getFormData() {
        return new KhachSan(
            txtMaKS.getText().trim(),
            txtTenKS.getText().trim(),
            Integer.parseInt(txtSoSao.getText().trim()),
            txtMoTa.getText().trim()
        );
    }

    private boolean validateForm() {
        if (txtMaKS.getText().trim().isEmpty() || txtTenKS.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.empty_fields"));
            return false;
        }
        try {
            Integer.parseInt(txtSoSao.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, MessageUtil.get("msg.error.star_format"));
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaKS.setText(""); txtTenKS.setText("");
        txtSoSao.setText(""); txtMoTa.setText("");
        txtMaKS.setEditable(true);
        table.clearSelection();
    }
}