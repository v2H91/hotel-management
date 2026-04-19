/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hotelapp.service;

import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.model.Phong;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author hungvu
 */
public interface IHotelService extends Remote {
    // Khách sạn
    List<KhachSan> getAllKhachSan() throws RemoteException;
    String addKhachSan(KhachSan ks) throws RemoteException;
    String updateKhachSan(KhachSan ks) throws RemoteException;
    String deleteKhachSan(String maKS) throws RemoteException;

    // Phòng
    List<Phong> getAllPhong() throws RemoteException;
    List<Phong> getPhongByKhachSan(String maKS) throws RemoteException;
    String addPhong(Phong p) throws RemoteException;
    String deletePhong(String maPhong) throws RemoteException;
    String updatePhong(Phong p) throws RemoteException;
}
