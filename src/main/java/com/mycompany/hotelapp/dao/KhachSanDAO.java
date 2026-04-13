package com.mycompany.hotelapp.dao;
import com.mycompany.hotelapp.db.DBConnection;
import com.mycompany.hotelapp.model.KhachSan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachSanDAO {

    public List<KhachSan> getAll() throws SQLException {
        List<KhachSan> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachSan";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KhachSan(
                    rs.getString("maKS"),
                    rs.getString("tenKS"),
                    rs.getInt("soSao"),
                    rs.getString("moTa")
                ));
            }
        }
        return list;
    }

    public void add(KhachSan ks) throws SQLException {
        String sql = "INSERT INTO KhachSan(maKS,tenKS,soSao,moTa) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ks.getMaKS());
            ps.setString(2, ks.getTenKS());
            ps.setInt   (3, ks.getSoSao());
            ps.setString(4, ks.getMoTa());
            ps.executeUpdate();
        }
    }

    public void update(KhachSan ks) throws SQLException {
        String sql = "UPDATE KhachSan SET tenKS=?,soSao=?,moTa=? WHERE maKS=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ks.getTenKS());
            ps.setInt   (2, ks.getSoSao());
            ps.setString(3, ks.getMoTa());
            ps.setString(4, ks.getMaKS());
            ps.executeUpdate();
        }
    }

    public void delete(String maKS) throws SQLException {
        String sql = "DELETE FROM KhachSan WHERE maKS=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKS);
            ps.executeUpdate();
        }
    }
}