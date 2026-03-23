package ui;

import javax.swing.*;
import util.AppContext;
import model.Complaint;

public class AdminUI {

    public AdminUI() {

        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(400, 300);
        frame.setLayout(null);

        JButton processBtn = new JButton("Process Complaint");
        processBtn.setBounds(100, 80, 200, 40);

        frame.add(processBtn);

        processBtn.addActionListener(e -> {

            Complaint c = AppContext.service.processNext();

            if (c == null) {
                JOptionPane.showMessageDialog(frame, "No complaints");
            } else {
                String team = AppContext.service.assignTeam(c);

                JOptionPane.showMessageDialog(frame,
                        "Complaint ID: " + c.getId() +
                        "\nAssigned to: " + team);
            }
        });

        frame.setVisible(true);
    }
}