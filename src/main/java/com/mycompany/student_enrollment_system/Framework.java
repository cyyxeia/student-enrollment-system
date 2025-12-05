/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.student_enrollment_system;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
/**
 *
 * @author 星野一歌
 */
public class Framework {
    private Connection connection;
    private boolean isConnected=false;
    private Login login;
    private homePage homepage;
    private studentdashboard sdb;
    private enrollment2 enrollment;
    private schedule sched;
    private subject_grade subjectGrade;
    private collegeandcourses collegeCourses;
    private faculty_profs_employee faculty;
    private boolean isLoggedIn=false;
    private String accountUsername;
    private String accountPassword; //I know it's unsafe, ignore
    private String accountLevel;
    ImageIcon JOPIcon = new ImageIcon(getClass().getResource("/com/mycompany/student_enrollment_system/images/plmsmalllogo.png"));
    
    public void startDatabaseConnection(String connectionUrl, String connectionUsername, String connectionPassword) {
        try {connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
        isConnected=true;requiredOptions();System.out.println("Connection to database has been established.");
        }
        catch (SQLException e) {e.printStackTrace();isConnected=false;
        System.out.println("Connection to database failed.");}
    }
    public void loginOpen() {
        login = new Login(this);
        login.setExtendedState(JFrame.MAXIMIZED_BOTH); login.setVisible(true);
        login.userNotPassIncorrectTextVisibility(false, false);
        System.out.println("Opened login window");
    }
    public void loginCheck(String loginUsername, String loginPassword) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT username, password, account_level FROM ACCOUNT WHERE username = ?")) {
            ps.setString(1, loginUsername); 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString("password").equals(loginPassword)) {
                        accountUsername = rs.getString("username"); accountPassword = rs.getString("password"); accountLevel = rs.getString(3);
                        isLoggedIn = true;
                        System.out.println("Account logged in");
                        openHomepage();
                        login.dispose();
                    } else {isLoggedIn = false;System.out.println("Incorrect password");
                        login.userNotPassIncorrectTextVisibility(false, true);
                        login.clearFields();
                    }
                } else {isLoggedIn = false;System.out.println("Username not found");
                    login.userNotPassIncorrectTextVisibility(true, false);
                    login.clearFields();
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public void requiredOptions() {
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }
            System.out.println("Set auto commit to false and read-only to false.");
        }
        catch (SQLException e) {e.printStackTrace();}
        
    }
    public void commit() {
        try {
            connection.commit();System.out.println("Commit");
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public void rollback() {
        try {
            connection.rollback();System.out.println("Rollback");
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection to database has been closed.");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Database is null or closed already.");}
    }
    public void openHomepage() {
        homepage = new homePage(this);
        homepage.setExtendedState(JFrame.MAXIMIZED_BOTH); homepage.setVisible(true);
        System.out.println("Opened homepage window");
    }
    public void openStudent() {
        sdb = new studentdashboard(this);
        sdb.setExtendedState(JFrame.MAXIMIZED_BOTH); sdb.setVisible(true);
        System.out.println("Opened student dashboard window");
    }
    public void openEnrollment() {
        enrollment = new enrollment2(this);
        enrollment.setExtendedState(JFrame.MAXIMIZED_BOTH); enrollment.setVisible(true);
        System.out.println("Opened enrollment window");
    }
    public void openSchedule() {
        sched = new schedule(this);
        sched.setExtendedState(JFrame.MAXIMIZED_BOTH); sched.setVisible(true);
        System.out.println("Opened schedule window");
    }
    public void openSubjectAndGrade() {
        subjectGrade = new subject_grade(this);
        subjectGrade.setExtendedState(JFrame.MAXIMIZED_BOTH); subjectGrade.setVisible(true);
        System.out.println("Opened subject and grade window");
    }
    public void openCollegeAndCourse() {
        collegeCourses = new collegeandcourses(this);
        collegeCourses.setExtendedState(JFrame.MAXIMIZED_BOTH); collegeCourses.setVisible(true);
        System.out.println("Opened college and courses window");
    }
    public void openFaculty() {
        faculty = new faculty_profs_employee(this);
        faculty.setExtendedState(JFrame.MAXIMIZED_BOTH); faculty.setVisible(true);
        loadFaculty();
        System.out.println("Opened faculty window");
    }
    public void loadFaculty() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT emp.employee_id, emp.last_name||', '||emp.first_name, col.college_code, emp.gender, emp.birthday FROM employee emp INNER JOIN subject sub ON emp.employee_id = sub.employee_id INNER JOIN college col ON sub.college_code = col.college_code WHERE emp.deleted = 'F'")) {
            try (ResultSet rs = ps.executeQuery()) {
                faculty.clearTable();
                while (rs.next()) {
                    faculty.loadTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
                }
                System.out.println("Faculty table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load faculty");}
    }
    public void loadCollegeAndCourse() {
        
    }
    public void addNewRow(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int columnCount = model.getColumnCount();

        Object[] inputs = new Object[columnCount];
        Object[] message = new Object[columnCount * 2];

        for (int i = 0; i < columnCount; i++) {

            String colName = model.getColumnName(i);

            // Date picker
            if (colName.equalsIgnoreCase("Birthday") || colName.equalsIgnoreCase("Date")) {

                UtilDateModel dateModel = new UtilDateModel();
                Properties p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");

                JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
                JDatePickerImpl datePicker =
                        new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());

                inputs[i] = datePicker;
            }

            // Gender combobox
            else if (colName.equalsIgnoreCase("Gender")) {
                inputs[i] = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            }

            // Default text field
            else {
                inputs[i] = new JTextField();
            }

            message[i * 2] = colName + ":";
            message[i * 2 + 1] = inputs[i];
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                "Add Row",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                JOPIcon
        );

        if (option == JOptionPane.OK_OPTION) {

            Object[] rowData = new Object[columnCount];

            for (int i = 0; i < columnCount; i++) {

                Object input = inputs[i];

                if (input instanceof JTextField) {
                    rowData[i] = ((JTextField) input).getText().trim();
                }

                else if (input instanceof JComboBox) {
                    rowData[i] = ((JComboBox<?>) input).getSelectedItem();
                }

                else if (input instanceof JDatePickerImpl) {
                    JDatePickerImpl picker = (JDatePickerImpl) input;
                    Object val = picker.getModel().getValue();

                    if (val == null) {
                        rowData[i] = null;
                    } else {
                        java.util.Date utilDate = (java.util.Date) val;
                        rowData[i] = new java.sql.Date(utilDate.getTime());
                    }
                }
            }
            boolean emptyField = false;
            for (Object o : rowData) {
                if (o == null) {
                    emptyField = true;
                    break;
                }
                if (o instanceof String && ((String) o).isEmpty()) {
                    emptyField = true;
                    break;
                }
            }
            if (!emptyField) {
                StringBuilder sql = new StringBuilder("INSERT INTO " + table.getName() + " VALUES (");
                for (int i = 0; i < rowData.length; i++) {
                    sql.append("?");
                    sql.append(", ");
                }
                sql.append("?");  
                sql.append(")");
                try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                    for (int i = 0; i < rowData.length; i++) {
                        Object val = rowData[i];
                        if (val instanceof String) ps.setString(i + 1, (String) val);
                        else if (val instanceof Integer) ps.setInt(i + 1, (Integer) val);
                        else if (val instanceof java.sql.Date) ps.setDate(i + 1, (java.sql.Date) val);
                        else ps.setObject(i + 1, val);
                    }
                    ps.setString(rowData.length + 1, "F");
                    ps.executeUpdate();
                    commit();
                    loadFaculty();
                    System.out.println("New row added");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                invalid("Invalid field inputs");
                System.out.println("Invalid field inputs");
            }
        }
    }
    public void addNewRow(String tableName) {
        try {
            // 1. Get column metadata excluding 'deleted'
            String query = "SELECT * FROM " + tableName + " WHERE 1=0"; // no data, just metadata
            int columnCount;
            String[] columnNames;

            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                columnCount = meta.getColumnCount();

                // Collect column names excluding 'deleted'
                java.util.List<String> cols = new java.util.ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String col = meta.getColumnName(i);
                    if (!col.equalsIgnoreCase("deleted")) {
                        cols.add(col);
                    }
                }
                columnNames = cols.toArray(new String[0]);
                columnCount = columnNames.length;
            }

            // 2. Build input components for each column
            Object[] inputs = new Object[columnCount];
            Object[] message = new Object[columnCount * 2];

            for (int i = 0; i < columnCount; i++) {
                String colName = columnNames[i];

                if (colName.equalsIgnoreCase("birthday") || colName.equalsIgnoreCase("date")) {
                    // Date input with JDatePicker
                    UtilDateModel dateModel = new UtilDateModel();
                    Properties p = new Properties();
                    p.put("text.today", "Today");
                    p.put("text.month", "Month");
                    p.put("text.year", "Year");
                    JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
                    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());
                    inputs[i] = datePicker;
                } else if (colName.equalsIgnoreCase("gender")) {
                    String[] genders = { "Male", "Female", "Other" };
                    JComboBox<String> comboBox = new JComboBox<>(genders);
                    inputs[i] = comboBox;
                } else {
                    inputs[i] = new JTextField();
                }

                message[i * 2] = colName + ":";
                message[i * 2 + 1] = inputs[i];
            }

            // 3. Show input dialog
            int option = JOptionPane.showConfirmDialog(
                    null,
                    message,
                    "Add Row to " + tableName,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    JOPIcon
            );

            if (option == JOptionPane.OK_OPTION) {
                Object[] rowData = new Object[columnCount];
                boolean emptyField = false;

                for (int i = 0; i < columnCount; i++) {
                    Object input = inputs[i];
                    Object value = null;

                    if (input instanceof JTextField) {
                        value = ((JTextField) input).getText().trim();
                        if (((String) value).isEmpty()) emptyField = true;
                    } else if (input instanceof JComboBox) {
                        String selected = (String) ((JComboBox<?>) input).getSelectedItem();
                        if (selected != null) {
                            switch (selected) {
                                case "Male" -> value = "M";
                                case "Female" -> value = "F";
                                case "Other" -> value = "O";
                            }
                        } else {
                            emptyField = true;  // nothing selected
                        }
                    } else if (input instanceof JDatePickerImpl) {
                        Object dateVal = ((JDatePickerImpl) input).getModel().getValue();
                        if (dateVal == null) {
                            emptyField = true;
                        } else {
                            value = new java.sql.Date(((java.util.Date) dateVal).getTime());
                        }
                    }

                    rowData[i] = value;
                }

                if (!emptyField) {
                    // 4. Build INSERT with deleted = 'F'
                    StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                    for (String col : columnNames) {
                        sql.append(col).append(", ");
                    }
                    sql.append("deleted) VALUES (");
                    for (int i = 0; i < columnCount; i++) {
                        sql.append("?, ");
                    }
                    sql.append("?") // for deleted
                       .append(")");

                    try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                        for (int i = 0; i < columnCount; i++) {
                            Object val = rowData[i];
                            if (val instanceof String) ps.setString(i + 1, (String) val);
                            else if (val instanceof Integer) ps.setInt(i + 1, (Integer) val);
                            else if (val instanceof java.sql.Date) ps.setDate(i + 1, (java.sql.Date) val);
                            else ps.setObject(i + 1, val);
                        }
                        // Set deleted = 'F'
                        ps.setString(columnCount + 1, "F");
                        ps.executeUpdate();
                        commit();  // commit if using manual commit
                        loadFaculty();        // refresh JTable
                        System.out.println("New row added");
                    }
                } else {
                    invalid("Invalid field inputs");
                    System.out.println("Invalid field inputs");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void invalid(String message) {
        String jopmessage = "<html><h1 style='color: red;'>"+message+"</h1></html>";
        JOptionPane.showMessageDialog(null, jopmessage, "Invalid", 0, JOPIcon);
    }
}
