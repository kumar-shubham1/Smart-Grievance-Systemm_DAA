package ui;

import dao.ComplaintDAO;
import dao.DBConnection;
import model.Complaint;
import service.TrendAnalysisService;
import service.TopKService;
import service.RabinKarpService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.util.*;

public class AdminUI {

    JTable table;
    DefaultTableModel model;

    public AdminUI() {

        final JFrame frame = new JFrame("Admin Panel");
        frame.setSize(1100, 500);
        frame.setLayout(new BorderLayout());

        String[] cols = {"ID","Title","Category","Priority","Status","Created At","Updated At"};

        model = new DefaultTableModel(cols, 0);

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton loadBtn = new JButton("Load");
        JButton deleteBtn = new JButton("Delete");
        JButton trendBtn = new JButton("Trends");
        JButton topBtn = new JButton("Top Complaints");
        JButton duplicateBtn = new JButton("Check Duplicates");

        panel.add(loadBtn);
        panel.add(deleteBtn);
        panel.add(trendBtn);
        panel.add(topBtn);
        panel.add(duplicateBtn);

        frame.add(panel, BorderLayout.SOUTH);

        ComplaintDAO dao = new ComplaintDAO();

        // LOAD
        loadBtn.addActionListener(e -> loadAll());

        // DELETE
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select complaint");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            dao.deleteComplaint(id);
            model.removeRow(row);

            JOptionPane.showMessageDialog(frame, "Deleted");
        });

        // TRENDS
        trendBtn.addActionListener(e -> {
            try {
                int days = Integer.parseInt(
                        JOptionPane.showInputDialog("Enter days:")
                );

                List<Complaint> list = getAll();

                TrendAnalysisService t = new TrendAnalysisService();
                List<Complaint> res = t.getComplaintsLastNDays(list, days);

                showSimpleWindow("Last " + days + " Days", res);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // TOP K
        topBtn.addActionListener(e -> {
            try {
                int k = Integer.parseInt(
                        JOptionPane.showInputDialog("Enter number:")
                );

                List<Complaint> list = getAll();

                TopKService t = new TopKService();
                List<Complaint> res = t.getTopK(list, k);

                showSimpleWindow("Top " + k, res);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // DUPLICATES (Rabin-Karp keyword based)
        duplicateBtn.addActionListener(e -> {

            List<Complaint> list = getAll();
            RabinKarpService rk = new RabinKarpService();

            String[] keywords = {"internet", "wifi", "server", "login", "network"};

            Map<String, List<Complaint>> groups = new HashMap<>();

            for (Complaint c : list) {
                String desc = c.getDescription();

                for (String key : keywords) {
                    if (rk.containsPattern(desc, key)) {
                        groups.putIfAbsent(key, new ArrayList<>());
                        groups.get(key).add(c);
                    }
                }
            }

            List<Complaint> duplicates = new ArrayList<>();

            for (String key : groups.keySet()) {
                if (groups.get(key).size() > 1) {
                    duplicates.addAll(groups.get(key));
                }
            }

            showDuplicateWindow(duplicates, dao);
        });

        frame.setVisible(true);
    }

    // LOAD ALL
    private void loadAll() {
        try {
            model.setRowCount(0);

            String sql = "SELECT * FROM complaints ORDER BY category, priority DESC";
            ResultSet rs = DBConnection.getConnection()
                    .prepareStatement(sql)
                    .executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getDouble("priority"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL FROM DB
    private List<Complaint> getAll() {

        List<Complaint> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM complaints";
            ResultSet rs = DBConnection.getConnection()
                    .prepareStatement(sql)
                    .executeQuery();

            while (rs.next()) {
                Complaint c = new Complaint();

                c.setId(rs.getInt("id"));
                c.setTitle(rs.getString("title"));
                c.setDescription(rs.getString("description"));
                c.setCategory(rs.getString("category"));
                c.setPriority(rs.getDouble("priority"));
                c.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // SIMPLE WINDOW
    private void showSimpleWindow(String title, List<Complaint> list) {

        JFrame f = new JFrame(title);
        f.setSize(900, 400);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Title","Category","Priority"}, 0);

        JTable t = new JTable(m);

        for (Complaint c : list) {
            m.addRow(new Object[]{
                    c.getId(),
                    c.getTitle(),
                    c.getCategory(),
                    c.getPriority()
            });
        }

        f.add(new JScrollPane(t));
        f.setVisible(true);
    }

    // DUPLICATE WINDOW WITH SHOW DESCRIPTION
    private void showDuplicateWindow(List<Complaint> list, ComplaintDAO dao) {

        JFrame f = new JFrame("Duplicate Complaints");
        f.setSize(900, 400);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Title","Category","Priority"}, 0);

        JTable t = new JTable(m) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Complaint c : list) {
            m.addRow(new Object[]{
                    c.getId(),
                    c.getTitle(),
                    c.getCategory(),
                    c.getPriority()
            });
        }

        JButton showBtn = new JButton("Show Description");
        JButton deleteBtn = new JButton("Delete Selected");

        // SHOW DESCRIPTION
        showBtn.addActionListener(e -> {

            int row = t.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(f, "Select a complaint first!");
                return;
            }

            int id = (int) m.getValueAt(row, 0);

            try {
                String sql = "SELECT description FROM complaints WHERE id=?";
                var ps = DBConnection.getConnection().prepareStatement(sql);
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String desc = rs.getString("description");

                    JTextArea area = new JTextArea(desc);
                    area.setLineWrap(true);
                    area.setWrapStyleWord(true);
                    area.setEditable(false);

                    JScrollPane scroll = new JScrollPane(area);
                    scroll.setPreferredSize(new java.awt.Dimension(400, 200));

                    JOptionPane.showMessageDialog(f, scroll, "Description", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row == -1) return;

            int id = (int) m.getValueAt(row, 0);
            dao.deleteComplaint(id);
            m.removeRow(row);
        });

        JPanel panel = new JPanel();
        panel.add(showBtn);
        panel.add(deleteBtn);

        f.add(new JScrollPane(t), BorderLayout.CENTER);
        f.add(panel, BorderLayout.SOUTH);

        f.setVisible(true);
    }
}