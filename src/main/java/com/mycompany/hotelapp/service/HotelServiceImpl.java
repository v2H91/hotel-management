package com.mycompany.hotelapp.service;

import com.mycompany.hotelapp.dao.KhachSanDAO;
import com.mycompany.hotelapp.dao.PhongDAO;
import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.model.Phong;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of the RMI Interface
 *
 * @author hungvu
 */
public class HotelServiceImpl extends UnicastRemoteObject implements IHotelService {

    private KhachSanDAO ksDAO = new KhachSanDAO();
    private PhongDAO pDAO = new PhongDAO();

    public HotelServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String updatePhong(Phong p) throws RemoteException {
        try {
             pDAO.update(p);  
             return "SUCCESS";
        } catch (SQLException e) {
            throw new RemoteException("Database Error (getAllKS): " + e.getMessage());
        }
    }

    // --- QUẢN LÝ KHÁCH SẠN ---
    @Override
    public List<KhachSan> getAllKhachSan() throws RemoteException {
        try {
            return ksDAO.getAll();
        } catch (SQLException e) {
            throw new RemoteException("Database Error (getAllKS): " + e.getMessage());
        }
    }

    @Override
    public String addKhachSan(KhachSan ks) throws RemoteException {
        try {
            ksDAO.add(ks);
            return "SUCCESS";
        } catch (SQLException e) {
            return "ADD_ERROR: " + e.getMessage();
        }
    }

    @Override
    public String updateKhachSan(KhachSan ks) throws RemoteException {
        try {
            ksDAO.update(ks);
            return "SUCCESS";
        } catch (SQLException e) {
            return "UPDATE_ERROR: " + e.getMessage();
        }
    }

    @Override
    public String deleteKhachSan(String maKS) throws RemoteException {
        try {
            // Kiểm tra ràng buộc: Nếu khách sạn còn phòng thì không cho xóa
            if (pDAO.hasRoom(maKS)) {
                return "FAIL_HAS_ROOM";
            }
            ksDAO.delete(maKS);
            return "SUCCESS";
        } catch (SQLException e) {
            return "DELETE_ERROR: " + e.getMessage();
        }
    }

    // --- QUẢN LÝ PHÒNG ---
    @Override
    public List<Phong> getAllPhong() throws RemoteException {
        try {
            return pDAO.getAll();
        } catch (SQLException e) {
            throw new RemoteException("Database Error (getAllPhong): " + e.getMessage());
        }
    }

    @Override
    public List<Phong> getPhongByKhachSan(String maKS) throws RemoteException {
        try {
            return pDAO.getByKhachSan(maKS);
        } catch (SQLException e) {
            throw new RemoteException("Database Error (getPhongByKS): " + e.getMessage());
        }
    }

    @Override
    public String addPhong(Phong p) throws RemoteException {
        try {
            pDAO.add(p);
            return "SUCCESS";
        } catch (SQLException e) {
            return "ADD_PHONG_ERROR: " + e.getMessage();
        }
    }

    @Override
    public String deletePhong(String maPhong) throws RemoteException {
        try {
            pDAO.delete(maPhong);
            return "SUCCESS";
        } catch (SQLException e) {
            return "DELETE_PHONG_ERROR: " + e.getMessage();
        }
    }
}
