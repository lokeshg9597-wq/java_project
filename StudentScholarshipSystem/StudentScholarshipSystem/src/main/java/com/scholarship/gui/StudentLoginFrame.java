package com.scholarship.gui;

import com.scholarship.dao.StudentDAO;

import javax.swing.*;
import java.awt.*;

public class StudentLoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public StudentLoginFrame() {

        setTitle("Student Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));

        panel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        emailField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("LOGIN");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> loginStudent());
    }

    private void loginStudent() {

        String email = emailField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter email and password!"
            );

            return;
        }

        StudentDAO dao = new StudentDAO();

        int studentId = dao.loginStudent(email, password);

        if (studentId != -1) {

            JOptionPane.showMessageDialog(
                    this, "Student Login Successful!"
            );

            new StudentDashboard(studentId).setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Invalid email or password!"
            );
        }
    }
}
