
/*
 * Class MemberUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class implements the user interface for Member Management, providing
 *  menu options and input handling for all member-related actions.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void addMember(Connection dbconn)
 *      static void updateMemberPhone(Connection dbconn)
 *      static void updateMemberEmail(Connection dbconn)
 *      static void updateEmergencyContact(Connection dbconn)
 *      static void deleteMember(Connection dbconn)
 */
import java.sql.*;
import java.util.Scanner;

public class MemberUI {
    
    // this method shows the menu
    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            // Display the Member Management submenu
            System.out.println("\n--- Member Management ---");
            System.out.println("1. Add new member");
            System.out.println("2. Update member phone number");
            System.out.println("3. Update member email");
            System.out.println("4. Update emergency contact");
            System.out.println("5. Delete member");
            System.out.println("6. Return to main menu");
            System.out.print("Enter your choice (1-6): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                
                switch (choice) {
                    case 1:
                        addMember(dbconn);
                        break;
                    case 2:
                        updateMemberPhone(dbconn);
                        break;
                    case 3:
                        updateMemberEmail(dbconn);
                        break;
                    case 4:
                        updateEmergencyContact(dbconn);
                        break;
                    case 5:
                        deleteMember(dbconn);
                        break;
                    case 6:
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
     * this method holds the UI for adding a member
     * @param dbconn - The connection to the database
     */
    private static void addMember(Connection dbconn) {
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("\n--- Add New Member ---");

        member.listMembers(dbconn);
        
        // Collect member information
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine();
        java.sql.Date dob = null;
        
        // make sure the format is correct
        try {
            dob = java.sql.Date.valueOf(dobStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }
        
        System.out.print("Enter emergency contact name: ");
        String emergContactName = scanner.nextLine();
        
        System.out.print("Enter emergency contact phone: ");
        String emergContactPhone = scanner.nextLine();
        
        try {
            boolean success = member.insertMember(dbconn, name, phone, email, dob, emergContactName, emergContactPhone);
            
            if (success) {
                System.out.println("Member added successfully!");
            } else {
                System.out.println("Failed to add member.");
            }
        } catch (SQLException e) {
            System.out.println("DB error adding member: " + e.getMessage());
        }
    }

   /**
    * this method allows the user to update the phone number
    * @param dbconn
    */
    private static void updateMemberPhone(Connection dbconn) {
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Member Phone Number ---");
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId;
        
        memberId = scanner.nextInt();
        scanner.nextLine();  
        
        // Get new phone number
        System.out.print("Enter new phone number: ");
        String newPhoneNumber = scanner.nextLine();
        
        // Update phone number in database
        boolean success = member.updatePhoneNumber(dbconn, memberId, newPhoneNumber);
        
        if (success) {
            System.out.println("Phone number updated successfully!");
        } else {
            System.out.println("Failed to update phone number. Member ID may not exist.");
        }
    }

    /**
    * this method allows the user to update the email
    * @param dbconn
    */
    private static void updateMemberEmail(Connection dbconn) {
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Member Email ---");
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId;
        
        memberId = scanner.nextInt();
        scanner.nextLine();  
        
        // Get new email
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        
        // Update email in database
        boolean success = member.updateEmail(dbconn, memberId, newEmail);
        
        if (success) {
            System.out.println("Email updated successfully!");
        } else {
            System.out.println("Failed to update email. Member ID may not exist.");
        }
    }

    /**
    * this method allows the user to update the emergency contact
    * @param dbconn
    */
    private static void updateEmergencyContact(Connection dbconn) {
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Update Emergency Contact Info ---");
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId;
        
        memberId = scanner.nextInt();
        scanner.nextLine();  
        
        System.out.print("Enter new contact name: ");
        String newContactName = scanner.nextLine();
        
        System.out.print("Enter new phone number: ");
        String newPhoneNumber = scanner.nextLine();

        // Update emergency contact info in database
        boolean success = member.updateEmergencyContact(dbconn, memberId, newContactName, newPhoneNumber);
        
        if (success) {
            System.out.println("Emergency Contact Info updated successfully!");
        } else {
            System.out.println("Failed to update Emergency Contact Info. Member ID may not exist.");
        }
    }

    /**
    * this method allows the user to delete a member account
    * @param dbconn
    */
    private static void deleteMember(Connection dbconn) {
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Delete Member ---");
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId;
        
        memberId = scanner.nextInt();
        scanner.nextLine();  

        try {
            // Delete member from database
            boolean success = member.deleteMember(dbconn, memberId);
            
            if (success) {
                System.out.println("Member deleted successfully!");
            } else {
                System.out.println("Failed to delete member. Member ID may not exist.");
            }
        } catch (SQLException e) {
            System.out.println("DB error deleting member: " + e.getMessage());
        }
    }
}