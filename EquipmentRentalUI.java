/*
 * Class EquipmentRentalUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class provides the UI for equipment rental management,
 *  enabling to create, update, and delete rental transactions.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void addRental(Connection dbconn)
 *      static void updateRental(Connection dbconn)
 *      static void deleteRental(Connection dbconn)
 */

import java.sql.*;
import java.util.*;

public class EquipmentRentalUI {

    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            System.out.println("\n--- Equipment Rental Management ---");
            System.out.println("1. Add new rental");
            System.out.println("2. Update rental status");
            System.out.println("3. Delete rental record");
            System.out.println("4. Return to main menu");
            System.out.print("Enter your choice (1-4): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                switch (choice) {
                    case 1:
                        addRental(dbconn);
                        break;
                    case 2:
                        updateRental(dbconn);
                        break;
                    case 3:
                        deleteRental(dbconn);
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

    /**
     * this method allows the user to add a rental
     * @param dbconn - The connection to the database
     */
    private static void addRental(Connection dbconn) {
        Rental rental = new Rental();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Add New Rental ---");
        
        // Get resort property ID
        System.out.print("Enter resort property ID: ");
        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();  
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();
        
        // Get ski pass ID
        System.out.print("Enter ski pass ID: ");
        int skiPassId = scanner.nextInt();
        scanner.nextLine();
        
        // Get transaction amount
        System.out.print("Enter rental amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        // Get date/time from user
        System.out.print("Enter transaction date and time (YYYY-MM-DD HH:MM:SS): ");
        String dateTimeStr = scanner.nextLine();
        Timestamp dateTime = null;
        
        try {
            dateTime = Timestamp.valueOf(dateTimeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid timestamp format. Please use YYYY-MM-DD HH:MM:SS format.");
            return;
        }
        
        // Get number of items to rent
        System.out.print("Enter number of items to rent: ");
        int numItems = scanner.nextInt();
        scanner.nextLine();
        
        // Create an array list to store item IDs
        ArrayList<Integer> itemIds = new ArrayList<>();
        
        // Get item IDs
        System.out.print("Enter item ID #: ");
        for (int i = 0; i < numItems; i++) {
            
            int itemId = scanner.nextInt();
            scanner.nextLine();
            itemIds.add(itemId);
        }
        
        try {
            boolean success = rental.createRentalXact(dbconn, resortPropertyId, memberId, 
                              "Rental", dateTime, amount, skiPassId, itemIds);
            
            if (success) {
                System.out.println("Rental created successfully!");
            } else {
                System.out.println("Failed to create rental.");
            }
        } catch (Exception e) {
            System.out.println("Error creating rental: " + e.getMessage());
        }
    }

    /**
     * this method allows the update a rental
     * @param dbconn - The connection to the database
     */
    private static void updateRental(Connection dbconn) {
        Rental rental = new Rental();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Rental Status ---");
        
        // Get rental transaction ID
        System.out.print("Enter rental transaction ID: ");
        int rentalXactDetailsId = scanner.nextInt();
        scanner.nextLine();
    
        // Get date from user for return
        System.out.print("Enter return date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        java.sql.Date returnDate = null;
        
        try {
            returnDate = java.sql.Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }
    
        boolean success = rental.setRentalXactReturned(dbconn, rentalXactDetailsId, returnDate);
            
        if (success) {
            System.out.println("Rental marked as returned successfully!");
        } else {
            System.out.println("Failed to update rental status. Rental ID may not exist.");
        }
    }

    /**
     * this method allows the user to delete a rental
     * @param dbconn - The connection to the database
     */
    private static void deleteRental(Connection dbconn) {
        Rental rental = new Rental();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Rental Record ---");
        System.out.println("Note: Rentals can only be deleted if created in error and not yet used");
        
        // Get rental transaction ID
        System.out.print("Enter rental transaction ID: ");
        int rentalXactDetailsId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean success = rental.deleteRentalXact(dbconn, rentalXactDetailsId);
            
            if (success) {
                System.out.println("Rental deleted successfully!");
            } else {
                System.out.println("Failed to delete rental. It may not exist or cannot be deleted.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting rental: " + e.getMessage());
        }
    }
}