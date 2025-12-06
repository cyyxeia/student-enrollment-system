/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.student_enrollment_system;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import org.jdatepicker.impl.*;
public class Framework {
    private Connection connection;
    private boolean isConnected=false;
    private Login login;
    private Student sdb;
    private Enrollment enrollment;
    private Subject subject;
    private CollegeCourse collegeCourses;
    private Faculty faculty;
    private boolean isLoggedIn=false;
    private String accountUsername;
    private String accountPassword; //I know it's unsafe, ignore
    private String accountLevel;
    ImageIcon JOPIcon = new ImageIcon(getClass().getResource("/com/mycompany/student_enrollment_system/images/plmsmalllogo.png"));

    public void startDatabaseConnection(String connectionUrl, String connectionUsername, String connectionPassword) {
        try {connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
        isConnected=true;
        requiredOptions();
        System.out.println("Connection to database has been established.");
        }
        catch (SQLException e) {e.printStackTrace();isConnected=false;
        System.out.println("Connection to database failed.");}
    }
    public void loginOpen() {
        login = new Login(this);
        login.setExtendedState(JFrame.MAXIMIZED_BOTH);
        login.setIconImage(JOPIcon.getImage());
        login.setVisible(true);
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
                        openStudent();
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
    public void openStudent() {
        sdb = new Student(this);
        sdb.setExtendedState(JFrame.MAXIMIZED_BOTH); sdb.setVisible(true);
        sdb.setIconImage(JOPIcon.getImage());
        loadStudent();
        System.out.println("Opened student dashboard window");
    }
    public void openEnrollment() {
        enrollment = new Enrollment(this);
        enrollment.setExtendedState(JFrame.MAXIMIZED_BOTH); enrollment.setVisible(true);
        enrollment.setIconImage(JOPIcon.getImage());
        loadEnrollment();
        System.out.println("Opened enrollment window");
    }
    public void openSubject() {
        subject = new Subject(this);
        subject.setExtendedState(JFrame.MAXIMIZED_BOTH); subject.setVisible(true);
        subject.setIconImage(JOPIcon.getImage());
        loadSubject();
        System.out.println("Opened subject window");
    }
    public void openCollegeAndCourse() {
        collegeCourses = new CollegeCourse(this);
        collegeCourses.setExtendedState(JFrame.MAXIMIZED_BOTH); collegeCourses.setVisible(true);    
        loadCollegeAndCourse();
        System.out.println("Opened college and courses window");
    }
    public void openFaculty() {
        faculty = new Faculty(this);
        faculty.setExtendedState(JFrame.MAXIMIZED_BOTH); faculty.setVisible(true);
        faculty.setIconImage(JOPIcon.getImage());
        loadFaculty();
        System.out.println("Opened faculty window");
    }
    public void loadFaculty() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT employee_id, e_lastname||', '||e_firstname, birthday, gender FROM employee WHERE deleted = 'F'")) {
            try (ResultSet rs = ps.executeQuery()) {
                faculty.clearTable();
                while (rs.next()) {
                    faculty.loadTable(rs.getString(1), rs.getString(2), rs.getDate(3), rs.getString(4));
                }
                System.out.println("Faculty table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load faculty");}
    }
    public void loadCollegeAndCourse() {
        collegeCourses.clearTable();
        try (PreparedStatement psCollege = connection.prepareStatement("SELECT col.college_code, col.college_title, col.status FROM COLLEGE col WHERE deleted='F'")) {
            try (ResultSet rsCollege = psCollege.executeQuery()) {
                while (rsCollege.next()) {
                    collegeCourses.loadTable(rsCollege.getString(1),rsCollege.getString(2),rsCollege.getString(3));
                }
                System.out.println("College table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load college");}
        try (PreparedStatement psCourse = connection.prepareStatement("SELECT cor.program_code, cor.college_code, cor.program_title, cor.status FROM CPROGRAM cor WHERE deleted='F'")) {
            try (ResultSet rsCourse = psCourse.executeQuery()) {
                while (rsCourse.next()) {
                    collegeCourses.loadTable(rsCourse.getString(1),rsCourse.getString(2),rsCourse.getString(3),rsCourse.getString(4));
                }
                System.out.println("Courses table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load courses");}
    }
    public void loadSubject() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT sub.subject_code, sub.subject_title, sub.units, sub.college_code, sub.employee_id, emp.e_lastname||', '||emp.e_firstname, sub.school_year, sub.semester, sub.day_sched, sub.start_time, sub.end_time, sub.room_no, sub.status, sub.date_started, sub.date_closed FROM SUBJECT sub INNER JOIN EMPLOYEE emp ON sub.employee_id = emp.employee_id WHERE sub.deleted='F'")) {
            try (ResultSet rs = ps.executeQuery()) {
                subject.clearTable();
                while (rs.next()) {
                    subject.loadTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getDate(14), rs.getDate(15));
                }
                System.out.println("Subject table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load faculty");}
    }
    public void loadEnrollment() {
       try (PreparedStatement ps = connection.prepareStatement("SELECT enr.student_id, stu.s_lastname||', '||stu.s_firstname||' '||stu.s_middlename, enr.block_no, enr.subject_code, sub.subject_title, enr.student_type, enr.date_enrolled, enr.grade FROM ENROLLMENT enr INNER JOIN STUDENT stu ON enr.student_id = stu.student_id INNER JOIN SUBJECT sub ON sub.subject_code = enr.subject_code WHERE enr.deleted='F'")) {
          try (ResultSet rs = ps.executeQuery()) {
             enrollment.clearTable();
             while (rs.next()) {
                enrollment.loadTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getDate(7), rs.getString(8));
             }
             System.out.println("Enrollment table loaded");
          }
       }
       catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load enrollment");}
    }
    public void loadStudent() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT student_id, s_lastname||', '||s_firstname||' '||s_middlename, email, birthday, placeofbirth, contact_no, sex, nationality, civil_status, disability, barangay||', '||municipality||', '||province||', '||region||', '||zip_code FROM STUDENT WHERE deleted='F'")) {
            try (ResultSet rs = ps.executeQuery()) {
                sdb.clearTable();
                while (rs.next()) {
                    sdb.loadTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
                }
                System.out.println("Student table loaded");
            }
        }
        catch (SQLException e) {e.printStackTrace();System.out.println("Failed to load student");}
    }
    public void addNewRow(String tableName) {
        if (accountLevel.equals("student")) {
            invalid("You cannot add a row here as student.");
            return;
        } else if (accountLevel.equals("faculty") && !tableName.toUpperCase().equals("STUDENT")) {
            invalid("You cannot add a row here as faculty.");
            return;
        }
        try {
            String query = "SELECT * FROM " + tableName + " WHERE 1=0";
            int columnCount;
            String[] columnNames;

            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                ResultSetMetaData meta = rs.getMetaData();
                java.util.List<String> cols = new java.util.ArrayList<>();

                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String col = meta.getColumnName(i);
                    if (!col.equalsIgnoreCase("deleted")) cols.add(col);
                }

                columnNames = cols.toArray(new String[0]);
                columnCount = columnNames.length;
            }

            Object[] inputs = new Object[columnCount];
            Object[] message = new Object[columnCount * 2];

            for (int i = 0; i < columnCount; i++) {
                String colName = columnNames[i];

                switch (colName.toLowerCase()) {

                    case "units" -> {
                        JComboBox<String> combo = new JComboBox<>(new String[]{"1", "2", "3"});
                        inputs[i] = combo;
                    }

                    case "school_year" -> {
                        JComboBox<String> yearStart = new JComboBox<>();
                        int current = java.time.Year.now().getValue();

                        for (int y = current - 20; y <= current + 20; y++)
                            yearStart.addItem(String.valueOf(y));

                        JPanel yearPanel = new JPanel();
                        yearPanel.add(yearStart);
                        yearPanel.add(new JLabel("-"));
                        JLabel yearEndLabel = new JLabel(String.valueOf(current + 1));
                        yearPanel.add(yearEndLabel);

                        // Auto-update end year when start changes
                        yearStart.addActionListener(e -> {
                            int ys = Integer.parseInt((String) yearStart.getSelectedItem());
                            yearEndLabel.setText(String.valueOf(ys + 1));
                        });

                        // store both in inputs
                        inputs[i] = new Object[]{yearStart, yearEndLabel};
                        message[i * 2] = colName + ":";
                        message[i * 2 + 1] = yearPanel;
                        continue;
                    }

                    case "semester" -> {
                        JComboBox<String> combo = new JComboBox<>(new String[]{"1", "2"});
                        inputs[i] = combo;
                    }

                    case "day_sched" -> {
                        JPanel panel = new JPanel(new GridLayout(2, 4));
                        JCheckBox[] cbs = {
                            new JCheckBox("S"), new JCheckBox("M"), new JCheckBox("T"),
                            new JCheckBox("W"), new JCheckBox("Th"), new JCheckBox("F"), new JCheckBox("Su")
                        };
                        for (JCheckBox cb : cbs) panel.add(cb);
                        inputs[i] = cbs;     // store checkboxes array
                        message[i * 2 + 1] = panel;
                        message[i * 2] = colName + ":";
                        continue;
                    }

                    case "start_time", "end_time" -> {
                        JComboBox<String> hour = new JComboBox<>(new String[]{
                                "1","2","3","4","5","6","7","8","9","10","11","12"
                        });
                        JComboBox<String> min = new JComboBox<>(new String[]{
                                "00","15","30","45"
                        });
                        JComboBox<String> mer = new JComboBox<>(new String[]{"AM", "PM"});
                        JPanel timePanel = new JPanel();
                        timePanel.add(hour);
                        timePanel.add(new JLabel(":"));
                        timePanel.add(min);
                        timePanel.add(mer);

                        inputs[i] = new Object[]{hour, min, mer};
                        message[i * 2] = colName + ":";
                        message[i * 2 + 1] = timePanel;
                        continue;
                    }

                    case "birthday", "date_started", "date_closed", "date_enrolled" -> {
                        UtilDateModel dateModel = new UtilDateModel();
                        Properties p = new Properties();
                        p.put("text.today", "Today");
                        p.put("text.month", "Month");
                        p.put("text.year", "Year");
                        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
                        JDatePickerImpl picker =
                            new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());
                        inputs[i] = picker;
                    }

                    case "gender", "sex" -> {
                        JComboBox<String> c = new JComboBox<>(new String[]{"Male","Female","Other"});
                        inputs[i] = c;
                    }

                    case "status" -> {
                        JComboBox<String> c = new JComboBox<>(new String[]{"Active","Inactive"});
                        inputs[i] = c;
                    }
                    case "student_type" -> {
                        JComboBox<String> c = new JComboBox<>(new String[]{"Regular","Irregular"});
                        inputs[i] = c;
                    }
                    case "grade" -> {
                        JTextField tf = new JTextField();
                        tf.addKeyListener(new java.awt.event.KeyAdapter() {
                           @Override
                           public void keyTyped(java.awt.event.KeyEvent e) {
                              char c = e.getKeyChar();
                              if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE)
                                 e.consume();
                           }
                        });
                        inputs[i] = tf;
                     }
                    case "block_no" -> {
                        JComboBox<String> combo = new JComboBox<>(new String[]{
                           "1","2","3","4","5","6"
                        });
                        inputs[i] = combo;
                    }
                    case "civil_status" -> {
                        JComboBox<String> c = new JComboBox<>(new String[]{"Single","Married","Widowed","Separated","Divorced"});
                        inputs[i] = c;
                    }
                    default -> inputs[i] = new JTextField();
                }

                message[i * 2] = colName + ":";
                if (message[i * 2 + 1] == null) message[i * 2 + 1] = inputs[i];
            }

            int option = JOptionPane.showConfirmDialog(
                    null, message, "Add Row to " + tableName,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, JOPIcon
            );

            if (option != JOptionPane.OK_OPTION) return;

            Object[] rowData = new Object[columnCount];
            boolean emptyField = false;

            for (int i = 0; i < columnCount; i++) {
                String col = columnNames[i].toLowerCase();
                Object in = inputs[i];
                Object value = null;

                if (in instanceof JTextField tf) {
                    value = tf.getText().trim();
                    if (value.equals("")) emptyField = true;
                }
                else if (in instanceof Object[] yearObj && col.equals("school_year")) {
                    JComboBox ys = (JComboBox) yearObj[0];
                    JLabel ye = (JLabel) yearObj[1];
                    value = ys.getSelectedItem() + "-" + ye.getText();
                }
                else if (in instanceof JComboBox cb) {
                    String selected = (String) cb.getSelectedItem();
                    if (selected == null) emptyField = true;
                    else {
                        switch (col) {
                            case "gender" -> value = selected.charAt(0) + "";
                            case "sex" -> value = selected.charAt(0) + "";
                            case "status" -> value = selected.charAt(0) + "";
                            default -> value = selected;
                        }
                    }
                }

                else if (in instanceof JCheckBox[] cbs) {
                    StringBuilder sb = new StringBuilder();
                    for (JCheckBox cb : cbs) if (cb.isSelected()) {
                        if (!sb.isEmpty()) sb.append("/");
                        sb.append(cb.getText());
                    }
                    if (sb.isEmpty()) emptyField = true;
                    value = sb.toString();
                }

                else if (in instanceof Object[] time) {
                    JComboBox h = (JComboBox) time[0];
                    JComboBox m = (JComboBox) time[1];
                    JComboBox mer = (JComboBox) time[2];
                    value = h.getSelectedItem() + ":" + m.getSelectedItem() + " " + mer.getSelectedItem();
                }

                else if (in instanceof JDatePickerImpl dp) {
                    Object dv = dp.getModel().getValue();
                    if (dv == null) emptyField = true;
                    else value = new java.sql.Date(((java.util.Date) dv).getTime());
                }
                else if (in instanceof JTextField tf) {
                    value = tf.getText().trim();
                    if (value.equals("")) emptyField = true;
                }

                rowData[i] = value;
            }

            if (emptyField) {
                invalid("Invalid field inputs");
                System.out.println("Invalid field inputs");
                return;
            }

            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
            for (String col : columnNames) sql.append(col).append(", ");
            sql.append("deleted) VALUES (");
            sql.append("?, ".repeat(columnCount)).append("?)");

            try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                for (int i = 0; i < columnCount; i++)
                    ps.setObject(i + 1, rowData[i]);

                ps.setString(columnCount + 1, "F");
                ps.executeUpdate();
                commit();
                loadSelect(tableName);
                System.out.println("New row added");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            invalid("Invalid constraint or input");
        }
    }
    public void editSelectedRow(String tableName, JTable table, String idColumn) {
        if (accountLevel.equals("student")) {
            invalid("You cannot modify a row here as student.");
            return;
        } else if (accountLevel.equals("faculty") && !tableName.toUpperCase().equals("STUDENT")) {
            invalid("You cannot modify a row here as faculty.");
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;

        String id = table.getValueAt(selectedRow, table.convertColumnIndexToModel(0)).toString();

        try {
            String query = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
            int columnCount;
            String[] columnNames;

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    java.util.List<String> cols = new java.util.ArrayList<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        String col = meta.getColumnName(i);
                        if (!col.equalsIgnoreCase("deleted")) cols.add(col);
                    }
                    columnNames = cols.toArray(new String[0]);
                    columnCount = columnNames.length;

                    if (!rs.next()) return;

                    Object[] inputs = new Object[columnCount];
                    Object[] message = new Object[columnCount * 2];

                    for (int i = 0; i < columnCount; i++) {
                        String colName = columnNames[i];
                        String colLower = colName.toLowerCase();
                        Object value = rs.getObject(colName);

                        switch (colLower) {
                            case "units" -> {
                                JComboBox<String> combo = new JComboBox<>(new String[]{"1","2","3"});
                                if (value != null) combo.setSelectedItem(value.toString());
                                inputs[i] = combo;
                            }
                            case "school_year" -> {
                                JComboBox<String> yearStart = new JComboBox<>();
                                int current = java.time.Year.now().getValue();
                                for (int y = current - 20; y <= current + 20; y++) yearStart.addItem(String.valueOf(y));
                                JPanel yearPanel = new JPanel();
                                yearPanel.add(yearStart);
                                yearPanel.add(new JLabel("-"));
                                JLabel yearEndLabel = new JLabel(String.valueOf(current + 1));
                                yearPanel.add(yearEndLabel);
                                if (value != null && value.toString().contains("-")) {
                                    String[] parts = value.toString().split("-");
                                    yearStart.setSelectedItem(parts[0]);
                                    yearEndLabel.setText(parts[1]);
                                }
                                yearStart.addActionListener(e -> {
                                    int ys = Integer.parseInt((String) yearStart.getSelectedItem());
                                    yearEndLabel.setText(String.valueOf(ys + 1));
                                });
                                inputs[i] = new Object[]{yearStart, yearEndLabel};
                                message[i*2] = colName + ":";
                                message[i*2+1] = yearPanel;
                                continue;
                            }
                            case "semester" -> {
                                JComboBox<String> combo = new JComboBox<>(new String[]{"1","2"});
                                if (value != null) combo.setSelectedItem(value.toString());
                                inputs[i] = combo;
                            }
                            case "day_sched" -> {
                                JPanel panel = new JPanel(new GridLayout(2,4));
                                JCheckBox[] cbs = {
                                    new JCheckBox("S"), new JCheckBox("M"), new JCheckBox("T"),
                                    new JCheckBox("W"), new JCheckBox("Th"), new JCheckBox("F"), new JCheckBox("Su")
                                };
                                if (value != null) {
                                    String[] days = value.toString().split("/");
                                    for (JCheckBox cb : cbs) for (String d : days) if (cb.getText().equals(d)) cb.setSelected(true);
                                }
                                for (JCheckBox cb : cbs) panel.add(cb);
                                inputs[i] = cbs;
                                message[i*2] = colName + ":";
                                message[i*2+1] = panel;
                                continue;
                            }
                            case "start_time", "end_time" -> {
                                JComboBox<String> hour = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
                                JComboBox<String> min = new JComboBox<>(new String[]{"00","15","30","45"});
                                JComboBox<String> mer = new JComboBox<>(new String[]{"AM","PM"});
                                if (value != null) {
                                    String[] parts = value.toString().split("[: ]");
                                    hour.setSelectedItem(parts[0]);
                                    min.setSelectedItem(parts[1]);
                                    mer.setSelectedItem(parts[2]);
                                }
                                JPanel timePanel = new JPanel();
                                timePanel.add(hour); timePanel.add(new JLabel(":")); timePanel.add(min); timePanel.add(mer);
                                inputs[i] = new Object[]{hour, min, mer};
                                message[i*2] = colName + ":";
                                message[i*2+1] = timePanel;
                                continue;
                            }
                            case "birthday","date_started","date_closed","date_enrolled" -> {
                                UtilDateModel dateModel = new UtilDateModel();
                                if (value != null) dateModel.setValue((java.util.Date)value);
                                Properties p = new Properties();
                                p.put("text.today","Today"); p.put("text.month","Month"); p.put("text.year","Year");
                                JDatePanelImpl datePanel = new JDatePanelImpl(dateModel,p);
                                JDatePickerImpl picker = new JDatePickerImpl(datePanel,new org.jdatepicker.impl.DateComponentFormatter());
                                inputs[i] = picker;
                            }
                            case "gender","sex" -> {
                                JComboBox<String> c = new JComboBox<>(new String[]{"Male","Female","Other"});
                                if (value != null) c.setSelectedItem(value.toString());
                                inputs[i] = c;
                            }
                            case "status" -> {
                                JComboBox<String> c = new JComboBox<>(new String[]{"Active","Inactive"});
                                if (value != null) c.setSelectedItem(value.toString());
                                inputs[i] = c;
                            }
                            case "student_type" -> {
                                JComboBox<String> c = new JComboBox<>(new String[]{"Regular","Irregular"});
                                if (value != null) c.setSelectedItem(value.toString());
                                inputs[i] = c;
                            }
                            case "grade","block_no","civil_status" -> {
                                JComboBox<String> c = new JComboBox<>();
                                if (colLower.equals("grade")) continue;
                                if (colLower.equals("block_no")) c = new JComboBox<>(new String[]{"1","2","3","4","5","6"});
                                if (colLower.equals("civil_status")) c = new JComboBox<>(new String[]{"Single","Married","Widowed","Separated","Divorced"});
                                if (value != null) c.setSelectedItem(value.toString());
                                inputs[i] = c;
                            }
                            default -> inputs[i] = new JTextField(value != null ? value.toString() : "");
                        }
                        if (message[i*2] == null) message[i*2] = colName + ":";
                        if (message[i*2+1] == null) message[i*2+1] = inputs[i];
                    }

                    int option = JOptionPane.showConfirmDialog(null,message,"Edit Row in "+tableName,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,JOPIcon);
                    if (option != JOptionPane.OK_OPTION) return;

                    Object[] rowData = new Object[columnCount];
                    boolean emptyField = false;
                    for (int i = 0; i < columnCount; i++) {
                        String col = columnNames[i].toLowerCase();
                        Object in = inputs[i];
                        Object val = null;
                        if (in instanceof JTextField tf) { val = tf.getText().trim(); if (val.equals("")) emptyField=true; }
                        else if (in instanceof Object[] yearObj && col.equals("school_year")) {
                            JComboBox ys = (JComboBox) yearObj[0]; JLabel ye = (JLabel) yearObj[1];
                            val = ys.getSelectedItem()+"-"+ye.getText();
                        }
                        else if (in instanceof JComboBox cb) {
                            val = cb.getSelectedItem(); if (val==null) emptyField=true;
                            if (col.equals("gender")||col.equals("sex")||col.equals("status")) val = val.toString().charAt(0)+"";
                        }
                        else if (in instanceof JCheckBox[] cbs) {
                            StringBuilder sb = new StringBuilder();
                            for (JCheckBox cb:cbs) if(cb.isSelected()){if(!sb.isEmpty()) sb.append("/"); sb.append(cb.getText());}
                            if(sb.isEmpty()) emptyField=true;
                            val = sb.toString();
                        }
                        else if (in instanceof Object[] time) {
                            JComboBox h=(JComboBox)time[0]; JComboBox m=(JComboBox)time[1]; JComboBox mer=(JComboBox)time[2];
                            val = h.getSelectedItem()+":"+m.getSelectedItem()+" "+mer.getSelectedItem();
                        }
                        else if (in instanceof JDatePickerImpl dp) {
                            Object dv = dp.getModel().getValue();
                            if(dv==null) emptyField=true; else val=new java.sql.Date(((java.util.Date)dv).getTime());
                        }
                        rowData[i]=val;
                    }
                    if(emptyField){invalid("Invalid field inputs"); return;}

                    StringBuilder sqlUpdate = new StringBuilder("UPDATE "+tableName+" SET ");
                    for(String col:columnNames) sqlUpdate.append(col).append("=?, ");
                    sqlUpdate.delete(sqlUpdate.length()-2,sqlUpdate.length());
                    sqlUpdate.append(" WHERE ").append(idColumn).append("=?");

                    try(PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate.toString())){
                        for(int i=0;i<columnCount;i++) psUpdate.setObject(i+1,rowData[i]);
                        psUpdate.setString(columnCount+1,id);
                        psUpdate.executeUpdate();
                        commit();
                        loadSelect(tableName);
                        System.out.println("Row updated");
                    }
                }
            }
        } catch(SQLException e){ e.printStackTrace(); invalid("Invalid constraint or input"); }
    }

    private String findIdValueFromTable(JTable table, int selectedRow, String idColumn) {
       int modelRow = table.convertRowIndexToModel(selectedRow);
       
       for (int i = 0; i < table.getModel().getColumnCount(); i++) {
          if (table.getModel().getColumnName(i).equalsIgnoreCase(idColumn)) {
             Object v = table.getModel().getValueAt(modelRow, i);
             return v == null ? null : v.toString();
          }
       }
       
       try {
          int viewIndex = table.getColumnModel().getColumnIndex(idColumn);
          int modelIndex = table.convertColumnIndexToModel(viewIndex);
          Object v = table.getModel().getValueAt(modelRow, modelIndex);
          return v == null ? null : v.toString();
       } catch (IllegalArgumentException ignored) {
          
       }
       
       if (table.getModel().getColumnCount() > 0) {
          Object v = table.getModel().getValueAt(modelRow, 0);
          return v == null ? null : v.toString();
       }

       return null;
    }
    public void softDeleteRow(String tableName, JTable table, String idColumn) {
        if (accountLevel.equals("student")) {
            invalid("You cannot delete a row here as student.");
            return;
        } else if (accountLevel.equals("faculty") && !tableName.toUpperCase().equals("STUDENT")) {
            invalid("You cannot delete a row here as faculty.");
            return;
        }
        int row = table.getSelectedRow();
        if (row < 0) {
           invalid("No row selected.");
           return;
        }

        String idValue = findIdValueFromTable(table, row, idColumn);
        if (idValue == null) {
           invalid("ID value not found for column '" + idColumn + "'.");
           return;
        }

        int confirm = JOptionPane.showConfirmDialog(
           null,
           "Are you sure you want to delete this row?",
           "Confirm Delete",
           JOptionPane.YES_NO_OPTION,
           JOptionPane.WARNING_MESSAGE,
           JOPIcon
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE " + tableName + " SET deleted='T' WHERE " + idColumn + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
           ps.setObject(1, idValue);
           ps.executeUpdate();
           commit();
           loadSelect(tableName);
           System.out.println("Row marked as deleted");
        }
        catch (SQLException e) {
           e.printStackTrace();
           invalid("Delete failed");
        }
    }
    public void invalid(String message) {
        String jopmessage = "<html><h1 style='color: red;'>"+message+"</h1></html>";
        JOptionPane.showMessageDialog(null, jopmessage, "Invalid", 0, JOPIcon);
    }
    public void loadSelect(String table) {
        switch (table.toUpperCase()) {
            case "EMPLOYEE" -> loadFaculty();
            case "COLLEGE","CPROGRAM" -> loadCollegeAndCourse();
            case "SUBJECT" -> loadSubject();
            case "ENROLLMENT" -> loadEnrollment();
            case "STUDENT" -> loadStudent();
        }
    }
}
