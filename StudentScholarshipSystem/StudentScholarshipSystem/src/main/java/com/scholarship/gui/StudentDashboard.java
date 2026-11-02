
package com.scholarship.gui;

import com.scholarship.dao.ScholarshipDAO;
import com.scholarship.dao.ApplicationDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {

    private final int studentId;

    private JTable scholarshipTable;
    private JTable applicationTable;

    public StudentDashboard(int studentId) {

        this.studentId = studentId;

        setTitle("Student Scholarship Dashboard");

        setSize(950, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Available Scholarships",
                createScholarshipPanel());

        tabs.addTab("My Applications",
                createApplicationPanel());

        add(tabs);

    }

    private JPanel createScholarshipPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columns = {
                "ID",
                "Scholarship Name",
                "Description",
                "Income Limit",
                "Minimum Marks",
                "Amount"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        scholarshipTable = new JTable(model);

        loadScholarships(model);

        JButton applyButton = new JButton("APPLY SCHOLARSHIP");

        applyButton.addActionListener(e -> applyScholarship());

        panel.add(new JScrollPane(scholarshipTable),
                BorderLayout.CENTER);

        panel.add(applyButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadScholarships(DefaultTableModel model) {

        model.setRowCount(0);

        ScholarshipDAO dao = new ScholarshipDAO();

        List<String[]> data = dao.getAllScholarships();

        for (String[] row : data) {
            model.addRow(row);
        }
    }

    private void applyScholarship() {

        int selectedRow =
                scholarshipTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a scholarship!"
            );

            return;
        }

        int modelRow =
                scholarshipTable.convertRowIndexToModel(selectedRow);

        int scholarshipId = Integer.parseInt(
                scholarshipTable.getModel()
                        .getValueAt(modelRow, 0).toString()
        );

        String input = JOptionPane.showInputDialog(
                this,
                "Enter your academic marks (0-100):"
        );

        if (input == null) {
            return;
        }

        try {

            double marks = Double.parseDouble(input.trim());

            if (marks < 0 || marks > 100) {

                JOptionPane.showMessageDialog(
                        this,
                        "Marks must be between 0 and 100!"
                );

                return;
            }

            ApplicationDAO dao = new ApplicationDAO();

            String result = dao.applyScholarship(
                    studentId,
                    scholarshipId,
                    marks
            );

            if (result.equals("SUCCESS")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Scholarship application submitted successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        result
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric marks!"
            );
        }
    }

    private JPanel createApplicationPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {
                "Application ID",
                "Scholarship",
                "Marks",
                "Application Date",
                "Status",
                "Remarks"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        applicationTable = new JTable(model);

        loadApplications(model);

        JButton refreshButton = new JButton("REFRESH");

        refreshButton.addActionListener(
                e -> loadApplications(model)
        );

        panel.add(new JScrollPane(applicationTable),
                BorderLayout.CENTER);

        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadApplications(DefaultTableModel model) {

        model.setRowCount(0);

        ApplicationDAO dao = new ApplicationDAO();

        List<String[]> data =
                dao.getStudentApplications(studentId);

        for (String[] row : data) {
            model.addRow(row);
        }
    }
}
