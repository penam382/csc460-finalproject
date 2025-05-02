import java.sql.*;

public class Lesson extends ResortComponent{
	public Lesson() {

	}

	public boolean createLessonXactDetails(Connection dbconn, int lessonXactDetailsId, int transactionId, int remSessions) {
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

	public boolean createLessonUsage(Connection dbconn, int lessonUsageId, int lessonXactDetailsId, int lessionSessionId, Date usedDate) {
		PreparedStatement stmt = null;

		try {
			String query = "INSERT INTO LessonUsage " +
						   "(lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended) " +
						   "VALUES (?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, lessonUsageId);
			stmt.setInt(2, lessonXactDetailsId);
			stmt.setDate(3, usedDate);
			stmt.setInt(4, lessionSessionId);
			stmt.setInt(5, 1);

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

	public boolean createLessonXact(Connection dbconn, int resortPropertyId, int memberId,
	Timestamp xactDateTime, int amount, int remSessions) {
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
			return false;
		}


		// Create LessonXactDetailsId
		int newLessonXactDetailsId = createNewId(dbconn, "LessonXactDetails", "lessonXactDetailsId");

		// Create LessonXactDetails,  content: transactionId, numSessions, remainingSessions
		// Logic: NumSessions = how many were used
		boolean successLessonXact = createLessonXactDetails(dbconn, newLessonXactDetailsId, newTransactionId, remSessions);
		if(!successLessonXact) {
			return false;
		}

		return true;
	}

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
			String query = "UPDATE LessonXactDetails SET remainingSessions = remainingSessions  - 1 " +
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
			if(answer.next()) {
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
}