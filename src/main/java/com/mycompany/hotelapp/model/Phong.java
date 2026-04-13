package com.mycompany.hotelapp.model;
import java.io.Serializable;

public class Phong implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhong;
    private String soPhong;
    private String loai;
    private double gia;
    private String maKS;

    public Phong(String maPhong, String soPhong, String loai, double gia, String maKS) {
        this.maPhong = maPhong;
        this.soPhong = soPhong;
        this.loai    = loai;
        this.gia     = gia;
        this.maKS    = maKS;
    }

    public Phong() {}

    public String getMaPhong() { return maPhong; }
    public String getSoPhong() { return soPhong; }
    public String getLoai()    { return loai; }
    public double getGia()     { return gia; }
    public String getMaKS()    { return maKS; }

    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    public void setLoai(String loai)       { this.loai = loai; }
    public void setGia(double gia)         { this.gia = gia; }
    public void setMaKS(String maKS)       { this.maKS = maKS; }

    @Override
    public String toString() {
        return maPhong + " | Phòng " + soPhong + " | " + loai + " | " + gia + "đ";
    }
}