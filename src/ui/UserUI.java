package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ComplaintDAO;
import model.Complaint;
import java.sql.*;

public class UserUI {

    public UserUI(model.User user) {

        JFrame frame = new JFrame("User Panel");
        frame.setSize(500, 520);
        frame.setLayout(null);
 
        JLabel titleLabel = new JLabel("Title");
        titleLabel.setBounds(20, 20, 100, 25);
        frame.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(150, 20, 250, 25);
        frame.add(titleField);

        JLabel descLabel = new JLabel("Description");
        descLabel.setBounds(20, 60, 100, 25);
        frame.add(descLabel);

        JTextArea descArea = new JTextArea();
        descArea.setBounds(150, 60, 250, 60);
        frame.add(descArea);

        JLabel catLabel = new JLabel("Category");
        catLabel.setBounds(20, 140, 100, 25);
        frame.add(catLabel);

        String[] categories = {"IT", "Maintenance", "Service"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(150, 140, 250, 25);
        frame.add(categoryBox);

        JLabel severityLabel = new JLabel("Severity (1-10)");
        severityLabel.setBounds(20, 180, 120, 25);
        frame.add(severityLabel);

        JLabel urgencyLabel = new JLabel("Urgency (1-10)");
        urgencyLabel.setBounds(20, 210, 120, 25);
        frame.add(urgencyLabel);

        JLabel impactLabel = new JLabel("Impact (1-10)");
        impactLabel.setBounds(20, 240, 120, 25);
        frame.add(impactLabel);

        Integer[] range = {1,2,3,4,5,6,7,8,9,10};

        JComboBox<Integer> severityBox = new JComboBox<>(range);
        severityBox.setBounds(150, 180, 80, 25);
        frame.add(severityBox);

        JComboBox<Integer> urgencyBox = new JComboBox<>(range);
        urgencyBox.setBounds(150, 210, 80, 25);
        frame.add(urgencyBox);

        JComboBox<Integer> impactBox = new JComboBox<>(range);
        impactBox.setBounds(150, 240, 80, 25);
        frame.add(impactBox);

        JButton submitBtn = new JButton("Submit Complaint");
        submitBtn.setBounds(150, 280, 180, 30);
        frame.add(submitBtn);

        JButton viewBtn = new JButton("View My Complaints");
        viewBtn.setBounds(150, 320, 180, 30);
        frame.add(viewBtn);

        String[] cols = {"ID","Title","Category","Priority","Status"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 360, 450, 120);
        scroll.setVisible(false); 
        frame.add(scroll);

        submitBtn.addActionListener(e -> {

            String title = titleField.getText();
            String desc = descArea.getText();
            String category = (String) categoryBox.getSelectedItem();

            int severity = (int) severityBox.getSelectedItem();
            int urgency = (int) urgencyBox.getSelectedItem();
            int impact = (int) impactBox.getSelectedItem();

            double priority = (severity + urgency + impact) / 3.0;

            Complaint c = new Complaint();
            c.setTitle(title);
            c.setDescription(desc);
            c.setCategory(category);
            c.setSeverity(severity);
            c.setUrgency(urgency);
            c.setImpact(impact);
            c.setPriority(priority);

            ComplaintDAO dao = new ComplaintDAO();
            dao.insertComplaint(c);

            JOptionPane.showMessageDialog(frame, "Complaint Submitted!");

        
            titleField.setText("");
            descArea.setText("");

            scroll.setVisible(false);
        });

        
        viewBtn.addActionListener(e -> {

            scroll.setVisible(true); 
            tableModel.setRowCount(0);

            ComplaintDAO dao = new ComplaintDAO();
            ResultSet rs = dao.getComplaintsByUser();

            try {
                while(rs.next()){
                    tableModel.addRow(new Object[]{
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