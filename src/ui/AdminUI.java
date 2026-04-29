package ui;

import dao.ComplaintDAO;
import model.Complaint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import service.TopKService;
import service.TrendAnalysisService;
import service.RabinKarpService;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public AdminUI() {

        setTitle("Admin Dashboard");
        setSize(1100, 600);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(Theme.titleFont());
        title.setForeground(Theme.TEXT);
        main.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[] { "ID", "Title", "Category", "Priority", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(Theme.normalFont());

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                String status = (String) table.getValueAt(row, 4);

                if ("REOPENED".equals(status)) {
                    c.setBackground(new Color(255, 200, 200)); // light red
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                }

                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int id = (int) model.getValueAt(row, 0);

                        ComplaintDAO dao = new ComplaintDAO();
                        Complaint c = dao.getComplaintById(id);

                        if (c != null && c.getDescription() != null) {
                            JOptionPane.showMessageDialog(AdminUI.this, c.getDescription(), "Complaint Description",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(AdminUI.this, "Description not found");
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        main.add(sp, BorderLayout.CENTER);

        JButton loadBtn = new JButton("Load");
        JButton topKBtn = new JButton("Top-K");
        JButton trendsBtn = new JButton("Trends");
        JButton duplicatesBtn = new JButton("Duplicates");
        JButton escalateBtn = new JButton("Escalate");
        JButton deleteBtn = new JButton("Delete");

        JPanel panel = new JPanel();
        panel.setBackground(Theme.PANEL);
        panel.add(loadBtn);
        panel.add(topKBtn);
        panel.add(trendsBtn);
        panel.add(duplicatesBtn);
        panel.add(escalateBtn);
        panel.add(deleteBtn);

        main.add(panel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> load());
        topKBtn.addActionListener(e -> showTopK());
        trendsBtn.addActionListener(e -> showTrends());
        duplicatesBtn.addActionListener(e -> checkDuplicates());
        escalateBtn.addActionListener(e -> escalateStatus());
        deleteBtn.addActionListener(e -> delete());

        add(main);
        setVisible(true);
    }

    private void load() {
        model.setRowCount(0);

        List<Complaint> list = new ComplaintDAO().getComplaintsByUser();

        for (Complaint c : list) {
            model.addRow(new Object[] {
                    c.getId(),
                    c.getTitle(),
                    c.getCategory(),
                    c.getPriority(),
                    c.getStatus()
            });
        }

        int count = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ("REOPENED".equals(model.getValueAt(i, 4))) {
                count++;
            }
        }
        setTitle("Admin Dashboard (Reopened: " + count + ")");
    }

    private void showTopK() {
        model.setRowCount(0);
        List<Complaint> list = new ComplaintDAO().getComplaintsByUser();
        TopKService topKService = new TopKService();
        List<Complaint> topK = topKService.getTopK(list, 5); // HEAP: Top 5 priority

        for (Complaint c : topK) {
            model.addRow(new Object[] {
                    c.getId(), c.getTitle(),
                    c.getCategory(), c.getPriority(), c.getStatus()
            });
        }
    }

    private void showTrends() {
        String input = JOptionPane.showInputDialog(this, "Enter number of days:");

        if (input != null && !input.isEmpty()) {
            try {
                int days = Integer.parseInt(input);

                model.setRowCount(0);
                List<Complaint> list = new ComplaintDAO().getComplaintsByUser();
                TrendAnalysisService trendService = new TrendAnalysisService();
                List<Complaint> recent = trendService.getComplaintsLastNDays(list, days);

                for (Complaint c : recent) {
                    model.addRow(new Object[] {
                            c.getId(), c.getTitle(),
                            c.getCategory(), c.getPriority(), c.getStatus()
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void checkDuplicates() {
        ComplaintDAO dao = new ComplaintDAO();

        // get selected row
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a complaint first");
            return;
        }

        // get ID
        int id = (int) model.getValueAt(row, 0);

        // fetch FULL complaint from DB
        Complaint target = dao.getComplaintById(id);

        // fetch ALL complaints from DB
        List<Complaint> all = dao.getComplaintsByUser();

        if (target != null) {
            RabinKarpService service = new RabinKarpService();
            List<Complaint> duplicates = service.getDuplicates(target, all);

            model.setRowCount(0);

            if (duplicates.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No duplicate complaints found");
            } else {
                for (Complaint c : duplicates) {
                    model.addRow(new Object[]{
                            c.getId(),
                            c.getTitle(),
                            c.getCategory(),
                            c.getPriority(),
                            c.getStatus()
                    });
                }
            }
        }
    }

    private void escalateStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint to escalate.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String currentStatus = (String) model.getValueAt(row, 4);

        String nextStatus = currentStatus;
        if ("NEW".equals(currentStatus))
            nextStatus = "IN_PROGRESS";
        else if ("IN_PROGRESS".equals(currentStatus))
            nextStatus = "RESOLVED";
        else if ("RESOLVED".equals(currentStatus))
            nextStatus = "ESCALATED";

        if (!nextStatus.equals(currentStatus)) {
            new ComplaintDAO().updateStatus(id, nextStatus);
            model.setValueAt(nextStatus, row, 4);
            JOptionPane.showMessageDialog(this, "Status escalated to: " + nextStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Complaint is already ESCALATED.");
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        int id = (int) model.getValueAt(row, 0);
        new ComplaintDAO().deleteComplaint(id);
        model.removeRow(row);
    }
}