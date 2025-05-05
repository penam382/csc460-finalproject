import java.sql.*;
import java.sql.Date;
import java.util.*;

/*
 * Class Rental:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class contains functions that allow required system Adds, Updates, and Deletes
 *  with regard to the equipment rentals at the Ski Resort.
 * 
 *  Inst Methods:
 * 		createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId)
 * 		createNewRentalXactDetails(Connection dbconn, int rentalXactDetailsId, int transactionId, int skiPassId)
 * 		createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType, Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds)
 * 		setRentalXactReturned(Connection dbconn, int rentalXactDetailsId, Date dateReturned)
 * 		deleteRentalXact(Connection dbconn, int rentalXactDetailsId)
 */
public class Rental extends ResortComponent{
	public Rental() {

	}

	/*
	 * createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId)
	 * 
	 * Purpose: This function creates a new item in rental, which connects a rental transaction to item(s) in the rental.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  itemId: The id of the item being rented
	 * 	RentalXactDetailsId: The ID of the rental transaction this item in rental is assoc. with
	 * 
	 */
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

	/*
	 * createNewRentalXactDetails(Connection dbconn, int rentalXactDetailsId, int transactionId, int skiPassId)
	 * 
	 * Purpose: This function creates a new set of details for a rental transaction
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  rentalXactDetailsId: The unique Id for this set of details for the transactions
	 *  transactionId: the id for the overarching transaction for this rental
	 *  skiPassId: The ski pass id associated with this rental.
	 * 
	 */
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

	/*
	 * createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType, Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds)
	 * 
	 * Purpose: This function is a wrapper around a rental transaction, creating a new overall transaction, new rental transaction details, and associating
	 * the transaction with the items being rented.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  resortPropertyId: The id of the property the rental is associated with
	 *  memberId: The member renting item(s)
	 *  transactionType: The type of transaction ("Rental")
	 *  dateTime: the time of the rental transaction
	 *  amount: The cost of the rental
	 *  skiPassId: The id of the ski pass associated with the rental
	 *  itemIds: The list of ids for items in the rental transaction
	 * 
	 */
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
			System.out.println("ERROR: Couldn't create a new transaction.");
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
			System.out.println("ERROR: couldn't create a rentalXact inside Rental");
			return false;
		}

		// For itemId in itemIds
			// create ItemInRental
		for(int i = 0; i < itemIds.size(); i++) {
			int itemId = itemIds.get(i);

			boolean successItemInRental = createItemInRental(dbconn, itemId, newRentalXactDetailsId);
			if(!successItemInRental) {
				System.out.println("ERROR: Couldn't create an item in rental in Rental");
				return false;
			}
		}

		return true;
	}

	/*
	 * setRentalXactReturned(Connection dbconn, int rentalXactDetailsId, Date dateReturned)
	 * 
	 * Purpose: This function performs a return of a rental item, marking the transaction as returned and saving the return date.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  rentalXactDetailsId: The id of the rental transaction whose items are being returned
	 *  dateReturned: The date in which items in this rental were returned.
	 * 
	 */
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

	/*
	 * deleteRentalXact(Connection dbconn, int rentalXactDetailsId)
	 * 
	 * Purpose: This function deletes a rental transaction.
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  rentalXactDetailsId: The id of the rental transaction which is being deleted
	 * 
	 */
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