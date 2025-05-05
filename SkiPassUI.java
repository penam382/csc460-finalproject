import java.sql.*;
import java.util.Scanner;

/**
 * SkiPassUI class - Handles all ski pass-related user interface operations
 */
public class SkiPassUI {
    
    /**
     * Displays the ski pass management menu and processes user choices
     * 
     * @param dbconn The active database connection
     */
    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            System.out.println("\n--- Ski Pass Management ---");
            System.out.println("1. Add new ski pass");
            System.out.println("2. Record lift usage");
            System.out.println("3. Adjust remaining uses");
            System.out.println("4. Delete expired ski pass");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice (1-5): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline
                
                switch (choice) {
                    case 1:
                        addSkiPass(dbconn);
                        break;
                    case 2:
                        recordLiftUsage(dbconn);
                        break;
                    case 3:
                        adjustRemainingUses(dbconn);
                        break;
                    case 4:
                        deleteSkiPass(dbconn);
                        break;
                    case 5:
                        returnToMain = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a number between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();  // Clear the invalid input
            }
        }
    }

    /**
     * Adds a new ski pass to the database
     * 
     * @param dbconn The active database connection
     */
    private static void addSkiPass(Connection dbconn) throws SQLException {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Add New Ski Pass ---");
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  
        
        // Get resort property ID
        System.out.print("Enter resort property ID: ");
        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();  
        
        // Choose pass type
        System.out.println("Choose ski pass type:");
        System.out.println("1. 1-day pass: $20");
        System.out.println("2. 2-day pass: $40");
        System.out.println("3. 4-day pass: $60");
        System.out.println("4. Season pass: $1000");
        System.out.print("Enter choice (1-4): ");
        int passTypeChoice = scanner.nextInt();
        scanner.nextLine();

        // Set price based on selection
        double amount = 0.0;
        switch (passTypeChoice) {
            case 1: // 1-day pass
                amount = 20.0;
                break;
            case 2: // 2-day pass
                amount = 40.0;
                break;
            case 3: // 4-day pass
                amount = 60.0;
                break;
            case 4: // Season pass
                amount = 1000.0;
                break;
            default:
                System.out.println("Invalid pass type choice.");
                return;
        }
        
        // Get expiration date input from user
        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expDateStr = scanner.nextLine();
        java.sql.Date expirationDate = null;
        
        try {
            expirationDate = java.sql.Date.valueOf(expDateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }
        
        // assuming a season pass will be about 5 months
        if (passTypeChoice == 4) {
            passTypeChoice = 300;
        }
        // Get remaining uses
        int remainingUses = passTypeChoice;
        
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
        
        boolean success = skiPass.newSkiPass(dbconn, memberId, resortPropertyId, 
                        xactDateTime, amount, remainingUses, expirationDate);
        
        if (success) {
            System.out.println("Ski pass added successfully!");
        } else {
            System.out.println("Failed to add ski pass.");
        }
    }

    /**
     * Records a lift usage for a ski pass
     * 
     * @param dbconn The active database connection
     */
    private static void recordLiftUsage(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Record Lift Usage ---");
        
        // Get ski pass ID
        System.out.print("Enter ski pass ID: ");
        int skiPassId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean success = skiPass.skiPassUsed(dbconn, skiPassId);
            
            if (success) {
                System.out.println("Lift usage recorded successfully!");
            } else {
                System.out.println("Failed to record lift usage. Pass may be expired or have no remaining uses.");
            }
        } catch (Exception e) {
            System.out.println("Error recording lift usage: " + e.getMessage());
        }
    }

    /**
     * Adjusts the remaining uses of a ski pass
     * 
     * @param dbconn The active database connection
     */
    private static void adjustRemainingUses(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Adjust Ski Pass Remaining Uses ---");
        
        // Get ski pass ID
        System.out.print("Enter ski pass ID: ");
        int skiPassId = scanner.nextInt();
        scanner.nextLine();
        
        // Get new remaining uses count
        System.out.print("Enter the correct value: ");
        int newRemUses = scanner.nextInt();
        scanner.nextLine();
        
        try {
            // Using the resetRemainingUses method since it's already implemented in your SkiPass class
            boolean success = skiPass.resetRemainingUses(dbconn, skiPassId, newRemUses);
            
            if (success) {
                System.out.println("Ski pass remaining uses adjusted successfully!");
            } else {
                System.out.println("Failed to adjust remaining uses. Pass ID may not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error adjusting remaining uses: " + e.getMessage());
        }
    }

    /**
     * Deletes an expired or depleted ski pass
     * 
     * @param dbconn The active database connection
     */
    private static void deleteSkiPass(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Ski Pass ---");
        System.out.println("Note: Ski pass can only be deleted if it's expired and has no remaining uses");
        
        // Get ski pass ID
        System.out.print("Enter ski pass ID: ");
        int skiPassId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean success = skiPass.deleteSkiPass(dbconn, skiPassId);
            
            if (success) {
                System.out.println("Ski pass deleted and archived successfully!");
            } else {
                System.out.println("Failed to delete ski pass. It may not meet deletion criteria.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting ski pass: " + e.getMessage());
        }
    }

}