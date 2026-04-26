package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import dao.ComplaintDAO;
import model.Complaint;
import model.User;

public class TeamUI extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    JComboBox<String> statusBox;

    public TeamUI(User user) {

        setTitle("Team Dashboard - " + user.getRole());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔝 Title
        JLabel heading = new JLabel("Team Dashboard", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(heading, BorderLayout.NORTH);

        // 📋 TABLE
        String[] cols = {"ID","Title","Category","Priority","Status","Created","Updated"};
        model = new DefaultTableModel(cols, 0);

        table = new JTable(model) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 🔘 BOTTOM PANEL
        JPanel panel = new JPanel();

        String[] statuses = {"IN_PROGRESS", "RESOLVED", "ESCALATED"};
        statusBox = new JComboBox<>(statuses);

        JButton updateBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");

        searchField = new JTextField(12);
        JButton searchBtn = new JButton("Search");

        panel.add(new JLabel("Status:"));
        panel.add(statusBox);
        panel.add(updateBtn);
        panel.add(refreshBtn);
        panel.add(searchField);
        panel.add(searchBtn);

        add(panel, BorderLayout.SOUTH);

        ComplaintDAO dao = new ComplaintDAO();

        // 🔄 LOAD FUNCTION
        Runnable loadData = () -> {
            try {
                model.setRowCount(0);

                List<Complaint> list = dao.getComplaintsByTeam(user.getRole());

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
        };

        // Initial load
        loadData.run();

        // 🔄 REFRESH
        refreshBtn.addActionListener(e -> loadData.run());

        // 🔘 UPDATE STATUS
        updateBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a complaint first!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String status = statusBox.getSelectedItem().toString();

            dao.updateStatus(id, status);

            JOptionPane.showMessageDialog(this, "Status Updated!");

            loadData.run(); // 🔥 auto refresh
        });

        // 🔍 SEARCH
        searchBtn.addActionListener(e -> {

            try {
                String title = searchField.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter title to search!");
                    return;
                }

                model.setRowCount(0);

                List<Complaint> list = dao.searchByTitle(title, user.getRole());

                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No results found!");
                    return;
                }

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

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}