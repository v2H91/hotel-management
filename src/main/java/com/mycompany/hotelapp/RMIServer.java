/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp;

import com.mycompany.hotelapp.service.HotelServiceImpl;
import com.mycompany.hotelapp.service.IHotelService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author hungvu
 */
public class RMIServer {
    public static void main(String[] args) {
        try {
            IHotelService service = new HotelServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099); // Cổng mặc định RMI
            registry.rebind("HotelService", service);
            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}