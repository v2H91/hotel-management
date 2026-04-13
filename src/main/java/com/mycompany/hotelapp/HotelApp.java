package com.mycompany.hotelapp;

import com.mycompany.hotelapp.view.MainFrame;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author hungvu
 */
public class HotelApp extends JFrame {

public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
