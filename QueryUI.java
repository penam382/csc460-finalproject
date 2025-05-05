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
            System.out.println("4. Custom query");
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
                        System.out.println("Custom query.");
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
    

private static void querySkiPassUsage(Connection dbconn) {
    Scanner scanner = new Scanner(System.in);
    SkiPass skiPass = new SkiPass();
    PreparedStatement stmt = null;
    ResultSet answer = null;
    
    try {
        // get the member ID
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();
        
        // find the skipassid using memberid
        String passQuery = 
            "SELECT sp.skiPassId " +
            "FROM SkiPass sp " +
            "JOIN SkiPassXactDetails spxd ON sp.skiPassId = spxd.skiPassId " +
            "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
            "WHERE t.memberId = ?";
        
        stmt = dbconn.prepareStatement(passQuery);
        stmt.setInt(1, memberId);
        answer = stmt.executeQuery();
        
        if (answer.next()) {
            int skiPassId = answer.getInt("skiPassId");
            
            boolean success = skiPass.skiPassQuery(dbconn, skiPassId);
            
            if (!success) {
                System.out.println("Failed to retrieve ski pass details.");
            }
        } else {
            System.out.println("No ski passes found for member ID: " + memberId);
        }
        
    } catch (SQLException e) {
        System.out.println("Error executing query: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        scanner.nextLine(); 
    } finally {
        try {
            if (answer != null) answer.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}

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
}