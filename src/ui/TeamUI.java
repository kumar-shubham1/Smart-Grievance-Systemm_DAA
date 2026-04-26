package ui;

import dao.ComplaintDAO;
import model.Complaint;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeamUI extends JFrame {

    JTable table;
    DefaultTableModel model;
    JComboBox<String> statusBox;
    JTextField searchField;

    public TeamUI(User user) {

        setTitle("Team Dashboard - " + user.getRole());
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);

        // 🔝 HEADER
        JLabel title = new JLabel("Team Dashboard - " + user.getRole(), SwingConstants.CENTER);
        title.setFont(Theme.titleFont());
        title.setForeground(Theme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        main.add(title, BorderLayout.NORTH);

        // 📋 TABLE
        model = new DefaultTableModel(
                new String[]{"ID","Title","Category","Priority","Status","Created","Updated"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(Theme.normalFont());

        table.getTableHeader().setFont(Theme.labelFont());
        table.getTableHeader().setBackground(new Color(60,60,70));
        table.getTableHeader().setForeground(Color.WHITE);

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        // 🔘 CONTROL PANEL
        JPanel panel = new JPanel();
        panel.setBackground(Theme.PANEL);

        statusBox = new JComboBox<>(new String[]{"IN_PROGRESS","RESOLVED","ESCALATED"});
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

        main.add(panel, BorderLayout.SOUTH);

        add(main);

        // 🔄 LOAD FUNCTION
        Runnable load = () -> loadData(user);

        load.run();

        // 🔄 REFRESH
        refreshBtn.addActionListener(e -> load.run());

        // 🔘 UPDATE STATUS
        updateBtn.addActionListener(e -> updateStatus(load));

        // 🔍 SEARCH
        searchBtn.addActionListener(e -> search(user));

        setVisible(true);
    }

    // 🔄 LOAD DATA
    private void loadData(User user) {
        try {
            model.setRowCount(0);

            List<Complaint> list =
                    new ComplaintDAO().getComplaintsByTeam(user.getRole());

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

    // 🔘 UPDATE STATUS
    private void updateStatus(Runnable reload) {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a complaint!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String status = statusBox.getSelectedItem().toString();

        new ComplaintDAO().updateStatus(id, status);

        JOptionPane.showMessageDialog(this, "Status Updated!");
        reload.run();
    }

    // 🔍 SEARCH
    private void search(User user) {
        try {
            model.setRowCount(0);

            String text = searchField.getText();

            List<Complaint> list =
                    new ComplaintDAO().searchByTitle(text, user.getRole());

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
}