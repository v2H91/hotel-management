/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.view;

import com.mycompany.hotelapp.utils.MessageUtil;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

   public MainFrame() {
        // Thay "Quản Lý Khách Sạn - TCP/IP" bằng code:
        setTitle(MessageUtil.get("main.title"));
        
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        initUI();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();
        
        // Thay các tiêu đề Tab bằng code:
        tabs.addTab(MessageUtil.get("main.tab.hotel"), new KhachSanPanel());
        tabs.addTab(MessageUtil.get("main.tab.room"), new PhongPanel());

        add(tabs);
    }
}
