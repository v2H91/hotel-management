package com.mycompany.hotelapp.model;
import java.io.Serializable;

public class KhachSan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maKS;
    private String tenKS;
    private int soSao;
    private String moTa;

    public KhachSan(String maKS, String tenKS, int soSao, String moTa) {
        this.maKS  = maKS;
        this.tenKS = tenKS;
        this.soSao = soSao;
        this.moTa  = moTa;
    }

    public KhachSan() {}

    public String getMaKS()  { return maKS; }
    public String getTenKS() { return tenKS; }
    public int getSoSao()    { return soSao; }
    public String getMoTa()  { return moTa; }

    public void setMaKS(String maKS)   { this.maKS = maKS; }
    public void setTenKS(String tenKS) { this.tenKS = tenKS; }
    public void setSoSao(int soSao)    { this.soSao = soSao; }
    public void setMoTa(String moTa)   { this.moTa = moTa; }

    @Override
    public String toString() {
        return maKS + " - " + tenKS + " (" + soSao + " sao)";
    }
}