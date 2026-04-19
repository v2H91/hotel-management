package com.mycompany.hotelapp.dao;

import com.mycompany.hotelapp.db.DBConnection;
import com.mycompany.hotelapp.model.Phong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongDAO {

    public List<Phong> getAll() throws SQLException {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Phong(
                        rs.getString("maPhong"),
                        rs.getString("soPhong"),
                        rs.getString("loai"),
                        rs.getDouble("gia"),
                        rs.getString("maKS")
                ));
            }
        }
        return list;
    }

    public List<Phong> getByKhachSan(String maKS) throws SQLException {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong WHERE maKS=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Phong(
                        rs.getString("maPhong"),
                        rs.getString("soPhong"),
                        rs.getString("loai"),
                        rs.getDouble("gia"),
                        rs.getString("maKS")
                ));
            }
        }
        return list;
    }

    // Kiểm tra khách sạn còn phòng không — dùng trước khi xóa KS
    public boolean hasRoom(String maKS) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Phong WHERE maKS=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKS);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void add(Phong p) throws SQLException {
        String sql = "INSERT INTO Phong(maPhong,soPhong,loai,gia,maKS) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getSoPhong());
            ps.setString(3, p.getLoai());
            ps.setDouble(4, p.getGia());
            ps.setString(5, p.getMaKS());
            ps.executeUpdate();
        }
    }

    public void delete(String maPhong) throws SQLException {
        String sql = "DELETE FROM Phong WHERE maPhong=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ps.executeUpdate();
        }
    }

    public void update(Phong p) throws SQLException {
        // Câu lệnh SQL cập nhật các trường, lọc theo mã phòng
        String sql = "UPDATE Phong SET soPhong = ?, loai = ?, gia = ?, maKS = ? WHERE maPhong = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Gán các giá trị mới cho các dấu hỏi (?) theo đúng thứ tự
            ps.setString(1, p.getSoPhong());
            ps.setString(2, p.getLoai());
            ps.setDouble(3, p.getGia());
            ps.setString(4, p.getMaKS());

            // Dấu hỏi cuối cùng là điều kiện WHERE
            ps.setString(5, p.getMaPhong());

            int rowsAffected = ps.executeUpdate();

            // (Tùy chọn) Kiểm tra xem có bản ghi nào được cập nhật không
            if (rowsAffected == 0) {
                throw new SQLException("Cập nhật thất bại, không tìm thấy phòng có mã: " + p.getMaPhong());
            }
        }
    }
}
