
package com.scholarship.gui;

import com.scholarship.dao.AdminDAO;
import com.scholarship.dao.ScholarshipDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private final ScholarshipDAO scholarshipDAO = new ScholarshipDAO();

    private JTable studentTable;
    private JTable applicationTable;
    private JTable scholarshipTable;

    private DefaultTableModel studentModel;
    private DefaultTableModel applicationModel;
    private DefaultTableModel scholarshipModel;

    public AdminDashboard() {

        setTitle("Admin Dashboard - Scholarship System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Students", createStudentPanel());
        tabs.addTab("Applications", createApplicationPanel());
        tabs.addTab("Scholarships", createScholarshipPanel());

        add(tabs);

        loadStudents();
        loadApplications();
        loadScholarships();
    }

    private DefaultTableModel createModel(String[] columns) {

        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // STUDENTS PANEL
    private JPanel createStudentPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        studentModel = createModel(new String[]{
                "ID", "Name", "Register No",
                "Department", "Year", "Email", "Family Income"
        });

        studentTable = new JTable(studentModel);

        JButton refresh = new JButton("REFRESH");

        refresh.addActionListener(e -> loadStudents());

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    private void loadStudents() {

        if (studentModel == null) return;

        studentModel.setRowCount(0);

        List<String[]> data = adminDAO.getAllStudents();

        for (String[] row : data) {
            studentModel.addRow(row);
        }
    }

    // APPLICATION PANEL
    private JPanel createApplicationPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        applicationModel = createModel(new String[]{
                "Application ID", "Student",
                "Scholarship", "Marks", "Date",
                "Status", "Remarks"
        });

        applicationTable = new JTable(applicationModel);

        JButton approve = new JButton("APPROVE");
        JButton reject = new JButton("REJECT");
        JButton refresh = new JButton("REFRESH");

        approve.addActionListener(e ->
                updateStatus("APPROVED")
        );

        reject.addActionListener(e ->
                updateStatus("REJECTED")
        );

        refresh.addActionListener(e -> loadApplications());

        JPanel buttons = new JPanel();

        buttons.add(approve);
        buttons.add(reject);
        buttons.add(refresh);

        panel.add(new JScrollPane(applicationTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void loadApplications() {

        if (applicationModel == null) return;

        applicationModel.setRowCount(0);

        List<String[]> data = adminDAO.getAllApplications();

        for (String[] row : data) {
            applicationModel.addRow(row);
        }
    }

    private void updateStatus(String status) {

        int selectedRow = applicationTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this, "Select an application first!"
            );

            return;
        }

        int modelRow =
                applicationTable.convertRowIndexToModel(selectedRow);

        int applicationId = Integer.parseInt(
                applicationModel.getValueAt(modelRow, 0).toString()
        );

        String currentStatus =
                applicationModel.getValueAt(modelRow, 5).toString();

        if (!currentStatus.equalsIgnoreCase("PENDING")) {

            JOptionPane.showMessageDialog(
                    this, "This application has already been processed!"
            );

            return;
        }

        String remarks = JOptionPane.showInputDialog(
                this,
                "Enter remarks for " + status + ":"
        );

        if (remarks == null) return;

        if (remarks.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter remarks!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirm " + status + " for application " + applicationId + "?",
                "Confirm Decision",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = adminDAO.updateApplicationStatus(
                applicationId,
                status,
                remarks.trim()
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this, "Application " + status + " successfully!"
            );

            loadApplications();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Unable to update. Refresh and check the application."
            );
        }
    }

    // SCHOLARSHIP PANEL
    private JPanel createScholarshipPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        scholarshipModel = createModel(new String[]{
                "ID", "Scholarship Name", "Description",
                "Income Limit", "Minimum Marks", "Amount"
        });

        scholarshipTable = new JTable(scholarshipModel);

        JButton add = new JButton("ADD");
        JButton update = new JButton("UPDATE");
        JButton delete = new JButton("DELETE");
        JButton refresh = new JButton("REFRESH");

        add.addActionListener(e -> addScholarship());
        update.addActionListener(e -> updateScholarship());
        delete.addActionListener(e -> deleteScholarship());
        refresh.addActionListener(e -> loadScholarships());

        JPanel buttons = new JPanel();

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(refresh);

        panel.add(new JScrollPane(scholarshipTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void loadScholarships() {

        if (scholarshipModel == null) return;

        scholarshipModel.setRowCount(0);

        List<String[]> data =
                scholarshipDAO.getAllScholarships();

        for (String[] row : data) {
            scholarshipModel.addRow(row);
        }
    }

    private void addScholarship() {

        JTextField name = new JTextField();
        JTextField description = new JTextField();
        JTextField income = new JTextField();
        JTextField marks = new JTextField();
        JTextField amount = new JTextField();

        Object[] fields = {
                "Scholarship Name:", name,
                "Description:", description,
                "Income Limit:", income,
                "Minimum Marks:", marks,
                "Amount:", amount
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Add Scholarship",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {

            if (name.getText().trim().isEmpty()
                    || description.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Name and description are required.");
            }

            double incomeValue = Double.parseDouble(income.getText().trim());
            double marksValue = Double.parseDouble(marks.getText().trim());
            double amountValue = Double.parseDouble(amount.getText().trim());

            if (incomeValue < 0 || marksValue < 0 ||
                    marksValue > 100 || amountValue <= 0) {
                throw new IllegalArgumentException("Enter valid scholarship values.");
            }

            boolean success = adminDAO.addScholarship(
                    name.getText().trim(),
                    description.getText().trim(),
                    incomeValue,
                    marksValue,
                    amountValue
            );

            JOptionPane.showMessageDialog(
                    this,
                    success ? "Scholarship added!" : "Unable to add scholarship."
            );

            loadScholarships();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter valid numeric values!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateScholarship() {

        int selected = scholarshipTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a scholarship!");
            return;
        }

        int row = scholarshipTable.convertRowIndexToModel(selected);

        try {

            int id = Integer.parseInt(
                    scholarshipModel.getValueAt(row, 0).toString()
            );

            JTextField name = new JTextField(
                    scholarshipModel.getValueAt(row, 1).toString()
            );

            JTextField description = new JTextField(
                    scholarshipModel.getValueAt(row, 2).toString()
            );

            JTextField income = new JTextField(
                    scholarshipModel.getValueAt(row, 3).toString()
            );

            JTextField marks = new JTextField(
                    scholarshipModel.getValueAt(row, 4).toString()
            );

            JTextField amount = new JTextField(
                    scholarshipModel.getValueAt(row, 5).toString()
            );

            Object[] fields = {
                    "Scholarship Name:", name,
                    "Description:", description,
                    "Income Limit:", income,
                    "Minimum Marks:", marks,
                    "Amount:", amount
            };

            int result = JOptionPane.showConfirmDialog(
                    this, fields, "Update Scholarship",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result != JOptionPane.OK_OPTION) return;

            if (name.getText().trim().isEmpty()
                    || description.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Name and description are required.");
            }

            double incomeValue = Double.parseDouble(income.getText().trim());
            double marksValue = Double.parseDouble(marks.getText().trim());
            double amountValue = Double.parseDouble(amount.getText().trim());

            if (incomeValue < 0 || marksValue < 0 ||
                    marksValue > 100 || amountValue <= 0) {
                throw new IllegalArgumentException("Enter valid scholarship values.");
            }

            boolean success = adminDAO.updateScholarship(
                    id,
                    name.getText().trim(),
                    description.getText().trim(),
                    incomeValue,
                    marksValue,
                    amountValue
            );

            JOptionPane.showMessageDialog(
                    this,
                    success ? "Scholarship updated!" : "Update failed."
            );

            loadScholarships();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter valid numeric values!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteScholarship() {

        int selected = scholarshipTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a scholarship!");
            return;
        }

        int row = scholarshipTable.convertRowIndexToModel(selected);

        int id = Integer.parseInt(
                scholarshipModel.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this scholarship?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = adminDAO.deleteScholarship(id);

        JOptionPane.showMessageDialog(
                this,
                success ? "Scholarship deleted!" :
                        "Delete failed. Existing applications may reference it."
        );

        loadScholarships();
    }
}
