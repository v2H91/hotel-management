package com.mycompany.hotelapp.controller;
import com.mycompany.hotelapp.dao.KhachSanDAO;
import com.mycompany.hotelapp.dao.PhongDAO;
import com.mycompany.hotelapp.model.KhachSan;
import com.mycompany.hotelapp.model.Phong;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())) {

            String request = (String) in.readObject();
            System.out.println("Yêu cầu từ client: " + request);

            switch (request) {

                case "GET_ALL_KS": {
                    List<KhachSan> list = new KhachSanDAO().getAll();
                    out.writeObject(list);
                    break;
                }
                case "ADD_KS": {
                    KhachSan ks = (KhachSan) in.readObject();
                    new KhachSanDAO().add(ks);
                    out.writeObject("SUCCESS");
                    break;
                }
                case "UPDATE_KS": {
                    KhachSan ks = (KhachSan) in.readObject();
                    new KhachSanDAO().update(ks);
                    out.writeObject("SUCCESS");
                    break;
                }
                case "DELETE_KS": {
                    String maKS = (String) in.readObject();
                    boolean conPhong = new PhongDAO().hasRoom(maKS);
                    if (conPhong) {
                        out.writeObject("FAIL_HAS_ROOM");
                    } else {
                        new KhachSanDAO().delete(maKS);
                        out.writeObject("SUCCESS");
                    }
                    break;
                }
                case "GET_ALL_PHONG": {
                    List<Phong> list = new PhongDAO().getAll();
                    out.writeObject(list);
                    break;
                }
                case "GET_PHONG_BY_KS": {
                    String maKS = (String) in.readObject();
                    List<Phong> list = new PhongDAO().getByKhachSan(maKS);
                    out.writeObject(list);
                    break;
                }
                case "ADD_PHONG": {
                    Phong p = (Phong) in.readObject();
                    new PhongDAO().add(p);
                    out.writeObject("SUCCESS");
                    break;
                }
                case "DELETE_PHONG": {
                    String maPhong = (String) in.readObject();
                    new PhongDAO().delete(maPhong);
                    out.writeObject("SUCCESS");
                    break;
                }
                default:
                    out.writeObject("UNKNOWN_REQUEST");
            }

        } catch (Exception e) {
            System.out.println("Lỗi xử lý client: " + e.getMessage());
        }
    }
}