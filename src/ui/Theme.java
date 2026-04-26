package ui;

import java.awt.*;
import javax.swing.*;

public class Theme {

    // 🎨 COLORS
    public static final Color BG = new Color(25, 25, 25);
    public static final Color PANEL = new Color(40, 40, 50);
    public static final Color TEXT = new Color(240, 240, 240);
    public static final Color PRIMARY = new Color(0, 123, 255);
    public static final Color SECONDARY = new Color(90, 90, 90);

    // 🔤 FONTS
    public static Font titleFont() {
        return new Font("Segoe UI", Font.BOLD, 26);
    }

    public static Font labelFont() {
        return new Font("Segoe UI", Font.BOLD, 14);
    }

    public static Font normalFont() {
        return new Font("Segoe UI", Font.PLAIN, 14);
    }

    // 🔘 BUTTON STYLE (NEW 🔥)
    public static void styleButton(JButton btn) {
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(normalFont());
    }

    // 🔤 TEXT FIELD STYLE (NEW 🔥)
    public static void styleTextField(JTextField tf) {
        tf.setFont(normalFont());
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
    }

    // 🔐 PASSWORD FIELD STYLE (NEW 🔥)
    public static void stylePasswordField(JPasswordField pf) {
        pf.setFont(normalFont());
        pf.setBackground(Color.WHITE);
        pf.setForeground(Color.BLACK);
    }
}