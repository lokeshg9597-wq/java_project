
package com.scholarship.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {

        setTitle("Student Scholarship Processing System");

        setSize(550, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "STUDENT SCHOLARSHIP SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(
                new GridLayout(3, 1, 15, 15)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        50, 100, 50, 100
                )
        );

        JButton studentButton =
                new JButton("STUDENT LOGIN");

        JButton adminButton =
                new JButton("ADMIN LOGIN");

        JButton registerButton =
                new JButton("STUDENT REGISTRATION");

        panel.add(studentButton);
        panel.add(adminButton);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        studentButton.addActionListener(e ->
                new StudentLoginFrame().setVisible(true)
        );

        adminButton.addActionListener(e ->
                new AdminLoginFrame().setVisible(true)
        );

        registerButton.addActionListener(e ->
                new StudentRegistrationFrame().setVisible(true)
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            WelcomeFrame frame = new WelcomeFrame();

            frame.setVisible(true);

        });
    }
}
