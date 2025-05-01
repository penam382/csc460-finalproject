public class SkiPass {

	public SkiPass() {

	}

	public int createNewXactId(Connection dbconn) {
		
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

	public int createNewSkiPassXactDetailsId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(skiPassXactDetailsId) AS maxId FROM SkiPassXactDetails";
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

	public int createSkiPassId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(skiPassId) AS maxId FROM SkiPass";
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
			stmt.setTimestamp(5, transactionDateTime);
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

	public void createNewSkiPass(Connection dbconn, int skiPassId, int remainingUses, Date expirationDate) {
		PreparedStatement stmt = null;

		try{
			String query = "INSERT INTO SkiPass " +
                "(skiPassId, numberOfUses, remainingUses, expirationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, skiPassId);
        	stmt.setInt(2, 0);
        	stmt.setInt(3, remainingUses);
        	stmt.setDate(4, expirationDate);

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

	public void createNewSkiPassXactDetails(Connection dbconn, int skiPassXactDetailsId, int transactionId, int skiPassId) {
		PreparedStatement stmt = null;

		try{
			String query = "INSERT INTO SkiPassXactDetails " +
                "(skiPassXactDetailsId, transactionId, skiPassId) " +
                "VALUES (?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, skiPassXactDetailsId);
        	stmt.setInt(2, transactionId);
        	stmt.setInt(3, skiPassId);

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

	public boolean newSkiPass(Connection dbconn, int memberId, int resortPropertyId, String xactType, Timestamp xactDateTime,
	double amount, int remainingUses, Date expirationDate) {
		// createNewXactId
		int newTransactionId = createNewXactId(dbconn);

		// Validate memberId, resortPropertyId
		if(!existsMemberId(dbconn, memberId)) {
			return false;
		}
		
		if(!existsResortPropertyId(dbconn, resortPropertyId)){
			return false;
		}

		// create new Transaction, content: xactId, resortPropertyId, memberId, type, dateTime, amount
		createNewTransaction(dbconn, newransactionId, resortPropertyId, memberId, xactType, xactDateTime, amount);

		// createNewSkiPassId
		int newSkiPassId = createSkiPassId(dbconn);

		// create new SkiPass, content: skiPassId, numUses = 0, remainingUses, expirDate
		createNewSkiPass(dbconn, newSkiPassId, remainingUses, expirationDate);

		// createNewSkiPassXactDetailsId
		newSkiPassXactDetailsId = createNewSkiPassXactDetailsId(dbconn);

		// create new skiPassXactDetails, content: id, transactionId, skiPassId
		createNewSkiPassXactDetails(dbconn, newSkiPassXactDetailsId, newTransactionId, newSkiPassId);

		return true;
	}

	public boolean skiPassUsed(Connection dbconn, int skiPassId) {
		// deduct 1 from ski pass remaining uses, incrememnt number of uses
		PreparedStatement stmt = null;
		boolean updated = false;

		try{
			String query = "UPDATE SkiPass SET numberOfUses = numberOfUses + 1, " +
			"remainingUses = remainingUses - 1 WHERE skiPassId = ? AND remainingUses > 0";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, skiPassId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update ski pass usage.");
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

	public boolean resetRemainingUses(Connection dbconn, int skiPassId, int newRemUses) {
		PreparedStatement stmt = null;
		boolean updated = false;

		try{
			String query = "UPDATE SkiPass SET remainingUses = ? WHERE skiPassId = ?";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, newRemUses);
			stmt.setInt(2, skiPassId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update ski pass usage.");
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

	public int createNewSkiPassArchiveId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(skiPassArchiveId) AS maxId FROM SkiPassArchive";
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

	public boolean deleteSkiPass(Connection dbconn, int skiPassId) {
		PreparedStatement stmt = null;
		ResultSet answer = null;

		try {
			// Check if Ski Pass is deletable
			String checkDeletableQuery = "SELECT * FROM SkiPass WHERE skiPassId = ? AND (remainingUses = 0 AND expirationDate < SYSDATE)";
			stmt = dbconn.prepareStatement(checkDeletableQuery);
			stmt.setInt(1, skiPassId);
			answer = stmt.executeQuery();

			// End if Ski Pass is not deletable
			if(answer.next()) {
				System.out.println("Error: This Ski Pass cannot be deleted because it is either not expired or has remaining uses.");
				return false;
			}

			stmt.close();
			answer.close();

			// Get Member Id, Number of Uses associated with Ski Pass
			int memberId = 0;
			int numUses = 0;

			String archiveInfoQuery = "SELECT t.memberId, sp.numberOfUses "+
									 "FROM SkiPassXactDetails spx "+
									 "JOIN Transactions t ON spx.transactionId = t.transactionId " +
									 "JOIN SkiPass sp ON spx.skiPassId = sp.skiPassId "+
									 "WHERE sp.skiPassId = ?";

			stmt = dbconn.prepareStatement(getSkiPassQuery);
			stmt.setInt(1, skiPassId);
			answer = stmt.executeQuery();

			if(answer.next()) {
				memberId = answer.getInt("memberId");
				numUses = answer.getInt("numberOfUses");
			} else {
            	System.out.println("Error: Could not find memberId or numberOfUses for Ski Pass.");
            	return false;
        	}

			stmt.close();
			answer.close();

			// Create new ski pass archive Id
			int newSkiPassArchiveId = createNewSkiPassArchiveId(dbconn);

			// Place newSkiPassArchiveId, MemberId, NumberOfUses in SkiPassArchive
			String insertIntoArchiveQuery = "INSERT INTO SkiPassArchive (skiPassArchiveId, memberId, numberOfUses) VALUES (?, ?, ?)";

			stmt = dbconn.prepareStatement(insertIntoArchiveQuery);
			stmt.setInt(1, newSkiPassArchiveId);
			stmt.setInt(2, memberId);
			stmt.setInt(2, numUses);
			stmt.executeUpdate();
			stmt.close();

			// Delete all entries with skiPassId from SkiPassXactDetails, RentalXactDetails, LiftUsage, SkiPass
			String deleteFromSpXactDetails = "DELETE FROM SkiPassXactDetails WHERE skiPassId = ?";

			stmt = dbconn.prepareStatement(deleteFromSpXactDetails);
			stmt.setInt(1, skiPassId);
			stmt.executeUpdate();
			stmt.close();

			String deleteFromRentXactDetails = "DELETE FROM RentalXactDetails WHERE skiPassId = ?";

			stmt = dbconn.prepareStatement(deleteFromRentXactDetails);
			stmt.setInt(1, skiPassId);
			stmt.executeUpdate();
			stmt.close();

			String deleteFromLiftUsage = "DELETE FROM LiftUsage WHERE skiPassId = ?";

			stmt = dbconn.prepareStatement(deleteFromLiftUsage);
			stmt.setInt(1, skiPassId);
			stmt.executeUpdate();
			stmt.close();

			String deleteFromSkiPass = "DELETE FROM SkiPass WHERE skiPassId = ?";

			stmt = dbconn.prepareStatement(deleteFromSkiPass);
			stmt.setInt(1, skiPassId);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform operations to delete Ski Pass.");
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

	public void skiPassQuery(Connection dbconn, int skiPassId){
		// For a given ski pass, list all lift rides and equipment rentals associated with it, along with
		//	timestamps and return status

		PreparedStatement stmt = null;
		ResultSet answer = null;

		try {
			// Print all lift rides (Lift Name, UsageDatetime) associated with SkiPass
			// Tables Used: LiftUsage, Lift
			String liftQuery = "SELECT Lift.liftName, LiftUsage.usageDateTime "+
						   "FROM LiftUsage JOIN Lift ON LiftUsage.liftId = Lift.liftId " +
						   "WHERE LiftUsage.skiPassId = ? " +
						   "ORDER BY LiftUsage.usageDateTime";


			stmt = dbconn.prepareStatement(liftQuery);
			stmt.setInt(1, skiPassId);
			answer = stmt.executeQuery();

			System.out.println("Lift Rides:")
			System.out.println("[Lift Name | Usage Date Time]")

			while(answer.next()) {
				String liftName = answer.getString("liftName");
				Timestamp usageTime = answer.getTimestamp("usageDateTime");
				System.out.println("[ " + liftName + " | " + usageTime + " }");
			}

			stmt.close();
			answer.close();

			String rentalQuery = "SELECT rx.rentalXactDetailsId, t.transactionDateTime, ri.itemType, ri.itemSize, rx.returnStatus "+
								 "FROM RentalXactDetails rx " +
								 "JOIN Transactions t ON rx.transactionId = t.transactionId " +
								 "JOIN ItemInRental iir ON rx.rentalXactDtailsId = iir.rentalXactDetailsId " +
								 "JOIN RentalInventory ri ON iir.itemId = ri.itemID " +
								 "WHERE rs.skiPassId = ? " +
								 "ORDER BY t.transactionDateTime";

			stmt = dbconn.prepareStatement(rentalQuery);
			stmt.setInt(1, skiPassId);
			answer = stmt.executeQuery();

			System.out.println("Equipment Rentals:")
			System.out.println("[Rental Xact ID | Xact Date Time | Item Type | Item Size | Return Status ]")

			while(answer.next()) {
				int rentalXactId = answer.getInt("rentalXactDetailsId");
				Timestamp rentalTime = answer.getTimestamp("transactionDateTime");
				String itemType = answer.getString("itemType");
				String itemSize = answer.getString("itemSize");
				String returnStatus = answer.getString("returnStatus");
				System.out.println("[ " + rentalXactId + " | " + rentalTime + " | " + itemType + " | " + itemSize + " | " + returnStatus + " ]");
			}

			stmt.close();
			answer.close();

		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform this query due to an exception.");
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

	}

}