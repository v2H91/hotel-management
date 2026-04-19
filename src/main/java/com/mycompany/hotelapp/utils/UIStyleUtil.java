/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author hungvu
 */
public class UIStyleUtil {

 public static void applyButtonStyle(JButton btn, Color color) {
    // QUAN TRỌNG: Tắt mặc định của hệ điều hành để hiển thị màu tự chọn
    btn.setContentAreaFilled(false); 
    btn.setOpaque(true); 
    
    btn.setBackground(color);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Tạo đường viền mảnh hoặc đổ bóng nhẹ để nút nhìn nổi hơn
    btn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color.darker(), 1),
        BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
}
    public static void applyLabelStyle(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(44, 62, 80)); // AppColors.TEXT_DARK
    }

}
