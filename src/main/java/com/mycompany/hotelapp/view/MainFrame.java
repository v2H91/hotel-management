/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.view;

import com.mycompany.hotelapp.constant.AppColors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

   public MainFrame() {
        setTitle("Hotel Management System");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Sử dụng BorderLayout làm chủ đạo
        setLayout(new BorderLayout());

        // --- 1. HEADER (NORTH) ---
        add(createHeader(), BorderLayout.NORTH);

        // --- 2. MAIN CONTENT (CENTER) ---
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Thêm các Panel của bạn vào đây
        tabs.addTab(" Quản lý Phòng ", new PhongPanel()); 
        tabs.addTab(" Khách Sạn ", new KhachSanPanel()); // Thay bằng KhachSanPanel của bạn
        
        add(tabs, BorderLayout.CENTER);

        // --- 3. FOOTER (SOUTH) ---
        add(createFooter(), BorderLayout.SOUTH);
    }
   
   private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.PRIMARY);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(0, 0, 0, 50)));

        // Trái: Tên ứng dụng
        JLabel lblLogo = new JLabel("  HOTEL MANAGER PRO");
        lblLogo.setForeground(AppColors.TEXT_LIGHT);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(lblLogo, BorderLayout.WEST);

        // Phải: Thông tin khách hàng (Hardcode)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        userPanel.setOpaque(false);

        JLabel lblUserInfo = new JLabel("<html><div style='text-align: right;'><b>Admin:</b> Vũ Văn Hùng - K23DTCN027<br/><small>Role: Quản trị viên</small></div></html>");
        lblUserInfo.setForeground(AppColors.TEXT_LIGHT);
        
        // Avatar tròn (Dùng cách vẽ đè đã hướng dẫn trước đó)
        URL avatarUrl = getClass().getResource("/hungvv.jpg");
        JLabel lblAvatar = new JLabel();
        if (avatarUrl != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(avatarUrl).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            lblAvatar.setIcon(icon);
        } else {
            lblAvatar.setText("[Avatar]");
        }

        userPanel.add(lblUserInfo);
        userPanel.add(lblAvatar);
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(AppColors.SECONDARY);
        footer.setPreferredSize(new Dimension(0, 30));

        JLabel lblCopyright = new JLabel("  © 2026 Hotel Management System - ptit v1.0.0");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(AppColors.TEXT_LIGHT);

        JLabel lblStatus = new JLabel("Server Status: Connected RMI  ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(46, 204, 113)); // Màu xanh lá cây (Online)

        footer.add(lblCopyright, BorderLayout.WEST);
        footer.add(lblStatus, BorderLayout.EAST);

        return footer;
    }
}
