
package com.scholarship.gui;

import com.scholarship.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginFrame() {

        setTitle("Admin Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));

        panel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("LOGIN");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> loginAdmin());
    }

    private void loginAdmin() {

        String username = usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter username and password!"
            );

            return;
        }

        AdminDAO dao = new AdminDAO();

        boolean success = dao.loginAdmin(username, password);

        if (success) {

            JOptionPane.showMessageDialog(
                    this, "Admin Login Successful!"
            );

            new AdminDashboard().setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Invalid admin username or password!"
            );
        }
    }
}
