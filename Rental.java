import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Rental extends ResortComponent{
	public Rental() {

	}

	private boolean createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId){
		// Validate itemId, rentalXactDetailsId
		if(!existsId(dbconn, itemId, "RentalInventory", "itemId")){
			return false;
		}

		if(!existsId(dbconn, RentalXactDetailsId, "RentalXactDetails", "rentalXactDetailsId")){
			return false;
		}

		// create itemInRentalId
		int newItemInRentalId = createNewId(dbconn, "ItemInRental", "itemInRentalId");

		// Populate Content: itemId, RentalXactDetialsId
		PreparedStatement stmt = null;

		try {
			String query = "INSERT INTO ItemInRental " +
						   "(itemInRentalId, itemId, rentalXactDetailsId) " +
						   "VALUES (?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, newItemInRentalId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, RentalXactDetailsId);

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

	private boolean createNewRentalXactDetails(Connection dbconn, int rentalXactDetailsId, int transactionId, int skiPassId) {
		PreparedStatement stmt = null;

		try{
			String query = "INSERT INTO RentalXactDetails " +
                "(rentalXactDetailsId, transactionId, skiPassId, returnStatus, dateReturned) " +
                "VALUES (?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, rentalXactDetailsId);
        	stmt.setInt(2, transactionId);
        	stmt.setInt(3, skiPassId);
			stmt.setInt(4, 0);
			stmt.setDate(5, null);

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

	public boolean createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType,
	Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds) {
		// Create Transaction Id
		int newTransactionId = createNewId(dbconn, "Transactions", "transactionId");

		// verify resortPropertyId, memberId
		if(!existsId(dbconn, memberId, "MemberAccount", "memberId")) {
			return false;
		}
		
		if(!existsId(dbconn, resortPropertyId, "ResortProperty", "resortPropertyId")){
			return false;
		}

		// populate transaction content: resortPropId, memberId, transactionType, transactionDateTime, amount
		boolean successXact = createNewTransaction(dbconn, newTransactionId, resortPropertyId, memberId, "Rental", dateTime, amount);
		if(!successXact) {
			return false;
		}

		// Create RentalXactDetailsId
		int newRentalXactDetailsId = createNewId(dbconn, "RentalXactDetails", "rentalXactDetailsId");

		// Validate skiPassId
		if(!existsId(dbconn, skiPassId, "SkiPass", "skiPassId")) {
			return false;
		}

		// fill content for RentalXactDetails: TransactionId, skiPassId, returnStatus = 0, dateReturned = NULL
		boolean successRentalXact = createNewRentalXactDetails(dbconn, newRentalXactDetailsId, newTransactionId, skiPassId);
		if(!successRentalXact) {
			return false;
		}

		// For itemId in itemIds
			// create ItemInRental
		for(int i = 0; i < itemIds.size(); i++) {
			int itemId = itemIds.get(i);

			boolean successItemInRental = createItemInRental(dbconn, itemId, newRentalXactDetailsId);
			if(!successItemInRental) {
				return false;
			}
		}

		return true;
	}

	public boolean setRentalXactReturned(Connection dbconn, int rentalXactDetailsId, Date dateReturned) {
		PreparedStatement stmt = null;
		boolean updated = false;

		try {
			String query = "UPDATE RentalXactDetails SET returnStatus = 1, dateReturned = ? WHERE rentalXactDetailsId = ?";

			stmt = dbconn.prepareStatement(query);
			stmt.setDate(1, dateReturned);
			stmt.setInt(2, rentalXactDetailsId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update Rental return.");
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

	public boolean deleteRentalXact(Connection dbconn, int rentalXactDetailsId) {

		// Delete ItemInRental
		boolean successDelItemInRental = deleteFromWhere(dbconn, "ItemInRental", "rentalXactDetailsId", rentalXactDetailsId);
		if(!successDelItemInRental) {
			return false;
		}

		// Delete RentalXact
		boolean successDelRentalXact = deleteFromWhere(dbconn, "RentalXactDetails", "rentalXactDetailsId", rentalXactDetailsId);
		if(!successDelRentalXact) {
			return false;
		}

		return true;
	}
}