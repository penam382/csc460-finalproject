/*
 * Class EquipmentInventoryUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class implements the UI for managing equipment inventory,
 *  allowing to add, update, and delete equipment in the system. Since this is something the employee does, 
 *  we assume they are able to have IDs with them.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void addEquipment(Connection dbconn)
 *      static void updateEquipmentType(Connection dbconn)
 *      static void updateEquipmentSize(Connection dbconn)
 *      static void deleteEquipment(Connection dbconn)
 */

import java.sql.*;
import java.util.Scanner;

public class EquipmentInventoryUI {

    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            System.out.println("\n--- Equipment Inventory Management ---");
            System.out.println("1. Add new equipment");
            System.out.println("2. Update equipment type");
            System.out.println("3. Update equipment size");
            System.out.println("4. Delete equipment");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice (1-5): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                
                switch (choice) {
                    case 1:
                        addEquipment(dbconn);
                        break;
                    case 2:
                        updateEquipmentType(dbconn);
                        break;
                    case 3:
                        updateEquipmentSize(dbconn);
                        break;
                    case 4:
                        deleteEquipment(dbconn);
                        break;
                    case 5:
                        returnToMain = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); 
            }
        }
    }

    /**
     * this method allows the user to add equipment
     * @param dbconn - The connection to the database
     */
    private static void addEquipment(Connection dbconn) {
        Equipment equipment = new Equipment();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Add New Equipment ---");
        
        // Get resort property ID
        System.out.print("Enter resort property ID: ");
        int resortPropertyId = scanner.nextInt();
        scanner.nextLine();
        
        // Get item type
        System.out.print("Enter equipment type (e.g., Ski, Snowboard, Boots): ");
        String itemType = scanner.nextLine();
        
        // Get item size
        System.out.print("Enter equipment size: ");
        String itemSize = scanner.nextLine();
        
        boolean success = equipment.addNewEquipment(dbconn, resortPropertyId, itemType, itemSize);
        
        if (success) {
            System.out.println("Equipment added successfully!");
        } else {
            System.out.println("Failed to add equipment.");
        }
    }

    /**
     * this method allows the user to update equipment type
     * @param dbconn - The connection to the database
     */
    private static void updateEquipmentType(Connection dbconn) {
        Equipment equipment = new Equipment();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Equipment Type ---");
        
        // Get item ID
        System.out.print("Enter equipment ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine();
        
        // Get new type
        System.out.print("Enter new equipment type: ");
        String newType = scanner.nextLine();
        
        boolean success = equipment.changeEquipmentType(dbconn, itemId, newType);
        
        if (success) {
            System.out.println("Equipment type updated successfully!");
        } else {
            System.out.println("Failed to update equipment type.");
        }
    }

    /**
     * this method allows the user to update equipment size
     * @param dbconn - The connection to the database
     */
    private static void updateEquipmentSize(Connection dbconn) {
        Equipment equipment = new Equipment();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Equipment Size ---");
        
        // item ID
        System.out.print("Enter equipment ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine();
        
        // new size
        System.out.print("Enter new equipment size: ");
        String newSize = scanner.nextLine();
        
        boolean success = equipment.changeEquipmentSize(dbconn, itemId, newSize);
        
        if (success) {
            System.out.println("Equipment size updated successfully!");
        } else {
            System.out.println("Failed to update equipment size.");
        }
    }

    /**
     * this method allows the user to adjust delete equipment
     * @param dbconn - The connection to the database
     */
    private static void deleteEquipment(Connection dbconn) {
        Equipment equipment = new Equipment();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Equipment ---");
        System.out.println("Note: Equipment cannot be deleted if currently rented out");

        System.out.print("Enter equipment ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine();
        
        boolean success = equipment.deleteEquipment(dbconn, itemId);
        
        if (success) {
            System.out.println("Equipment deleted successfully!");
        } else {
            System.out.println("Failed to delete equipment.");
        }
    }
}