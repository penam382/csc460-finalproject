import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

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
                scanner.nextLine();  // Consume the newline
                
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
                scanner.nextLine();  // Clear the invalid input
            }
        }
    }

    private static void addLessonPackage(Connection dbconn) {
        Lesson lesson = new Lesson();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Add New Lesson Package ---");
        
        // Get resort property ID
        System.out.print("Enter resort property ID: ");
        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();
        
        // Get member ID
        System.out.print("Enter member ID: ");
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
        
        // Current timestamp for transaction time
        Timestamp xactDateTime = new Timestamp(System.currentTimeMillis());
        
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
        int lessonXactDetailsId = scanner.nextInt();
        scanner.nextLine();
        
        // Get lesson session ID
        System.out.print("Enter lesson session ID: ");
        int lessonSessionId = scanner.nextInt();
        scanner.nextLine();
        
        // Current date for usage date
        java.sql.Date usedDate = java.sql.Date.valueOf(LocalDate.now());
        
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