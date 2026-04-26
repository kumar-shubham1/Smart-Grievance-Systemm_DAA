package ui;

import dao.ComplaintDAO;
import dao.DBConnection;
import model.Complaint;
import service.TrendAnalysisService;
import service.TopKService;
import service.RabinKarpService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class AdminUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public AdminUI() {

        setTitle("Admin Dashboard");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(heading, BorderLayout.NORTH);

        String[] cols = {"ID","Title","Category","Priority","Status","Created","Updated"};
        model = new DefaultTableModel(cols, 0);

        table = new JTable(model) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton loadBtn = new JButton("Load");
        JButton resolveBtn = new JButton("Resolve");
        JButton escalateBtn = new JButton("Escalate");
        JButton deleteBtn = new JButton("Delete");
        JButton trendBtn = new JButton("Trends");
        JButton topBtn = new JButton("Top-K");
        JButton duplicateBtn = new JButton("Duplicates");

        panel.add(loadBtn);
        panel.add(resolveBtn);
        panel.add(escalateBtn);
        panel.add(deleteBtn);
        panel.add(trendBtn);
        panel.add(topBtn);
        panel.add(duplicateBtn);

        add(panel, BorderLayout.SOUTH);

        ComplaintDAO dao = new ComplaintDAO();

        // LOAD
        loadBtn.addActionListener(e -> loadAll());

        // RESOLVE
        resolveBtn.addActionListener(e -> updateStatus("RESOLVED"));

        // ESCALATE
        escalateBtn.addActionListener(e -> updateStatus("ESCALATED"));

        // DELETE (with confirmation)
        deleteBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select complaint");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this complaint?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            int id = (int) model.getValueAt(row, 0);

            dao.deleteComplaint(id);
            model.removeRow(row);

            JOptionPane.showMessageDialog(this, "Deleted successfully!");
        });

        // TRENDS
        trendBtn.addActionListener(e -> {
            try {
                int days = Integer.parseInt(JOptionPane.showInputDialog("Enter days:"));
                List<Complaint> list = getAll();

                TrendAnalysisService t = new TrendAnalysisService();
                showSimpleWindow("Last " + days + " Days", t.getComplaintsLastNDays(list, days));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        // TOP-K
        topBtn.addActionListener(e -> {
            try {
                int k = Integer.parseInt(JOptionPane.showInputDialog("Enter K:"));
                List<Complaint> list = getAll();

                TopKService t = new TopKService();
                showSimpleWindow("Top " + k, t.getTopK(list, k));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        // DUPLICATES
        duplicateBtn.addActionListener(e -> {

            List<Complaint> list = getAll();
            RabinKarpService rk = new RabinKarpService();

            String[] keys = {"internet","wifi","server","login","network"};
            List<Complaint> duplicates = new ArrayList<>();

            for (String key : keys) {
                List<Complaint> temp = new ArrayList<>();

                for (Complaint c : list) {
                    if (rk.containsPattern(c.getDescription(), key)) {
                        temp.add(c);
                    }
                }

                if (temp.size() > 1) {
                    duplicates.addAll(temp);
                }
            }

            showDuplicateWindow(duplicates, dao);
        });

        loadAll(); // 🔥 auto load
        setVisible(true);
    }

    // 🔄 UPDATE STATUS
    private void updateStatus(String status) {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a complaint!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {
            String sql = "UPDATE complaints SET status=? WHERE id=?";
            var ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Status Updated!");

            loadAll(); // 🔥 auto refresh

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD ALL
    private void loadAll() {

        try {
            model.setRowCount(0);

            List<Complaint> list = new ComplaintDAO().getComplaintsByUser();

            for (Complaint c : list) {
                model.addRow(new Object[]{
                        c.getId(),
                        c.getTitle(),
                        c.getCategory(),
                        c.getPriority(),
                        c.getStatus(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Complaint> getAll() {
        return new ComplaintDAO().getComplaintsByUser();
    }

    // SIMPLE WINDOW
    private void showSimpleWindow(String title, List<Complaint> list) {

        JFrame f = new JFrame(title);
        f.setSize(800, 400);
        f.setLocationRelativeTo(null);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Title","Category","Priority"}, 0);

        JTable t = new JTable(m) {
            public boolean isCellEditable(int r, int c) {
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

        f.add(new JScrollPane(t));
        f.setVisible(true);
    }

    // DUPLICATE WINDOW
    private void showDuplicateWindow(List<Complaint> list, ComplaintDAO dao) {

        JFrame f = new JFrame("Duplicate Complaints");
        f.setSize(800, 400);
        f.setLocationRelativeTo(null);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Title","Category","Priority"}, 0);

        JTable t = new JTable(m) {
            public boolean isCellEditable(int r, int c) {
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

        JButton deleteBtn = new JButton("Delete");

        deleteBtn.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row == -1) return;

            int id = (int) m.getValueAt(row, 0);
            dao.deleteComplaint(id);
            m.removeRow(row);
        });

        JPanel panel = new JPanel();
        panel.add(deleteBtn);

        f.add(new JScrollPane(t), BorderLayout.CENTER);
        f.add(panel, BorderLayout.SOUTH);

        f.setVisible(true);
    }
}