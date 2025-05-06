import java.sql.*;
import java.util.Scanner;

/**
 * Author: Seth Jernigan & Marco Pena
 * Course: CSC 460
 * Assignment name: Prog04
 * Professor and TA's: Prof. Lester McCann, Xinyu Guo, Jianwei Shen
 * Due Date:   May 6th, 2025, at the beginning of class
 * 
 * Program #4 - This program 
 * 
 * For parts of the code where we directly use oracle things it is from JDBC.java
 * Also, the structure of the main also comes from JDBC.java with some differences.
 * 
 *  Instance Variables (private):
 *      - Connection dbconn: Stores the active database connection.
 * 
 *  Private Methods:
 *     
 * 		
 * Notes: I run the code using the following on lectura:
 *      "
 *      export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 
 *      javac Prog4.java
 *      java Prog4
 *                      "
 *  
 */

public class Prog4 {

    private static Connection dbconn = null;

    public static void main(String[] args) {
        final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        // Seth Login Info
        String username = "sajernigan";
	    String password = "a9330";

        // Load the Oracle JDBC driver by initializing its base class
        try {

            Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }

        try {
            // Make connection to Oracle database, store in dbconn variable
            dbconn = DriverManager.getConnection(oracleURL, username, password);
            System.out.println("Connected to Oracle database.");
            System.out.println("Welcome to our Ski Resort!");
            
            // allow user to input
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                // user options
                System.out.println();
                System.out.println("Please select an option (1-7):");
                System.out.println("1. Member Management");
                System.out.println("2. Ski Pass Management");
                System.out.println("3. Equipment Inventory Management");
                System.out.println("4. Equipment Rental Management");
                System.out.println("5. Lesson Purchase Management");
                System.out.println("6. Query System");
                System.out.println("7. Exit");

                // menu

                int choice = scanner.nextInt();
                scanner.nextLine();  

                switch (choice) {
                    case 1:
                        MemberUI.showMenu(dbconn);
                        break;
                    case 2:
                        SkiPassUI.showMenu(dbconn);
                        break;
                    case 3:
                        EquipmentInventoryUI.showMenu(dbconn);
                        break;
                    case 4:
                        EquipmentRentalUI.showMenu(dbconn);
                        break;
                    case 5:
                        LessonUI.showMenu(dbconn);
                        break;
                    case 6:
                        QueryUI.showMenu(dbconn);
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        System.out.println("Sorry, that is not an option, choose a number from 1-7");
                }
            }
            
            // close and give a message
            dbconn.close();
            scanner.close();
            System.out.println("Database connection closed.");
            System.exit(-1);
        } catch (SQLException e) {

            System.err.println("*** SQLException:  "
                + "Could not open JDBC connection.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

        }
    }

}
