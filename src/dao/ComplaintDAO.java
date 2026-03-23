package dao;

import java.sql.*;
import model.Complaint;

public class ComplaintDAO {


    public void insertComplaint(Complaint c) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO complaints (title, description, category, severity, urgency, impact, priority, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getCategory());
            ps.setInt(4, c.getSeverity());
            ps.setInt(5, c.getUrgency());
            ps.setInt(6, c.getImpact());
            ps.setDouble(7, c.getPriority());
            ps.setString(8, "NEW");

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getComplaintsByTeam(String team) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM complaints WHERE category=? ORDER BY priority DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, team);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

  
    public void updateStatus(int id, String status) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE complaints SET status=? WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ResultSet searchByTitle(String title, String team) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM complaints WHERE title LIKE ? AND category=? ORDER BY priority DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + title + "%");
            ps.setString(2, team);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

  
    public ResultSet getComplaintsByUser() {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM complaints ORDER BY priority DESC";

            PreparedStatement ps = conn.prepareStatement(sql);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}