import java.time.LocalDate;
import java.sql.*;

/*
 * Class Member:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class contains functions that allow required system Adds, Updates, and Deletes
 *  with regard to the members at the Ski Resort.
 * 
 *  Inst Methods:
 * 		createNewMemberId(Connection dbconn)
 * 		createNewEmergContId(Connection dbconn)
 * 		insertMember(Connection dbconn, String name, String phoneNumber, String email, Date dateOfBirth, String emergContactName, String emergContactPhone)
 * 		createEmergencyContact(Connection dbconn, int emergContactId, String contactName, String contactPhone)
 *      createMemberAccount(Connection dbconn, int memberId, String name, String phoneNumber, String email, Date dateOfBirth, int emergencyContactId)
 *      deleteMember(Connection dbconn, int memberId)
 *      updatePhoneNumber(Connection dbconn, int memberId, String newPhoneNumber)
 *      updateEmail(Connection dbconn, int memberId, String newEmail)
 *      updateEmergencyContact(Connection dbconn, int memberId, String newContactName, String newContactPhoneNum)
 *      memberQuery(Connection dbconn, int memberId)
 */
public class Member {
    private int memberId; // made this an instance variable for easier usage throughout class

    public Member() {
        // Default constructor
    }
    
    public Member(int memberId) {
        this.memberId = memberId;
    }

    /*
	 * createNewMemberId(Connection dbconn)
	 * 
	 * Purpose: This class creates a new ID for member insertion
	 * 
	 * Returns: Created Id
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 * 
	 */
    public int createNewMemberId(Connection dbconn) {
        Statement stmt = null;
        ResultSet answer = null;
        int newId = 1;

        try {
            String query = "SELECT MAX(memberId) AS maxId FROM MemberAccount";
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if(answer.next()) {
                int maxId = answer.getInt("maxId");
                if(!answer.wasNull()) {
                    newId = maxId + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        } finally {
            try {
                if (answer != null) {
                    answer.close();
                }

                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                System.exit(-1);
            }
        }

        return newId;
    }

    /*
	 * createNewEmergContId(Connection dbconn)
	 * 
	 * Purpose: This class creates a new ID for emergency contact insertion
	 * 
	 * Returns: Created Id
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 * 
	 */
    public int createNewEmergContId(Connection dbconn) {
        Statement stmt = null;
        ResultSet answer = null;
        int newId = 1;

        try {
            String query = "SELECT MAX(emergencyContactId) AS maxId FROM EmergencyContact";
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if(answer.next()) {
                int maxId = answer.getInt("maxId");
                if(!answer.wasNull()) {
                    newId = maxId + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            return -1; // return -1 instead of system.exit(-1)
        } finally {
            try {
                if (answer != null) {
                    answer.close();
                }

                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                return -1;
            }
        }

        return newId;
    }

    /*
	 * insertMember(Connection dbconn, String name, String phoneNumber, String email, Date dateOfBirth, String emergContactName, String emergContactPhone)
	 * 
	 * Purpose: A wrapper that calls functions to create a new member account and corresponding emergency contact.
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  name: The name of the added member
     *  phoneNumber: The phone number of the added member
     *  email: The email of the added member
     *  dateOfBirth: The DOB of the added member
     *  emergContactName: The name of the emergency contact for the member
     *  emrgContactPhone: The phone number for this emergency contact.
	 * 
	 */
    public boolean insertMember(Connection dbconn, String name, String phoneNumber, 
                               String email, Date dateOfBirth, 
                               String emergContactName, String emergContactPhone) throws SQLException {
        // Create Unique Member ID
        int newMemberId = createNewMemberId(dbconn);
        
        // Create Emergency Contact:
        // Create Unique Emergency Contact ID
        int newEmergContactId = createNewEmergContId(dbconn);
        if (newEmergContactId == -1) { // making sure that the contact was made correctly
            System.out.println("Failed to generate new emergency contact ID");
            return false;
        }
        
        // Insert emergency contact data
        boolean successfulEmergencyContact = createEmergencyContact(dbconn, newEmergContactId, emergContactName, emergContactPhone);
        if (!successfulEmergencyContact) {
            return false;
        }
        
        // Create Member Content
        boolean successfulMemberAccount = createMemberAccount(dbconn, newMemberId, name, phoneNumber, email, dateOfBirth, newEmergContactId);
        if (!successfulMemberAccount) {
            return false;
        }
        
        // Set the member ID in the current object if needed
        this.memberId = newMemberId;
        
        return true;
    }
    
    /*
	 * createEmergencyContact(Connection dbconn, int emergContactId, String contactName, String contactPhone)
	 * 
	 * Purpose: This function creates a new emergency contact
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  emergContactId: The unique id for the new emergency contact
     *  contactName: The name of the emergency contact
     *  contactPhone: The phone number of the emergency contact.
	 * 
	 */
    private boolean createEmergencyContact(Connection dbconn, int emergContactId, String contactName, String contactPhone) {
        PreparedStatement stmt = null;

        try {
            String query = "INSERT INTO EmergencyContact " +
                           "(emergencyContactId, name, phoneNumber) " +
                           "VALUES (?, ?, ?)";

            stmt = dbconn.prepareStatement(query);
            stmt.setInt(1, emergContactId);
            stmt.setString(2, contactName);
            stmt.setString(3, contactPhone);
            int success = stmt.executeUpdate();

            if (success == 0) {
                System.out.println("Error with creating an emergency contact");
                return false;
            }
            return true;

        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not execute insertion.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            return false;
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                return false;
            }
        }
    }
    
    /*
	 * createMemberAccount(Connection dbconn, int memberId, String name, String phoneNumber, String email, Date dateOfBirth, int emergencyContactId)
	 * 
	 * Purpose: This function creates a new member account
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  memberId: A unique ID for this member's account record
     *  name: The name of the new member
     *  phoneNumber: The phone number of the new member
     *  email: The email of the new member
     *  dateOfBirth: The DOB of the new member
     *  emergencyContactId: The ID of the emergency contact record for this new member
	 * 
	 */
    private boolean createMemberAccount(Connection dbconn, int memberId, String name, String phoneNumber, 
                                     String email, Date dateOfBirth, int emergencyContactId) {
        PreparedStatement stmt = null;

        try {
            String query = "INSERT INTO MemberAccount " +
                           "(memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";

            stmt = dbconn.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setString(2, name);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, email);
            stmt.setDate(5, dateOfBirth);
            stmt.setInt(6, emergencyContactId);

            int success = stmt.executeUpdate();

            if (success == 0) {
                System.out.println("Error with creating a member account.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not execute insertion.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            return false;
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                return false;
            }
        }
    }

    /*
	 * deleteMember(Connection dbconn, int memberId)
	 * 
	 * Purpose: This function deletes a member account
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  memberId: A unique ID for this member's account record
	 * 
	 */
    public boolean deleteMember(Connection dbconn, int memberId) throws SQLException {
        // Initialize classes to access their methods
        SkiPass skiPass = new SkiPass();
        Rental rental = new Rental();
        Lesson lesson = new Lesson();
        
        // Check member has no active ski passes
        PreparedStatement skiPassStmt = null;
        ResultSet answer = null;
        
        try {
            // Find all ski passes associated with this member
            String query = "SELECT sp.skiPassId " +
                        "FROM SkiPass sp " +
                        "JOIN SkiPassXactDetails spxd ON sp.skiPassId = spxd.skiPassId " +
                        "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ?";
            
            skiPassStmt = dbconn.prepareStatement(query);
            skiPassStmt.setInt(1, memberId);
            answer = skiPassStmt.executeQuery();
            
            // Check each ski pass for remaining uses
            while (answer.next()) {
                int skiPassId = answer.getInt("skiPassId");
                if (skiPass.hasRemainingUses(dbconn, skiPassId)) {
                    System.out.println("ERROR: Member has ski passes with remaining uses. Cannot delete account.");
                    return false;
                }
            }
        } finally {
            if (answer != null) {
                answer.close();
            }
            if (skiPassStmt != null) {
                skiPassStmt.close();
            }
        }

        // Check member has no open rentals
        PreparedStatement rentalStmt = null;
        ResultSet rentalAnswer = null;
        
        try {
            String query = "SELECT COUNT(*) AS openRentalCount " +
                        "FROM RentalXactDetails rxd " +
                        "JOIN Transactions t ON rxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ? AND rxd.returnStatus = 0";
            
            rentalStmt = dbconn.prepareStatement(query);
            rentalStmt.setInt(1, memberId);
            rentalAnswer = rentalStmt.executeQuery();
            
            if (rentalAnswer.next() && rentalAnswer.getInt("openRentalCount") > 0) {
                System.out.println("ERROR: Member has open rental records. Cannot delete account.");
                return false;
            }
        } finally {
            if (rentalAnswer != null) {
                rentalAnswer.close();
            }
            if (rentalStmt != null) {
                rentalStmt.close();
            }
        }

        // Check member has no unused lesson sessions
        PreparedStatement lessonStmt = null;
        ResultSet lessonAnswer = null;
        
        try {
            String query = "SELECT COUNT(*) AS unusedLessonCount " +
                        "FROM LessonXactDetails lxd " +
                        "JOIN Transactions t ON lxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ? AND lxd.remainingSessions > 0";
            
            lessonStmt = dbconn.prepareStatement(query);
            lessonStmt.setInt(1, memberId);
            lessonAnswer = lessonStmt.executeQuery();
            
            if (lessonAnswer.next() && lessonAnswer.getInt("unusedLessonCount") > 0) {
                System.out.println("ERROR: Member has unused lesson sessions. Cannot delete account.");
                return false;
            }
        } finally {
            if (lessonAnswer != null) {
                lessonAnswer.close();
            }
            if (lessonStmt != null) {
                lessonStmt.close();
            }
        }


        /**
         * If all checks pass, begin deletion process
         * 
         * DELETION PROCESS:
         * 1. SkiPass 
         * 2. RentalXactDetails
         * 3. LessonXactDetails
         * 4. LodgeXactDetails
         * 5. LiftUsage
         * 6. Transactions
         * 7. SkiPassArchive
         * 8. MemberAccount
         */
        // Delete all ski passes for this member
        PreparedStatement getSkiPassStmt = null;
        ResultSet skiPassResult = null;
        try {
            String query = "SELECT sp.skiPassId " +
                        "FROM SkiPass sp " +
                        "JOIN SkiPassXactDetails spxd ON sp.skiPassId = spxd.skiPassId " +
                        "JOIN Transactions t ON spxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ?";
            
            getSkiPassStmt = dbconn.prepareStatement(query);
            getSkiPassStmt.setInt(1, memberId);
            skiPassResult = getSkiPassStmt.executeQuery();
            
            while (skiPassResult.next()) {
                int skiPassId = skiPassResult.getInt("skiPassId");
                boolean success = skiPass.deleteSkiPass(dbconn, skiPassId);
                if (!success) {
                    System.out.println("ERROR: Failed to delete ski pass: " + skiPassId);
                    return false;
                }
            }
        } finally {
            if (skiPassResult != null) {
                skiPassResult.close();
            }
            if (getSkiPassStmt != null) {
                getSkiPassStmt.close();
            }
        }
        
        // Delete all rentals for this member 
        PreparedStatement getRentalStmt = null;
        ResultSet rentalResult = null;
        try {
            String query = "SELECT rxd.rentalXactDetailsId " +
                        "FROM RentalXactDetails rxd " +
                        "JOIN Transactions t ON rxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ?";
            
            getRentalStmt = dbconn.prepareStatement(query);
            getRentalStmt.setInt(1, memberId);
            rentalResult = getRentalStmt.executeQuery();
            
            while (rentalResult.next()) {
                int rentalXactDetailsId = rentalResult.getInt("rentalXactDetailsId");
                boolean success = rental.deleteRentalXact(dbconn, rentalXactDetailsId);
                if (!success) {
                    System.out.println("ERROR: Failed to delete rental: " + rentalXactDetailsId);
                    return false;
                }
            }
        } finally {
            if (rentalResult != null) {
                rentalResult.close();
            }
            if (getRentalStmt != null) {
                getRentalStmt.close();
            }
        }
        
        // Delete lesson transactions
        PreparedStatement getLessonStmt = null;
        ResultSet lessonResult = null;
        try {
            String query = "SELECT lxd.lessonXactDetailsId " +
                        "FROM LessonXactDetails lxd " +
                        "JOIN Transactions t ON lxd.transactionId = t.transactionId " +
                        "WHERE t.memberId = ?";
            
            getLessonStmt = dbconn.prepareStatement(query);
            getLessonStmt.setInt(1, memberId);
            lessonResult = getLessonStmt.executeQuery();
            
            while (lessonResult.next()) {
                int lessonXactDetailsId = lessonResult.getInt("lessonXactDetailsId");
                boolean success = lesson.deleteLessonXact(dbconn, lessonXactDetailsId);
                if (!success) {
                    System.out.println("ERROR: Failed to delete lesson: " + lessonXactDetailsId);
                    return false;
                }
            }
        } finally {
            if (lessonResult != null) {
                lessonResult.close();
            }
            if (getLessonStmt != null) {
                getLessonStmt.close();
            }
        }
        
        // Delete lodge transactions
        PreparedStatement lodgeDelStmt = null;
        try {
            String query = "DELETE FROM LodgeXactDetails WHERE transactionId IN " +
                        "(SELECT transactionId FROM Transactions WHERE memberId = ?)";
            
            lodgeDelStmt = dbconn.prepareStatement(query);
            lodgeDelStmt.setInt(1, memberId);
            lodgeDelStmt.executeUpdate();
        } finally {
            if (lodgeDelStmt != null) {
                lodgeDelStmt.close();
            }
        }
        
        // Delete lift usage records
        PreparedStatement liftUsageStmt = null;
        try {
            String query = "DELETE FROM LiftUsage WHERE memberId = ?";
            liftUsageStmt = dbconn.prepareStatement(query);
            liftUsageStmt.setInt(1, memberId);
            liftUsageStmt.executeUpdate();
        } finally {
            if (liftUsageStmt != null) {
                liftUsageStmt.close();
            }
        }

        
        
        // Delete all transactions
        PreparedStatement transactionStmt = null;
        try {
            String query = "DELETE FROM Transactions WHERE memberId = ?";
            transactionStmt = dbconn.prepareStatement(query);
            transactionStmt.setInt(1, memberId);
            transactionStmt.executeUpdate();
        } finally {
            if (transactionStmt != null) {
                transactionStmt.close();
            }
        }

        // Delete ski pass archive records
        PreparedStatement skiPassArchiveStmt = null;
        try {
            String query = "DELETE FROM SkiPassArchive WHERE memberId = ?";
            skiPassArchiveStmt = dbconn.prepareStatement(query);
            skiPassArchiveStmt.setInt(1, memberId);
            skiPassArchiveStmt.executeUpdate();
        } finally {
            if (skiPassArchiveStmt != null) {
                skiPassArchiveStmt.close();
            }
        }
        
        // delete the member account
        PreparedStatement deleteMemberStmt = null;
        try {
            String query = "DELETE FROM MemberAccount WHERE memberId = ?";
            deleteMemberStmt = dbconn.prepareStatement(query);
            deleteMemberStmt.setInt(1, memberId);
            int rowsDeleted = deleteMemberStmt.executeUpdate();
            
            return (rowsDeleted > 0);
        } finally {
            if (deleteMemberStmt != null) {
                deleteMemberStmt.close();
            }
        }
    }

    
     /*
	 * updatePhoneNumber(Connection dbconn, int memberId, String newPhoneNumber)
	 * 
	 * Purpose: This function updates the phone number of a member
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  memberId: A unique ID for this member's account record
     *  newPhoneNumber: The phone number for the member account to be updated to
	 * 
	 */
    public boolean updatePhoneNumber(Connection dbconn, int memberId, String newPhoneNumber) {
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            String query = "UPDATE MemberAccount SET phoneNumber = ? WHERE memberId = ?";
            stmt = dbconn.prepareStatement(query);
            stmt.setString(1, newPhoneNumber);
            stmt.setInt(2, memberId);

            int success = stmt.executeUpdate();

            if (success == 0) {
                System.out.println("Error with creating a member account.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not execute update.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                System.exit(-1);
            }
        }

        return updated;
    }

     /*
	 * updateEmail(Connection dbconn, int memberId, String newEmail)
	 * 
	 * Purpose: This function updates the email of a member
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  memberId: A unique ID for this member's account record
     *  newEmail: The email for the member account to be updated to
	 * 
	 */
    public boolean updateEmail(Connection dbconn, int memberId, String newEmail) {
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            String query = "UPDATE MemberAccount SET email = ? WHERE memberId = ?";
            stmt = dbconn.prepareStatement(query);
            stmt.setString(1, newEmail);
            stmt.setInt(2, memberId);

            int success = stmt.executeUpdate();

            if (success == 0) {
                System.out.println("Error with creating a member account.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not execute update.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                System.exit(-1);
            }
        }

        return updated;
    }

    /*
	 * updateEmergencyContact(Connection dbconn, int memberId, String newContactName, String newContactPhoneNum)
	 * 
	 * Purpose: This function updates a member's emergency contact information
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
     *  memberId: A unique ID for this member's account record
     *  newContactName: The new name for the member's emerg. contact
     *  newContactPhoneNum: The new phone number for the member's emerg. contact
	 * 
	 */
    public boolean updateEmergencyContact(Connection dbconn, int memberId, String newContactName, String newContactPhoneNum) {
        PreparedStatement stmt = null;
        ResultSet answer = null;
        boolean updated = false;

        try {
            // First, get the emergency contact ID for this member
            String queryEmergId = "SELECT emergencyContactId FROM MemberAccount WHERE memberId = ?";
            stmt = dbconn.prepareStatement(queryEmergId);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            if (answer.next()) {
                int emergContactId = answer.getInt("emergencyContactId");
                answer.close();
                stmt.close();
                
                // Now update the emergency contact info
                String updateQuery = "UPDATE EmergencyContact SET name = ?, phoneNumber = ? WHERE emergencyContactId = ?";
                stmt = dbconn.prepareStatement(updateQuery);
                stmt.setString(1, newContactName);
                stmt.setString(2, newContactPhoneNum);
                stmt.setInt(3, emergContactId);
                
                int success = stmt.executeUpdate();

                if (success == 0) {
                    System.out.println("Error with creating a member account.");
                    return false;
                }
                return true;
            } else {
                System.out.println("ERROR: Member not found.");
            }
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not execute update.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        } finally {
            try {
                if (answer != null) {
                    answer.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                System.exit(-1);
            }
        }

        return updated;
    }

    /**
     * Method: memberQuery
     * 
     * Purpose: 
     *      This method gets an input for a given member and list all the ski lessons they have purchased, including the number of remaining
     *      sessions, instructor name, and scheduled time.
     * 
     * Pre-condition: 
     *      The memberId must exist in the database.
     * 
     * Post-condition: 
     *      Returns a string with all the ski sessions information for the member.
     */
    public boolean memberQuery(Connection dbconn, int memberId) {
        PreparedStatement stmt = null;
        ResultSet answer = null;
        
        try {
            String query = "SELECT ls.sessionDate, ls.startTime, ls.endTime, " +
                        "e.employeeName AS instructorName, " +
                        "lxd.remainingSessions " +
                        "FROM Transactions t " +
                        "JOIN LessonXactDetails lxd ON t.transactionId = lxd.transactionId " +
                        "JOIN LessonUsage lu ON lxd.lessonXactDetailsId = lu.lessonXactDetailsId " +
                        "JOIN LessonSession ls ON lu.lessonSessionId = ls.lessonSessionId " +
                        "JOIN LessonInstructor li ON ls.instructorId = li.instructorId " +
                        "JOIN Employee e ON li.employeeId = e.employeeId " +
                        "WHERE t.memberId = ? " +
                        "ORDER BY ls.sessionDate, ls.startTime";
            
            stmt = dbconn.prepareStatement(query);
            stmt.setInt(1, memberId);
            answer = stmt.executeQuery();
            
            System.out.println("\nSki Lessons for Member ID: " + memberId);
            System.out.println("Date | Time | Instructor | Remaining Sessions");
            
            boolean hasLessons = false;
            
            while (answer.next()) {
                hasLessons = true;
                Date sessionDate = answer.getDate("sessionDate");
                String startTime = answer.getString("startTime");
                String endTime = answer.getString("endTime");
                String instructorName = answer.getString("instructorName");
                int remainingSessions = answer.getInt("remainingSessions");
                System.out.println("[ " + 
                                sessionDate + " | " + 
                                startTime + "-" + endTime + " | " +
                                instructorName + " | " +
                                remainingSessions + " ]");
            }
            
            if (!hasLessons) {
                System.out.println("No lessons found for this member.");
            }
                        
            return true;
        } catch (SQLException e) {
            System.err.println("*** SQLException:  "
                + "Could not perform this query due to an exception.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            return false;
        } finally {
            try {
                if (answer != null) {
                    answer.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the query resources.");
                return false;
            }
        }
    }

    // Method to potentially help the UI
    public void listMembers(Connection dbconn) {
        
    }

    // setters/getters
}