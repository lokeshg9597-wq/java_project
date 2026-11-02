
package com.scholarship.gui;

import com.scholarship.dao.StudentDAO;

import javax.swing.*;
import java.awt.*;

public class StudentRegistrationFrame extends JFrame {

    private JTextField nameField;
    private JTextField registerField;
    private JTextField departmentField;
    private JTextField yearField;
    private JTextField emailField;
    private JTextField incomeField;
    private JPasswordField passwordField;

    public StudentRegistrationFrame() {

        setTitle("Student Registration");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 12));
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        nameField = new JTextField();
        registerField = new JTextField();
        departmentField = new JTextField();
        yearField = new JTextField();
        emailField = new JTextField();
        incomeField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Register Number:"));
        panel.add(registerField);

        panel.add(new JLabel("Department:"));
        panel.add(departmentField);

        panel.add(new JLabel("Year:"));
        panel.add(yearField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Family Income:"));
        panel.add(incomeField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton registerButton = new JButton("REGISTER");

        panel.add(new JLabel(""));
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(e -> registerStudent());
    }

    private void registerStudent() {

        try {

            String name = nameField.getText().trim();
            String registerNo = registerField.getText().trim();
            String department = departmentField.getText().trim();
            String email = emailField.getText().trim();
            String password =
                    new String(passwordField.getPassword());

            if (name.isEmpty() || registerNo.isEmpty()
                    || department.isEmpty() || email.isEmpty()
                    || yearField.getText().trim().isEmpty()
                    || incomeField.getText().trim().isEmpty()
                    || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please fill all fields!"
                );
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(
                        this, "Enter a valid email address!"
                );
                return;
            }

            if (password.length() < 8) {
                JOptionPane.showMessageDialog(
                        this, "Password must have at least 8 characters!"
                );
                return;
            }

            int year = Integer.parseInt(yearField.getText().trim());
            double income = Double.parseDouble(incomeField.getText().trim());

            if (year < 1 || year > 6 || income < 0) {
                JOptionPane.showMessageDialog(
                        this, "Enter valid year and family income!"
                );
                return;
            }

            StudentDAO dao = new StudentDAO();

            boolean success = dao.registerStudent(
                    name,
                    registerNo,
                    department,
                    year,
                    email,
                    income,
                    password
            );

            if (success) {

                JOptionPane.showMessageDialog(
                        this, "Registration Successful!"
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. Check duplicate register number or email."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Year and income must be valid numbers!"
            );

        }
    }
}
