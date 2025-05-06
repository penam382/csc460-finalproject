/*
 * Class QueryUI:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class provides the UI for performing various database
 *  queries on ski resort data, including member lessons, ski pass usage, and trail information.
 * 
 *  Public Methods:
 *      static void showMenu(Connection dbconn)
 *
 *  Private Methods:
 *      static void queryMemberLessons(Connection dbconn)
 *      static void querySkiPassUsage(Connection dbconn)
 *      static void queryIntermediateTrails(Connection dbconn)
 *      static void memberProfileAnalysis(Connection dbconn)
 */

import java.sql.*;
import java.util.Scanner;


public class QueryUI {

    public static void showMenu(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        boolean returnToMain = false;
        
        while (!returnToMain) {
            System.out.println("\n--- Query Operations ---");
            System.out.println("1. Member lessons query");
            System.out.println("2. Ski pass usage query");
            System.out.println("3. Intermediate trails query");
            System.out.println("4. Member Profile Analysis");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice (1-5): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  
                
                switch (choice) {
                    case 1:
                        queryMemberLessons(dbconn);
                        break;
                    case 2:
                        querySkiPassUsage(dbconn);
                        break;
                    case 3:
                        queryIntermediateTrails(dbconn);
                        break;
                    case 4:
                        memberProfileAnalysis(dbconn);
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
     * This method gets an input for a given member and list all the ski lessons they have purchased, including the number of remaining
     *      sessions, instructor name, and scheduled time.
     * @param dbconn - The connection to the database
     */
    private static void queryMemberLessons(Connection dbconn) {
       
        Member member = new Member();
        Scanner scanner = new Scanner(System.in);
        
        // Get member ID
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  
        

        member.memberQuery(dbconn, memberId);
        System.out.println("Member lessons query executed.");
    }
    
    /**
     * This method performs the ski pass query, for a given ski pass, listing all lift rides and
	 * equipment rentals associated with it, along with timestamps and return statuses.
     * @param dbconn - The connection to the database
     */
    private static void querySkiPassUsage(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        SkiPass skiPass = new SkiPass();
        
        try {
            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); 

            String skiPassQuery = "SELECT spxd.skiPassId " +
                             "FROM SkiPassXactDetails spxd " +
                             "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                             "WHERE t.memberId = ?";
            
            PreparedStatement pstmt = dbconn.prepareStatement(skiPassQuery);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int skiPassId = rs.getInt("skiPassId");
                System.out.println("Your ski pass ID is: " + skiPassId);
                
                boolean success = skiPass.skiPassQuery(dbconn, skiPassId);
                
                if (!success) {
                    System.out.println("Failed to retrieve ski pass details.");
                }
            } else {
                System.out.println("No ski pass found for member ID: " + memberId);
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine();
        }
    }

    /**
     * This method list all open trails suitable for intermediate-level skiers, 
     *      along with their category and connected lifts that are currently operational
     * @param dbconn - The connection to the database
     */
    private static void queryIntermediateTrails(Connection dbconn) {
        PreparedStatement stmt = null;
        ResultSet answer = null;
        
        try {

            String query = 
                "SELECT t.trailName, t.category, l.liftName " +
                "FROM Trail t " +
                "JOIN LiftTrailAccess lta ON t.trailId = lta.trailId " +
                "JOIN Lift l ON lta.liftId = l.liftId " +
                "WHERE t.difficulty = 'intermediate' " +
                "AND t.isOpen = 1 " +
                "AND l.isOpen = 1 " +
                "ORDER BY t.trailName";
            
            stmt = dbconn.prepareStatement(query);
            answer = stmt.executeQuery();
            
            System.out.println("\n--- Open trails suitable for intermediate-level skiers ---");
            System.out.println("Trail Name | Category | Connected Lift");
            System.out.println("------------------------------------------");
            
            boolean foundResults = false;
            
            while (answer.next()) {
                foundResults = true;
                String trailName = answer.getString("trailName");
                String category = answer.getString("category");
                String liftName = answer.getString("liftName");
                
                System.out.println(trailName + " | " + category + " | " + liftName);
            }
            
            if (!foundResults) {
                System.out.println("No open trails suitable for intermediate-level skiers.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (answer != null) answer.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }


    /**
     * This method gets an input for a given member and list all the type of equipment they prefer. it also sees the 
     * size they typically where. you also can see the members price theyve paid. it provides value to the member to see this information
     * @param dbconn - The connection to the database
     */
    private static void memberProfileAnalysis(Connection dbconn) {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt = null;
        ResultSet answer = null;
        
        try {
            // Get member ID
            System.out.print("Enter member ID: ");
            int memberId = scanner.nextInt();
            scanner.nextLine(); 
            
            // First, get member name
            String memberQuery = "SELECT name FROM MemberAccount WHERE memberId = ?";
            stmt = dbconn.prepareStatement(memberQuery);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            String memberName = "";
            if (answer.next()) {
                memberName = answer.getString("name");
                System.out.println("\n--- Profile Analysis for " + memberName + " (ID: " + memberId + ") ---");
            } else {
                System.out.println("Member ID not found.");
                return;
            }
            
            // Close
            answer.close();
            stmt.close();
            
            // Query for equipment preferences (skier or snowboarder)
            String equipmentQuery = 
                "SELECT ri.itemType, COUNT(*) as rental_count " +
                "FROM Transactions t " +
                "JOIN RentalXactDetails rxd ON t.transactionId = rxd.transactionId " +
                "JOIN ItemInRental iir ON rxd.rentalXactDetailsId = iir.rentalXactDetailsId " +
                "JOIN RentalInventory ri ON iir.itemId = ri.itemId " +
                "WHERE t.memberId = ? " +
                "AND ri.itemType IN ('ski', 'snowboard') " + 
                "GROUP BY ri.itemType " +
                "ORDER BY rental_count DESC";
            
            stmt = dbconn.prepareStatement(equipmentQuery);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            System.out.println("\nEquipment Preference:");
            if (answer.next()) {
                String preferredEquipment = answer.getString("itemType");
                int rentalCount = answer.getInt("rental_count");
                
                if (preferredEquipment.equalsIgnoreCase("ski")) {
                    System.out.println("This member is a skier (rented skis " + rentalCount + " times)");
                } else {
                    System.out.println("This member is a snowboarder (rented snowboards " + rentalCount + " times)");
                }
                
                // Check if they've used both
                if (answer.next()) {
                    System.out.println("Note: This member has also used " + answer.getString("itemType") + 
                                    " (" + answer.getInt("rental_count") + " times)");
                }
            } else {
                System.out.println("This member has not rented skis or snowboards.");
            }

            answer.close();
            stmt.close();
            
            // Query for typical gear sizes
            String gearQuery = 
                "SELECT ri.itemType, ri.itemSize, COUNT(*) as rental_count " +
                "FROM Transactions t " +
                "JOIN RentalXactDetails rxd ON t.transactionId = rxd.transactionId " +
                "JOIN ItemInRental iir ON rxd.rentalXactDetailsId = iir.rentalXactDetailsId " +
                "JOIN RentalInventory ri ON iir.itemId = ri.itemId " +
                "WHERE t.memberId = ? " +
                "GROUP BY ri.itemType, ri.itemSize " +
                "ORDER BY ri.itemType, rental_count DESC";
            
            stmt = dbconn.prepareStatement(gearQuery);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            System.out.println("\nTypical Gear Sizes:");
            String currentType = "";
            boolean foundGear = false;
            
            while (answer.next()) {
                foundGear = true;
                String itemType = answer.getString("itemType");
                String itemSize = answer.getString("itemSize");
                int count = answer.getInt("rental_count");
                
                if (!itemType.equals(currentType)) {
                    currentType = itemType;
                    System.out.println("- " + itemType + ": " + itemSize + " (rented " + count + " times)");
                }
            }
            
            if (!foundGear) {
                System.out.println("No gear rental information found.");
            }
            
            answer.close();
            stmt.close();
            
            // total spending
            String spendingQuery = 
                "SELECT transactionType, SUM(amount) as total_spent " +
                "FROM Transactions " +
                "WHERE memberId = ? " +
                "GROUP BY transactionType " +
                "ORDER BY total_spent DESC";
            
            stmt = dbconn.prepareStatement(spendingQuery);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            System.out.println("\nSpending Analysis:");
            double totalSpent = 0;
            boolean foundTransactions = false;
            
            while (answer.next()) {
                foundTransactions = true;
                String transactionType = answer.getString("transactionType");
                double spent = answer.getDouble("total_spent");
                totalSpent += spent;
                
                System.out.println("- " + transactionType + ": $" + String.format("%.2f", spent));
            }
            
            if (foundTransactions) {
                System.out.println("Total Amount Spent: $" + String.format("%.2f", totalSpent));
            } else {
                System.out.println("No transaction records found.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            try {
                if (answer != null) {
                    answer.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}