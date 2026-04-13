/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.utils;

import java.util.ResourceBundle;

/**
 *
 * @author hungvu
 */
public class MessageUtil {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static String get(String key) {
        return bundle.getString(key);
    }
}
