package ui;

import javax.swing.*;
import dao.UserDAO;
import model.User;

public class LoginUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setLayout(null);

        JTextField userField = new JTextField();
        userField.setBounds(80, 30, 150, 25);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(80, 70, 150, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 110, 100, 30);

        frame.add(new JLabel("User")).setBounds(20,30,60,25);
        frame.add(userField);
        frame.add(new JLabel("Pass")).setBounds(20,70,60,25);
        frame.add(passField);
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {

            String username = userField.getText();
            String password = new String(passField.getPassword());

            UserDAO dao = new UserDAO();
            User user = dao.login(username, password);

            if(user == null){
                JOptionPane.showMessageDialog(frame,"Invalid login");
            } else {

                frame.dispose();

                String role = user.getRole().trim().toUpperCase();

           
                System.out.println("Role: " + role);

                switch(role) {
                    case "ADMIN":
                        new AdminUI();
                        break;

                    case "USER":
                        new UserUI(user);
                        break;

                    case "IT":
                    case "MAINTENANCE":
                    case "SERVICE":
                        new TeamUI(user);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Unknown role: " + role);
                }
            }
        });

        frame.setVisible(true);
    }
}