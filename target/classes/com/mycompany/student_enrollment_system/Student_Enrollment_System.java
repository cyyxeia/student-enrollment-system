/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.student_enrollment_system;
/**
 *
 * @author 星野一歌
 */
public class Student_Enrollment_System {
    public static void main(String args[]) {
        Framework framework = new Framework();
        framework.startDatabaseConnection("jdbc:oracle:thin:@localhost:1521:XE","imfinals","imfinals");
        framework.loginOpen();
        //that's it
    }
}
