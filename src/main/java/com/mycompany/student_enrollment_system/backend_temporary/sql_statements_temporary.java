package com.mycompany.student_enrollment_system.backend_temporary;
//Required import to make all of this work.
import java.sql.*;
public class sql_statements_temporary {
    static void startAConnectionToADatabase() {
        //code to run at the start of the program or on login. This must be inside a try-catch function. The catch catches SQL exceptions.
        try (Connection nameOfTheConnectionToUseForTheEntireRuntime = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","Username","Password")) {
            //java.sql code here. The rest of the database manipulation code here.
            
            //disable auto-commit
            if (nameOfTheConnectionToUseForTheEntireRuntime.getAutoCommit()) {
                nameOfTheConnectionToUseForTheEntireRuntime.setAutoCommit(false);
            }
            //check if connection is read-only and set to not read-only
            if (nameOfTheConnectionToUseForTheEntireRuntime.isReadOnly()) {
                nameOfTheConnectionToUseForTheEntireRuntime.setReadOnly(false);
            }
            //commit changes
            nameOfTheConnectionToUseForTheEntireRuntime.commit();
            //rollback changes to the latest commit. 1. Create a savepoint, 2. Rollback to that savepoint. Savepoint can be released too.
            Savepoint nameOfTheSavepoint = nameOfTheConnectionToUseForTheEntireRuntime.setSavepoint();
            nameOfTheConnectionToUseForTheEntireRuntime.rollback(nameOfTheSavepoint);
            nameOfTheConnectionToUseForTheEntireRuntime.releaseSavepoint(nameOfTheSavepoint);
            //code to run at the end of the program or on logout.
            if (!nameOfTheConnectionToUseForTheEntireRuntime.isClosed()) {
                nameOfTheConnectionToUseForTheEntireRuntime.close();
            }
        }
        catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
        }
    }
    static void loginLevelConfirmation() {
        //embedded accounts
    }
    static void actualStatementsToUse() {
        //wait I'll continue this.
    }
}
