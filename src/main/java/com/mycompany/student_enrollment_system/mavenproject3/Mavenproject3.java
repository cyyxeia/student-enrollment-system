package com.mycompany.student_enrollment_system.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Mavenproject3 extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Enrollment table model and table
    private DefaultTableModel enrollmentModel;
    private JTable enrollmentTable;

    public Mavenproject3() {
        setTitle("Student Portal");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header (Arial Bold) above the nav panel
        JLabel header = new JLabel("Pamantasan ng Lungsod ng Maynila", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));

        // Navigation bar
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton homeBtn = new JButton("Home");
        JButton enrollmentBtn = new JButton("Enrollment");
        JButton scheduleBtn = new JButton("Schedule");
        navPanel.add(homeBtn);
        navPanel.add(enrollmentBtn);
        navPanel.add(scheduleBtn);

        // Stack header + separator + nav panel
        JSeparator sep = new JSeparator();
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(header);
        topPanel.add(sep);
        topPanel.add(navPanel);

        // CardLayout for content
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels
        JPanel homePanel = buildHomePanel();
        JPanel enrollmentPanel = buildEnrollmentPanel();
        JPanel schedulePanel = buildSchedulePanel();

        // Add panels to card stack
        mainPanel.add(homePanel, "Home");
        mainPanel.add(enrollmentPanel, "Enrollment");
        mainPanel.add(schedulePanel, "Schedule");

        // Button actions
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        enrollmentBtn.addActionListener(e -> cardLayout.show(mainPanel, "Enrollment"));
        scheduleBtn.addActionListener(e -> cardLayout.show(mainPanel, "Schedule"));

        // Layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Welcome to Home", SwingConstants.CENTER);
        welcome.setFont(new Font("Inter", Font.PLAIN, 16));
        panel.add(welcome, BorderLayout.CENTER);
        return panel;
    }

    // Enrollment Panel with JTable + CRUD (editable rows)
    private JPanel buildEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table columns for enrollment
        String[] columns = {"Subject Code", "Units", "Subject Title", "Professor", "Time"};
        enrollmentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // all cells editable
            }
        };
        enrollmentTable = new JTable(enrollmentModel);
        enrollmentTable.setFillsViewportHeight(true);
        enrollmentTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // CRUD buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton insertBtn = new JButton("Insert");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        insertBtn.addActionListener(e -> insertRow());
        updateBtn.addActionListener(e -> updateRow());
        deleteBtn.addActionListener(e -> deleteRow());

        buttonPanel.add(insertBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Schedule Panel (simple JTable; expand columns as needed)
    private JPanel buildSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Day", "Time", "Subject"};
        DefaultTableModel scheduleModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // editable as well
            }
        };
        JTable scheduleTable = new JTable(scheduleModel);
        scheduleTable.setRowHeight(24);

        // Sample row to show structure
        scheduleModel.addRow(new Object[]{"Mon/Wed", "09:00–10:30", "Data Structures"});

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Insert a sample row (you can replace with input dialogs later)
    private void insertRow() {
        enrollmentModel.addRow(new Object[]{
            "CS101", "3", "Intro to CS", "Prof. Santos", "MWF 9:00–10:00"
        });
    }

    // Update selected row with sample data
    private void updateRow() {
        int row = enrollmentTable.getSelectedRow();
        if (row != -1) {
            enrollmentModel.setValueAt("CS201", row, 0);
            enrollmentModel.setValueAt("4", row, 1);
            enrollmentModel.setValueAt("Data Structures", row, 2);
            enrollmentModel.setValueAt("Prof. Cruz", row, 3);
            enrollmentModel.setValueAt("TTh 1:00–2:30", row, 4);
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to update!");
        }
    }

    // Delete selected row
    private void deleteRow() {
        int row = enrollmentTable.getSelectedRow();
        if (row != -1) {
            enrollmentModel.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Mavenproject3().setVisible(true));
    }
}
