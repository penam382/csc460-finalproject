import java.time.LocalDate;
import java.sql.*;

public class Member {
    private int memberId; // made this an instance variable for easier usage throughout class

    public Member() {
        // Default constructor
    }
    
    public Member(int memberId) {
        this.memberId = memberId;
    }

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

    // Method to insert member into database
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
     * i didnt get to this since it got late but this is where i am going to pick up
     */
    public boolean deleteMember(Connection dbconn, int memberId) throws SQLException {
        // Check member has no active ski passes
        

        // Check member has no open rentals
        

        // Check member has no unused lesson sessions
        

        // If all checks pass, remove all related entries, and record
        return true;
    }

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
    public String memberQuery(Connection dbconn, int memberId) {
        // For a given member, list all the ski sessions they have purchased, including the number
        //  of remaining sessions, instructor name, and scheduled time
        
        // check if memberId in DB
        return "";
    }

    // Method to potentially help the UI
    public void listMembers(Connection dbconn) {
        
    }

    // setters/getters
}