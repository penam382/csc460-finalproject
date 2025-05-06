/*
 * Class SkiPassUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class provides the user interface for managing ski passes,
 *  including adding new passes, recording lift usage, and adjusting pass details.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void addSkiPass(Connection dbconn)
 *      static void recordLiftUsage(Connection dbconn)
 *      static void adjustRemainingUses(Connection dbconn)
 *      static void deleteSkiPass(Connection dbconn)
 */

import java.sql.*;
import java.util.Scanner;

public class SkiPassUI {
    
    // this method shows the menu
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
                scanner.nextLine(); 
                
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
                scanner.nextLine();  
            }
        }
    }

    /**
     * this method adds a skipass from user inputs
     * @param dbconn - The connection to the database
     * @throws SQLException
     */
    private static void addSkiPass(Connection dbconn) throws SQLException {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Add New Ski Pass ---");
        
        // Get member ID
        skiPass.listMembers(dbconn);
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  
        
        // Get resort property ID
        skiPass.viewResortProperties(dbconn);
        System.out.print("Enter resort property ID: ");
        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();  
        
        // Choose pass type (i assumed prices here) 
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
     * this method allows the user to record a lift usage
     * @param dbconn - The connection to the database
     */
    private static void recordLiftUsage(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Record Lift Usage ---");

        try {
            skiPass.listMembers(dbconn);
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); 

            String skiPassQuery = "SELECT spxd.skiPassId, t.transactionDateTime " +
                             "FROM SkiPassXactDetails spxd " +
                             "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                             "WHERE t.memberId = ?";
            
            PreparedStatement pstmt = dbconn.prepareStatement(skiPassQuery);
            pstmt.setInt(1, memberId);
            ResultSet answer = pstmt.executeQuery();

            skiPass.showMemberSkiPasses(dbconn, memberId);

            System.out.println("Select Ski Pass to Use: ");
            int skiPassId = scanner.nextInt();

            skiPass.viewLift(dbconn);
            System.out.println("Enter Lift Id: ");
            int liftId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter usage date and time (YYYY-MM-DD HH:MM:SS): ");
            String usageDateTimeStr = scanner.nextLine();
            Timestamp usageDateTime = null;

            try {
                usageDateTime = Timestamp.valueOf(usageDateTimeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid timestamp format. Please use YYYY-MM-DD HH:MM:SS format.");
                return;
            }

            boolean success = skiPass.skiPassUsed(dbconn, skiPassId, memberId, liftId, usageDateTime);
                
            if (!success) {
                System.out.println("\"Failed to record lift usage. Pass may be expired or have no remaining uses.\"");
            }

            System.out.println("Sucessful record of lift usage");

        } catch (Exception e) {
            System.out.println("Error recording lift usage: " + e.getMessage());
        }
    }

    /**
     * this method allows the user to adjust remaining use if a member reports an issue
     * @param dbconn - The connection to the database
     */
    private static void adjustRemainingUses(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Adjust Ski Pass Remaining Uses ---");

        try {
            skiPass.listMembers(dbconn);
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); 

            String skiPassQuery = "SELECT spxd.skiPassId " +
                             "FROM SkiPassXactDetails spxd " +
                             "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                             "WHERE t.memberId = ?";
            
            PreparedStatement pstmt = dbconn.prepareStatement(skiPassQuery);
            pstmt.setInt(1, memberId);
            ResultSet answer = pstmt.executeQuery();

            skiPass.showMemberSkiPasses(dbconn, memberId);

            System.out.println("Select Ski Pass to Use: ");
            int skiPassId = scanner.nextInt();

            // Get new remaining uses 
            System.out.print("Enter the correct value: ");
            int newRemUses = scanner.nextInt();
            scanner.nextLine();

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
     * this method allows the user to delete a skipass
     * @param dbconn - The connection to the database
     */
    private static void deleteSkiPass(Connection dbconn) {
        SkiPass skiPass = new SkiPass();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Ski Pass ---");
        System.out.println("Note: Ski pass can only be deleted if it's expired and has no remaining uses");
        
        
        try {
            skiPass.listMembers(dbconn);
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); 

            String skiPassQuery = "SELECT spxd.skiPassId " +
                             "FROM SkiPassXactDetails spxd " +
                             "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                             "WHERE t.memberId = ?";
            
            PreparedStatement pstmt = dbconn.prepareStatement(skiPassQuery);
            pstmt.setInt(1, memberId);
            ResultSet answer = pstmt.executeQuery();

            int skiPassId = -1;
            // make sure skipassid exists for that member, if not we can just return 
            if (answer.next()) {
                skiPassId = answer.getInt("skiPassId");
                System.out.println("Your ski pass ID is: " + skiPassId);          
            } else {
                System.out.println("No ski pass found for member ID: " + memberId);
                return;
            }

            boolean success = skiPass.deleteSkiPass(dbconn, skiPassId);
            
            if (success) {
                System.out.println("Ski pass deleted successfully!");
            } else {
                System.out.println("Failed to delete ski pass.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting ski pass: " + e.getMessage());
        }
    }

}