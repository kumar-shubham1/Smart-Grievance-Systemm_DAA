package ui;

import dao.ComplaintDAO;
import model.Complaint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
                new String[]{"ID","Title","Category","Priority","Status"},0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(Theme.normalFont());

        JScrollPane sp = new JScrollPane(table);
        main.add(sp, BorderLayout.CENTER);

        JButton loadBtn = new JButton("Load");
        JButton deleteBtn = new JButton("Delete");

        JPanel panel = new JPanel();
        panel.setBackground(Theme.PANEL);
        panel.add(loadBtn);
        panel.add(deleteBtn);

        main.add(panel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> load());
        deleteBtn.addActionListener(e -> delete());

        add(main);
        setVisible(true);
    }

    private void load() {
        model.setRowCount(0);
        List<Complaint> list = new ComplaintDAO().getComplaintsByUser();

        for (Complaint c : list) {
            model.addRow(new Object[]{
                    c.getId(), c.getTitle(),
                    c.getCategory(), c.getPriority(), c.getStatus()
            });
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row,0);
        new ComplaintDAO().deleteComplaint(id);
        model.removeRow(row);
    }
}