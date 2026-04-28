package ui;

import dao.ComplaintDAO;
import model.Complaint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserUI extends JFrame {

    JTextField titleField;
    JTextArea descArea;
    JComboBox<String> categoryBox;
    JComboBox<String> severityBox, urgencyBox, impactBox;

    JTable table;
    DefaultTableModel tableModel;

    public UserUI() {

        setTitle("User Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        // 🔝 HEADER
        JLabel heading = new JLabel("User Dashboard", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(240, 240, 240));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        mainPanel.add(heading, BorderLayout.NORTH);

        // 🧩 LEFT PANEL (FORM)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(50, 50, 60));
        formPanel.setPreferredSize(new Dimension(320, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Helper for labels
        java.util.function.Function<String, JLabel> label = text -> {
            JLabel l = new JLabel(text);
            l.setForeground(new Color(220, 220, 220));
            l.setFont(labelFont);
            return l;
        };

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(label.apply("Title"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(180, 28));
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(label.apply("Description"), gbc);

        gbc.gridx = 1;
        descArea = new JTextArea(3, 15);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(Color.WHITE);
        descArea.setForeground(Color.BLACK);
        formPanel.add(new JScrollPane(descArea), gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(label.apply("Category"), gbc);

        gbc.gridx = 1;
        categoryBox = new JComboBox<>(new String[]{"IT", "Maintenance", "Service"});
        formPanel.add(categoryBox, gbc);

        String[] values = {"1","2","3","4","5","6","7","8","9","10"};

        // Severity
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(label.apply("Severity"), gbc);

        gbc.gridx = 1;
        severityBox = new JComboBox<>(values);
        formPanel.add(severityBox, gbc);

        // Urgency
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(label.apply("Urgency"), gbc);

        gbc.gridx = 1;
        urgencyBox = new JComboBox<>(values);
        formPanel.add(urgencyBox, gbc);

        // Impact
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(label.apply("Impact"), gbc);

        gbc.gridx = 1;
        impactBox = new JComboBox<>(values);
        formPanel.add(impactBox, gbc);

        // 🔘 BUTTONS
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBackground(new Color(0, 123, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(100, 100, 100));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(50, 50, 60));
        btnPanel.add(submitBtn);
        btnPanel.add(refreshBtn);

        formPanel.add(btnPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // 📋 TABLE
        String[] cols = {"ID","Title","Category","Priority","Status","Created","Updated"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 60, 70));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // 🔘 SUBMIT
        submitBtn.addActionListener(e -> {

            if (titleField.getText().isEmpty() || descArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            Complaint c = new Complaint();

            c.setTitle(titleField.getText());
            c.setDescription(descArea.getText());
            c.setCategory(categoryBox.getSelectedItem().toString());

            int severity = Integer.parseInt(severityBox.getSelectedItem().toString());
            int urgency = Integer.parseInt(urgencyBox.getSelectedItem().toString());
            int impact = Integer.parseInt(impactBox.getSelectedItem().toString());

            c.setSeverity(severity);
            c.setUrgency(urgency);
            c.setImpact(impact);

            double priority = (severity * 0.5) + (urgency * 0.3) + (impact * 0.2);
            c.setPriority(priority);

            new ComplaintDAO().insertComplaint(c);

            JOptionPane.showMessageDialog(this, "Complaint Submitted!");

            clearForm();
            loadTable();
        });

        // 🔄 REFRESH
        refreshBtn.addActionListener(e -> loadTable());

        loadTable();

        setVisible(true);
    }

    // 🔄 LOAD TABLE
    private void loadTable() {
        try {
            tableModel.setRowCount(0);

            List<Complaint> list = new ComplaintDAO().getComplaintsByUser();

            for (Complaint c : list) {
                tableModel.addRow(new Object[]{
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
    }

    // 🧹 CLEAR FORM
    private void clearForm() {
        titleField.setText("");
        descArea.setText("");
        severityBox.setSelectedIndex(0);
        urgencyBox.setSelectedIndex(0);
        impactBox.setSelectedIndex(0);
    }
}