import java.sql.*;

/*
 * Class Lesson:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class contains functions that allow required system Adds, Updates, and Deletes
 *  with regard to lessons at the Ski Resort.
 * 
 *  Inst Methods:
 * 		createLessonXactDetails(Connection dbconn, int lessonXactDetailsId, int transactionId, int remSessions)
 * 		createLessonUsage(Connection dbconn, int lessonUsageId, int lessonXactDetailsId, int lessionSessionId, Date usedDate)
 * 		createLessonXact(Connection dbconn, int resortPropertyId, int memberId, Timestamp xactDateTime, double amount, int remSessions)
 * 		useLesson(Connection dbconn, int lessonXactDetailsId, int lessonSessionId, Date usedDate)
 * 		deleteLessonXact(Connection dbconn, int lessonXactDetailsId)
 * 		
 */
public class Lesson extends ResortComponent{
	public Lesson() {

	}

	/*
	 * createLessonXactDetails(Connection dbconn, int lessonXactDetailsId, int transactionId, int remSessions)
	 * 
	 * Purpose: This class creates a new entry into the LessonXactDetails relation, featuring details for a lesson
	 * purchase at the resort
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  lessonXactDetailsId: A generated Id for the lessonXact
	 *  transactionId: The associated transaction id with this lesson transaction details entry
	 *  remSessions: The number of sessions purchased with this transaction.
	 * 
	 */
	private boolean createLessonXactDetails(Connection dbconn, int lessonXactDetailsId, int transactionId, int remSessions) {
		PreparedStatement stmt = null;

		try {
			String query = "INSERT INTO LessonXactDetails " +
						   "(lessonXactDetailsId, transactionId, numSessions, remainingSessions) " +
						   "VALUES (?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, lessonXactDetailsId);
			stmt.setInt(2, transactionId);
			stmt.setInt(3, 0);
			stmt.setInt(4, remSessions);

			stmt.executeUpdate();
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
		return true;
	}

	/*
	 * createLessonUsage(Connection dbconn, int lessonUsageId, int lessonXactDetailsId, int lessionSessionId, Date usedDate)
	 * 
	 * Purpose: This class creates a new entry into the LessonUsage table, which happens on when a member uses a lesson within
	 * their lesson purchase.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  lessonUsageId: A uniquely generated ID for this lesson usage
	 * 	lessonXactDetailsId: The ID of the transaction that the lesson usage is associated with
	 *  lessonSessionId = The ID of the session that the member used with this usage
	 *  usedDate: The date in which the session usage occured.
	 */
	private boolean createLessonUsage(Connection dbconn, int lessonUsageId, int lessonXactDetailsId, int lessionSessionId, Date usedDate) {
		PreparedStatement stmt = null;
		ResultSet answer = null;
		int remainingSessions = 0;

		try {
			String remSessionsQuery = "SELECT remainingSessions FROM LessonXactDetails WHERE lessonXactDetailsId = ?";
			stmt = dbconn.prepareStatement(remSessionsQuery);
			stmt.setInt(1, lessonXactDetailsId);
			answer = stmt.executeQuery();

			if(answer.next()) {
				remainingSessions = answer.getInt("remainingSessions");
			} else {
				System.out.println("Cannot find the Lesson Transaction.");
				return false;
			}

			String query = "INSERT INTO LessonUsage " +
						   "(lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) " +
						   "VALUES (?, ?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, lessonUsageId);
			stmt.setInt(2, lessonXactDetailsId);
			stmt.setInt(3, lessionSessionId);
			stmt.setDate(4, usedDate);
			stmt.setInt(5, 1);
			stmt.setInt(6, remainingSessions);

			stmt.executeUpdate();
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
		return true;
	}

	/*
	 * createLessonXact(Connection dbconn, int resortPropertyId, int memberId, Timestamp xactDateTime, double amount, int remSessions)
	 * 
	 * Purpose: This function performs the full functionality of purchasing a lesson or set of lessons, including creating a new transaction,
	 * and new lesson transaction details.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  resortPropertyId: The id of the property the lesson transaction was made at
	 *  memberId: The id of the member who made the lesson purchase
	 *  xactDateTime: The timestamp of the lesson purchase
	 *  amount: The cost of the lesson purchase
	 *  remSessions: The number of lesson sessions purchased
	 */
	public boolean createLessonXact(Connection dbconn, int resortPropertyId, int memberId,
	Timestamp xactDateTime, double amount, int remSessions) {
		// Create Transaction Id
		int newTransactionId = createNewId(dbconn, "Transactions", "transactionId");

		// Validate resortPropertyId, memberId
		if(!existsId(dbconn, memberId, "MemberAccount", "memberId")) {
			return false;
		}
		
		if(!existsId(dbconn, resortPropertyId, "ResortProperty", "resortPropertyId")){
			return false;
		}

		// create new Transaction, content: xactId, resortPropertyId, memberId, type, dateTime, amount
		boolean successXact = createNewTransaction(dbconn, newTransactionId, resortPropertyId, memberId, "Lessons", xactDateTime, amount);
		if(!successXact) {
			System.out.println("ERROR: Couldn't create transaction");
			return false;
		}


		// Create LessonXactDetailsId
		int newLessonXactDetailsId = createNewId(dbconn, "LessonXactDetails", "lessonXactDetailsId");

		// Create LessonXactDetails,  content: transactionId, numSessions, remainingSessions
		// Logic: NumSessions = how many were used
		boolean successLessonXact = createLessonXactDetails(dbconn, newLessonXactDetailsId, newTransactionId, remSessions);
		if(!successLessonXact) {
			System.out.println("ERROR: Couldn't create transaction");
			return false;
		}

		return true;
	}

	/*
	 * useLesson(Connection dbconn, int lessonXactDetailsId, int lessonSessionId, Date usedDate)
	 * 
	 * Purpose: This function performs actions associated with using a lesson, including creating a usage, and linking the
	 * lesson transaction to the lesson session for the usage, and decrementing number of remaining sessions.
	 * 
	 * Returns: True if update successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  lessonXactDetailsId: The ID of the the lesson purchase transaction
	 *  lessonSessionId: The session for which the lesson usage was associated with
	 *  usedDate: The date in which the lesson was used
	 */
	public boolean useLesson(Connection dbconn, int lessonXactDetailsId, int lessonSessionId, Date usedDate) {
		// Create lesson usage id
		int newLessonUsage = createNewId(dbconn, "LessonUsage", "lessonUsageId");

		// Validate: lessonXactDetailsId, lessonSessionId
		if(!existsId(dbconn, lessonXactDetailsId, "LessonXactDetails", "lessonXactDetailsId")){
			return false;
		}

		if(!existsId(dbconn, lessonSessionId, "LessonSession", "lessonSessionId")) {
			return false;
		}

		// Populate LessonUsage: lessonXactDetailsId, lessonSessionId, usedDate, attended
		boolean successLessonUse = createLessonUsage(dbconn, newLessonUsage, lessonXactDetailsId, lessonSessionId, usedDate);
		if(!successLessonUse) {
			return false;
		}

		// decrement remaining sessions, increment numSessions
		PreparedStatement stmt = null;
		boolean updated = false;

		try {
			String query = "UPDATE LessonXactDetails SET remainingSessions = remainingSessions  - 1, numSessions = numSessions + 1 " +
			"WHERE lessonXactDetailsId = ?";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, lessonXactDetailsId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update lesson transaction usage.");
			}
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not execute update.");
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

		return updated;
	}

	/*
	 * deleteLessonXact(Connection dbconn, int lessonXactDetailsId)
	 * 
	 * Purpose: This function deletes a lesson transaction if the user hasn't used any lessons yet for the purchase.
	 * 
	 * Returns: True if deletion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  lessonXactDetailsId: The ID of the the lesson purchase transaction
	 */
	public boolean deleteLessonXact(Connection dbconn, int lessonXactDetailsId) {
		PreparedStatement stmt = null;
		ResultSet answer = null;

		try {
			// Ensure numSessions = 0
			String checkNoSessionsUsed = "SELECT * FROM LessonXactDetails WHERE lessonXactDetailsId = ? AND numSessions = 0";
			stmt = dbconn.prepareStatement(checkNoSessionsUsed);
			stmt.setInt(1, lessonXactDetailsId);
			answer = stmt.executeQuery();

			// End if Lesson Xact is not deletable
			if(!answer.next()) {
				System.out.println("Error: This lesson transaction is not deletable.");
				return false;
			}

			stmt.close();
			answer.close();

			// Delete LessonXact
			boolean successDelLessonXact = deleteFromWhere(dbconn, "LessonXactDetails", "lessonXactDetailsId", lessonXactDetailsId);
			if(!successDelLessonXact){
				return false;
			}

		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform operations to delete Lesson Transaction.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;

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
				return false;
			}
		}
		
		return true;
	}

	// Helper function to print all resort properties
	public void viewResortProperties(Connection dbconn) {
		boolean tryShowProperties = showAllNames(dbconn, "ResortProperty", "resortPropertyId", "propertyType");
	}

	// Method to potentially help the UI
    public void listMembers(Connection dbconn) {
        boolean tryShowAllMembers = showAllNames(dbconn, "MemberAccount", "memberId", "name");
    }
}