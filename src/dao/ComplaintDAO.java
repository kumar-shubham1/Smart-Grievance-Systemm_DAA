package dao;

import model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // 🔹 INSERT
    public boolean insertComplaint(Complaint c, String username) {

        String sql = "INSERT INTO complaints(title, description, category, severity, urgency, impact, priority, status, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getCategory());
            ps.setInt(4, c.getSeverity());
            ps.setInt(5, c.getUrgency());
            ps.setInt(6, c.getImpact());
            ps.setDouble(7, c.getPriority());
            ps.setString(8, "NEW");
            ps.setString(9, username);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔹 GET ALL FOR USER
    public List<Complaint> getComplaintsByUser() {

        List<Complaint> list = new ArrayList<>();
        
        boolean isAdmin = false;
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            if (e.getClassName().endsWith("AdminUI")) {
                isAdmin = true;
                break;
            }
        }

        String sql = isAdmin ? "SELECT * FROM complaints ORDER BY created_at DESC" 
                             : "SELECT * FROM complaints WHERE username=? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!isAdmin) {
                ps.setString(1, System.getProperty("user.name"));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 GET BY ID
    public Complaint getComplaintById(int id) {
        String sql = "SELECT * FROM complaints WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 DELETE
    public boolean deleteComplaint(int id) {

        String sql = "DELETE FROM complaints WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔹 GET BY TEAM
    public List<Complaint> getComplaintsByTeam(String category) {

        List<Complaint> list = new ArrayList<>();

        String sql = "SELECT * FROM complaints WHERE category=? ORDER BY priority DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 UPDATE STATUS
    public boolean updateStatus(int id, String status) {

        String sql = "UPDATE complaints SET status=?, updated_at=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 🔹 SEARCH
    public List<Complaint> searchByTitle(String title, String category) {

        List<Complaint> list = new ArrayList<>();

        String sql = "SELECT * FROM complaints WHERE title LIKE ? AND category=? ORDER BY priority DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + title + "%");
            ps.setString(2, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔥 COMMON METHOD
    private Complaint mapRow(ResultSet rs) throws SQLException {

        Complaint c = new Complaint();

        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setCategory(rs.getString("category"));
        c.setSeverity(rs.getInt("severity"));
        c.setUrgency(rs.getInt("urgency"));
        c.setImpact(rs.getInt("impact"));
        c.setPriority(rs.getDouble("priority"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at")); // 🔥 NEW

        return c;
    }
}