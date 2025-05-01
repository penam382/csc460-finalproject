import java.time.LocalDate;
import java.sql.*;

public class Member {
    private int memberId;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    
    // Emergency contact details
    private int emergencyContactId;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Constructor
    public Member(String name, String phoneNumber, String email, LocalDate dateOfBirth, 
                  String emergencyContactName, String emergencyContactPhone) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public int createNewMemberId(Connection dbconn){
        return 1;
    }

    public int createNewEmergContId(Connection dbconn) {
        return 1;
    }

    // Method to insert member into database
    public boolean insertMember(Connection dbconn) throws SQLException {
        // Create Unique Member ID

        // Create Emergency Contact:
            // Create Unique Emergency Contact ID
            // Content: Name, Phone Number

        // Create Member Content: name, phoneNumber, email, dateOfBirth, emergencyContactId

        

        return true;
    }

    public boolean deleteMember(Connection dbconn) throws SQLException {

        // Check member has no active ski passes

        // Check member has no open rentals

        // Check member has no unused lesson sessions

        // If all checks pass, remove all related entries, and record

        return true;
    }

    public boolean updatePhoneNumber(Connection dbconn, int memberId, String newPhoneNumber) {
        return true;
    }

    public boolean updateEmail(Connection dbconn, int memberId, String newEmail) {
        return true;
    }

    public boolean updateEmergencyContact(Connection dbconn, int memberId, String newContactName, String newContactPhoneNum) {
        return true;
    }

    /**
      * Method: memberQuery
      * 
      * Purpose: 
      *      This method gets an input for a given member and list all the ski lessons they have purchased, including the number of remaining
      *      sessions, instructor name, and scheduled time.
      * 
      * Pre-condition: 
      *      
      * 
      * Post-condition: 
      *     
      */
    public String memberQuery(Connection dbconn, int memberId) {
        
        // check if memberId in DB
        return "";

    } 

    public void listMembers(Connection dbconn) {

    }


    // Getters and Setters
    

}