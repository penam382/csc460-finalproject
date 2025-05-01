import java.sql.*;

public class Lesson {
	public Lesson() {

	}

	private int createNewTransactionId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(transactionId) AS maxId FROM Transactions";
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if(answer.next()) {
				int maxId = answer.getInt("maxId");
				if(!answer.wasNull()){
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

	private int createNewLessonXactDetailsId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(lessonXactDetailsId) AS maxId FROM LessonXactDetails";
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if(answer.next()) {
				int maxId = answer.getInt("maxId");
				if(!answer.wasNull()){
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

	private int createNewLessonUsageId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(lessonUsageId) AS maxId FROM LessonUsage";
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if(answer.next()) {
				int maxId = answer.getInt("maxId");
				if(!answer.wasNull()){
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

	public boolean existsMemberId(Connection dbconn, int givenMemberId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM MemberAccount WHERE memberId = " + givenMemberId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No member with this ID found!");
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

		return found;
	}

	public boolean existsResortPropertyId(Connection dbconn, int givenResortPropertyId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM ResortProperty WHERE resortPropertyId = " + givenResortPropertyId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No resort property with this ID found!");
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

		return found;
	}

	public boolean existsLessonXactDetails(Connection dbconn, int givenLessonXactDetailsId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM LessonXactDetails WHERE lessonXactDetailsId = " + givenLessonXactDetailsId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No lesson transactions with this ID found!");
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

		return found;
	}

	public boolean existsLessonSession(Connection dbconn, int givenLessonSessionId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM LessonSession WHERE lessonSessionId = " + givenLessonSessionId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No lesson sessions with this ID found!");
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

		return found;
	}

	public void createNewTransaction(Connection dbconn, int transactionId, int resortPropertyId, int memberId, String transactionType,
	Timestamp xactDateTime, double amount){
		PreparedStatement stmt = null;

		try{
			String query = "INSERT INTO Transactions " +
                "(transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, transactionId);
			stmt.setInt(2, resortPropertyId);
			stmt.setInt(3, memberId);
			stmt.setString(4, transactionType);
			stmt.setTimestamp(5, xactDateTime);
			stmt.setDouble(6, amount);

			stmt.executeUpdate();
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not execute insertion.");
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
	}

	public void createLessonXactDetails(Connection dbconn, int lessonXactDetailsId, int transactionId, int remSessions) {
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
	}

	public void createLessonUsage(Connection dbconn, int lessonUsageId, int lessonXactDetailsId, int lessionSessionId, Date usedDate) {
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
	}

	public boolean createLessonXact(Connection dbconn, int resortPropertyId, int memberId,
	Timestamp xactDateTime, int amount, int remSessions) {
		// Create Transaction Id
		int newTransactionId = createNewTransactionId(dbconn);

		// Validate resortPropertyId, memberId
		if(!existsMemberId(dbconn, memberId)) {
			return false;
		}
		
		if(!existsResortPropertyId(dbconn, resortPropertyId)){
			return false;
		}

		// create new Transaction, content: xactId, resortPropertyId, memberId, type, dateTime, amount
		createNewTransaction(dbconn, newTransactionId, resortPropertyId, memberId, "Lessons", xactDateTime, amount);

		// Create LessonXactDetailsId
		int newLessonXactDetailsId = createNewLessonXactDetailsId(dbconn);

		// Create LessonXactDetails,  content: transactionId, numSessions, remainingSessions
		// Logic: NumSessions = how many were used
		createLessonXactDetails(dbconn, newLessonXactDetailsId, newTransactionId, remSessions);

		return true;
	}

	public boolean useLesson(Connection dbconn, int lessonXactDetailsId, int lessonSessionId, Date usedDate) {
		// Create lesson usage id
		int newLessonUsage = createNewLessonUsageId(dbconn);

		// Validate: lessonXactDetailsId, lessonSessionId
		if(!existsLessonXactDetails(dbconn, lessonXactDetailsId)){
			return false;
		}

		if(!existsLessonSession(dbconn, lessonSessionId)) {
			return false;
		}

		// Populate LessonUsage: lessonXactDetailsId, lessonSessionId, usedDate, attended
		createLessonUsage(dbconn, newLessonUsage, lessonXactDetailsId, lessonSessionId, usedDate);

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

			String deleteLessonXactQuery = "DELETE FROM LessonXactDetails WHERE lessonXactDetailsId = ?";

			stmt = dbconn.prepareStatement(deleteLessonXactQuery);
			stmt.setInt(1, lessonXactDetailsId);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform operations to delete Lesson Transaction.");
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
		
		return true;
	}
}