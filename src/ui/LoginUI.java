package ui;

import dao.UserDAO;
import model.User;
import util.AppContext;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public LoginUI() {

        setTitle("Login");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 TITLE
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(Theme.titleFont());
        title.setForeground(Theme.TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // 👤 USERNAME
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(label("Username"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(180, 30));
        Theme.styleTextField(usernameField);
        panel.add(usernameField, gbc);

        // 🔐 PASSWORD
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(label("Password"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(180, 30));
        Theme.stylePasswordField(passwordField);
        panel.add(passwordField, gbc);

        // 🔘 BUTTON
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(120, 35));
        Theme.styleButton(loginBtn);

        panel.add(loginBtn, gbc);

        // 🚀 LOGIN LOGIC
        loginBtn.addActionListener(e -> login());

        add(panel);
        setVisible(true);
    }

    // 🔐 LOGIN METHOD
    private void login() {

        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username & password");
            return;
        }

        User u = new UserDAO().login(user, pass);

        if (u != null) {
            AppContext.currentUserId = u.getId();
            dispose();

            switch (u.getRole()) {
                case "ADMIN":
                    new AdminUI();
                    break;

                case "IT":
                case "Maintenance":
                case "Service":
                    new TeamUI(u);
                    break;

                default:
                    new UserUI();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Invalid Login");
        }
    }

    // 🎨 LABEL STYLE
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT);
        l.setFont(Theme.labelFont());
        return l;
    }

    // ✅ MAIN METHOD (RUN THIS FILE)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}