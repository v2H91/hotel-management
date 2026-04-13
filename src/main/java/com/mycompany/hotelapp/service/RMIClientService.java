/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author hungvu
 */
public class RMIClientService {
    private static IHotelService service;

    public static IHotelService getService() {
        if (service == null) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1099);
                service = (IHotelService) registry.lookup("HotelService");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return service;
    }
}