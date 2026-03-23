package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ComplaintDAO;
import model.User;
import java.sql.*;

public class TeamUI {

    public TeamUI(User user) {

        JFrame frame = new JFrame("Team Panel - " + user.getRole());
        frame.setSize(600, 400);
        frame.setLayout(null);

        String[] cols = {"ID","Title","Category","Priority","Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 550, 200);
        frame.add(scroll);

        String[] statuses = {"IN_PROGRESS", "RESOLVED", "ESCALATED"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        statusBox.setBounds(20, 240, 150, 25);
        frame.add(statusBox);

        JButton updateBtn = new JButton("Update Status");
        updateBtn.setBounds(200, 240, 150, 30);
        frame.add(updateBtn);

        JTextField searchField = new JTextField();
        searchField.setBounds(20, 290, 200, 25);
        frame.add(searchField);

        JButton searchBtn = new JButton("Search by Title");
        searchBtn.setBounds(240, 290, 180, 30);
        frame.add(searchBtn);

        ComplaintDAO dao = new ComplaintDAO();

        try {
            ResultSet rs = dao.getComplaintsByTeam(user.getRole());

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getDouble("priority"),
                        rs.getString("status")
                });
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        updateBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(frame,"Select a complaint!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String newStatus = (String) statusBox.getSelectedItem();

            dao.updateStatus(id, newStatus);

            model.setValueAt(newStatus, row, 4);
        });

        searchBtn.addActionListener(e -> {

            String keyword = searchField.getText();
            model.setRowCount(0);

            try {
                ResultSet rs = dao.searchByTitle(keyword, user.getRole());

                while(rs.next()){
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getDouble("priority"),
                            rs.getString("status")
                    });
                }

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        frame.setVisible(true);
    }
}