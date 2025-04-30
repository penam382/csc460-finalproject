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

    // Method to insert member into database
    public boolean insertMember(Connection dbconn) throws SQLException {
        
        // here we will add the information into our DB

        return true;
    }

    public boolean deletetMember(Connection dbconn) throws SQLException {

        return true;
    }

    public boolean updateMember(Connection dbconn) throws SQLException {
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


    // Getters and Setters
    

}