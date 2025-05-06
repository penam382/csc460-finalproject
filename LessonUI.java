/*
 * Class LessonUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class implements the UI for lesson management,
 *  allowing to create lesson packages, track usage, and manage records.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void addLessonPackage(Connection dbconn)
 *      static void recordLessonUsage(Connection dbconn)
 *      static void deleteLessonPackage(Connection dbconn)
 */

 import java.sql.*;
import java.util.Scanner;

public class LessonUI {

    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            System.out.println("\n--- Lesson Management ---");
            System.out.println("1. Add new lesson package");
            System.out.println("2. Record lesson usage");
            System.out.println("3. Delete lesson package");
            System.out.println("4. Return to main menu");
            System.out.print("Enter your choice (1-4): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  
                
                switch (choice) {
                    case 1:
                        addLessonPackage(dbconn);
                        break;
                    case 2:
                        recordLessonUsage(dbconn);
                        break;
                    case 3:
                        deleteLessonPackage(dbconn);
                        break;
                    case 4:
                        returnToMain = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();  
            }
        }
    }

    private static void addLessonPackage(Connection dbconn) {
        Lesson lesson = new Lesson();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Add New Lesson Package ---");
        
        // Get resort property ID
        System.out.print("Enter resort property ID: ");

        lesson.viewResortProperties(dbconn);

        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();
        
        // Get member ID
        System.out.print("Enter member ID: ");

        lesson.listMembers(dbconn);

        int memberId = scanner.nextInt();
        scanner.nextLine();
        
        // Get number of sessions
        System.out.print("Enter number of sessions in package: ");
        int remSessions = scanner.nextInt();
        scanner.nextLine();
        
        // Get package price
        System.out.print("Enter package price: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        // Get transaction date/time from user
        System.out.print("Enter transaction date and time (YYYY-MM-DD HH:MM:SS): ");
        String xactDateTimeStr = scanner.nextLine();
        Timestamp xactDateTime = null;
        
        try {
            xactDateTime = Timestamp.valueOf(xactDateTimeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid timestamp format. Please use YYYY-MM-DD HH:MM:SS format.");
            return;
        }

        try {
            boolean success = lesson.createLessonXact(dbconn, resortPropertyId, memberId, 
                              xactDateTime, amount, remSessions);
            
            if (success) {
                System.out.println("Lesson package added successfully!");
            } else {
                System.out.println("Failed to add lesson package.");
            }
        } catch (Exception e) {
            System.out.println("Error adding lesson package: " + e.getMessage());
        }
    }

    private static void recordLessonUsage(Connection dbconn) {
        Lesson lesson = new Lesson();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Record Lesson Usage ---");
        
        // Get lesson transaction ID
        System.out.print("Enter lesson transaction ID: ");

        lesson.showAllLessonXacts(dbconn);

        int lessonXactDetailsId = scanner.nextInt();
        scanner.nextLine();
        
        // Get lesson session ID
        System.out.print("Enter lesson session ID: ");

        lesson.showAllLessonSessions(dbconn);

        int lessonSessionId = scanner.nextInt();
        scanner.nextLine();
        
        // Get date from user for usage (without time component)
        System.out.print("Enter usage date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        java.sql.Date usedDate = null;
        
        try {
            usedDate = java.sql.Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }
        
        try {
            boolean success = lesson.useLesson(dbconn, lessonXactDetailsId, lessonSessionId, usedDate);
            
            if (success) {
                System.out.println("Lesson usage recorded successfully!");
            } else {
                System.out.println("Failed to record lesson usage.");
            }
        } catch (Exception e) {
            System.out.println("Error recording lesson usage: " + e.getMessage());
        }
    }

    private static void deleteLessonPackage(Connection dbconn) {
        Lesson lesson = new Lesson();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Lesson Package ---");
        System.out.println("Note: Only lesson packages with no used sessions can be deleted");
        
        // Get lesson transaction ID
        System.out.print("Enter lesson transaction ID: ");
        lesson.showAllLessonXacts(dbconn);
        int lessonXactDetailsId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean success = lesson.deleteLessonXact(dbconn, lessonXactDetailsId);
            
            if (success) {
                System.out.println("Lesson package deleted successfully!");
            } else {
                System.out.println("Failed to delete lesson package. It may have used sessions.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting lesson package: " + e.getMessage());
        }
    }
}