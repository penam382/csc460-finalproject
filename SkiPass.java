import java.sql.*;

/*
 * Class Equipment:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class contains functions that allow required system Adds, Updates, and Deletes
 *  with regard to ski passes at the Ski Resort.
 * 
 *  Inst Methods:
 * 		createNewSkiPass(Connection dbconn, int skiPassId, int remainingUses, Date expirationDate)
 * 		newSkiPass(Connection dbconn, int memberId, int resortPropertyId, Timestamp xactDateTime, double amount, int remainingUses, Date expirationDate)
 * 		skiPassUsed(Connection dbconn, int skiPassId)
 * 		resetRemainingUses(Connection dbconn, int skiPassId, int newRemUses)
 * 		hasRemainingUses(Connection dbconn, int skiPassId)
 * 		deleteSkiPass(Connection dbconn, int skiPassId)
 * 		skiPassQuery(Connection dbconn, int skiPassId)
 */
public class SkiPass extends ResortComponent{

	public SkiPass() {

	}

	/*
	 * createNewSkiPass(Connection dbconn, int skiPassId, int remainingUses, Date expirationDate)
	 * 
	 * Purpose: This function creates a new ski pass
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The unique ID for the new ski pass
	 *  remainingUses: The number of remaining uses (or initial purchase uses) for the ski pass
	 *  expirationDate: The date when the ski pass expires
	 * 
	 */
	private boolean createNewSkiPass(Connection dbconn, int skiPassId, int remainingUses, Date expirationDate) {
		PreparedStatement stmt = null;

		try{
			String query = "INSERT INTO SkiPass " +
                "(skiPassId, numberOfUses, remainingUses, expirationDate) " +
                "VALUES (?, ?, ?, ?)";

			int numberOfUses = 0;

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, skiPassId);
        	stmt.setInt(2, numberOfUses);
        	stmt.setInt(3, remainingUses);
        	stmt.setDate(4, expirationDate);

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
	 * createNewSkiPassXactDetails(Connection dbconn, int skiPassXactDetailsId, int transactionId, int skiPassId)
	 * 
	 * Purpose: This function creates a new set of ski pass transaction details for a ski pass purchase
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassXactDetailsId: The unique ID for this ski pass transactions details
	 *  transactionId: The id of the overarching transaction for the ski pass purchase
	 *  skiPassId: The id of the ski pass purchase
	 * 
	 */
	private boolean createNewSkiPassXactDetails(Connection dbconn, int skiPassXactDetailsId, int transactionId, int skiPassId) {
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
	 * newSkiPass(Connection dbconn, int memberId, int resortPropertyId, Timestamp xactDateTime, double amount, int remainingUses, Date expirationDate)
	 * 
	 * Purpose: This function is the wrapper to perform creation of a new ski pass, including creating a new transaction and ski pass transaction details
	 * and the ski pass itself.
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  memberId: The id of the member purchasing a ski pass
	 *  resortPropertyId: The id of the resort property that the ski pass was purchased at
	 *  xactDateTime: The time of the ski pass purchase
	 *  amount: The cost of the ski pass
	 *  remainingUses: The amount of uses for the purchased ski pass
	 *  expirationDate: the expiration date for the ski pass
	 * 
	 */
	public boolean newSkiPass(Connection dbconn, int memberId, int resortPropertyId, Timestamp xactDateTime,
		double amount, int remainingUses, Date expirationDate) {
			// createNewXactId
			int newTransactionId = createNewId(dbconn, "Transactions", "transactionId");

			// Validate memberId, resortPropertyId
			if(!existsId(dbconn, memberId, "MemberAccount", "memberId")) {
				return false;
			}
			
			if(!existsId(dbconn, resortPropertyId, "ResortProperty", "resortPropertyId")){
				return false;
			}

			// create new Transaction, content: xactId, resortPropertyId, memberId, type, dateTime, amount
			boolean successXact = createNewTransaction(dbconn, newTransactionId, resortPropertyId, memberId, "Ski Pass", xactDateTime, amount);
			
			if(!successXact) {
				System.out.println("ERROR: Failed to create new transaction");
				return false;
			}

			// createNewSkiPassId
			int newSkiPassId = createNewId(dbconn, "SkiPass", "skiPassId");

			// create new SkiPass, content: skiPassId, numUses = 0, remainingUses, expirDate
			boolean successSkiPass = createNewSkiPass(dbconn, newSkiPassId, remainingUses, expirationDate);

			if(!successSkiPass){
				System.out.println("ERROR: Failed to create new ski pass");
				return false;
			}

			// create NewSkiPassXactDetailsId
			int newSkiPassXactDetailsId = createNewId(dbconn, "SkiPassXactDetails", "skiPassXactDetailsId");

			// create new skiPassXactDetails, content: id, transactionId, skiPassId
			boolean successSkiPasXact = createNewSkiPassXactDetails(dbconn, newSkiPassXactDetailsId, newTransactionId, newSkiPassId);

			if(!successSkiPasXact) {
				System.out.println("ERROR: Failed to create new SkiPassXact");
				return false;
			}

			return true;
		}

	/*
	 * skiPassUsed(Connection dbconn, int skiPassId)
	 * 
	 * Purpose: This function acts when the ski pass is used, checking that the pass is usable, increasing number of uses, decreasing
	 * uses remaining.
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The id of the ski pass being used.
	 * 
	 */
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
				return false;
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
	 * resetRemainingUses(Connection dbconn, int skiPassId, int newRemUses)
	 * 
	 * Purpose: This function allows resetting remaining uses for a ski pass to a specified number
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The id of the ski pass being reset
	 *  newRemUses: The new number of remaining uses for the ski pass
	 * 
	 */
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
				return false;
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
	 * hasRemainingUses(Connection dbconn, int skiPassId) 
	 * 
	 * Purpose: This function checks if a ski pass has remaining uses left
	 * 
	 * Returns: True if remaining uses exist, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The id of the ski pass being checked
	 * 
	 */
	public boolean hasRemainingUses(Connection dbconn, int skiPassId) {
		PreparedStatement stmt = null;
		ResultSet answer = null;
		boolean hasUses = false;
		
		try {
			String query = "SELECT remainingUses FROM SkiPass WHERE skiPassId = ?";
			
			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, skiPassId);
			
			answer = stmt.executeQuery();
			
			if (answer.next()) {
				int remainingUses = answer.getInt("remainingUses");
				if (remainingUses > 0) {
					hasUses = true;
				}
			} else {
				System.out.println("ERROR: Ski pass not found.");
				return false;
			} 
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
				+ "Could not execute query.");
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
		
		return hasUses;
	}

	/*
	 * deleteSkiPass(Connection dbconn, int skiPassId)
	 * 
	 * Purpose: This function deletes a ski pass if it has no remaining uses and is expired.
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The id of the ski pass being deleted
	 * 
	 */
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
			if(!answer.next()) {
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

			stmt = dbconn.prepareStatement(archiveInfoQuery);
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
			int newSkiPassArchiveId = createNewId(dbconn, "SkiPassArchive", "skiPassArchiveId");

			// Place newSkiPassArchiveId, MemberId, NumberOfUses in SkiPassArchive
			String insertIntoArchiveQuery = "INSERT INTO SkiPassArchive (skiPassArchiveId, memberId, numberOfUses) VALUES (?, ?, ?)";

			stmt = dbconn.prepareStatement(insertIntoArchiveQuery);
			stmt.setInt(1, newSkiPassArchiveId);
			stmt.setInt(2, memberId);
			stmt.setInt(3, numUses);
			stmt.executeUpdate();
			stmt.close();

			System.out.println("Successfully archived SkiPass");

			// Delete all entries with skiPassId from SkiPassXactDetails, RentalXactDetails, LiftUsage, SkiPass

			// Delete from SkiPassXactDetails
			boolean successDelSkiPassXact = deleteFromWhere(dbconn, "SkiPassXactDetails", "skiPassId", skiPassId);
			if(!successDelSkiPassXact) {
				return false;
			}

			// Delete from RentXactDetails
			boolean successDelRentXact = deleteFromWhere(dbconn, "RentalXactDetails", "skiPassId", skiPassId);
			if(!successDelRentXact) {
				return false;
			}

			// Delete from LiftUsage
			boolean successDelLiftUsage = deleteFromWhere(dbconn, "LiftUsage", "skiPassId", skiPassId);
			if(!successDelLiftUsage) {
				return false;
			}

			// Delete from SkiPass
			boolean successDelSkiPass = deleteFromWhere(dbconn, "SkiPass", "skiPassId", skiPassId);
			if(!successDelSkiPass) {
				return false;
			}

		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform operations to delete Ski Pass.");
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

	/*
	 * skiPassQuery(Connection dbconn, int skiPassId)
	 * 
	 * Purpose: This function performs the ski pass query, for a given ski pass, listing all lift rides and
	 * equipment rentals associated with it, along with timestamps and return statuses.
	 * 
	 * Returns: True if successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  skiPassId: The id of the ski pass being queried on
	 * 
	 */
	public boolean skiPassQuery(Connection dbconn, int skiPassId){
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

			System.out.println("Lift Rides:");
			System.out.println("[Lift Name | Usage Date Time]");

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
								 "JOIN ItemInRental iir ON rx.rentalXactDetailsId = iir.rentalXactDetailsId " +
								 "JOIN RentalInventory ri ON iir.itemId = ri.itemID " +
								 "WHERE rx.skiPassId = ? " +
								 "ORDER BY t.transactionDateTime";

			stmt = dbconn.prepareStatement(rentalQuery);
			stmt.setInt(1, skiPassId);
			answer = stmt.executeQuery();

			System.out.println("Equipment Rentals:");
			System.out.println("[Rental Xact ID | Xact Date Time | Item Type | Item Size | Return Status ]");

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