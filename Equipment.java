import java.sql.*;

/*
 * Class Equipment:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class contains functions that allow required system Adds, Updates, and Deletes
 *  with regard to the equipment inventory at the Ski Resort.
 * 
 *  Inst Methods:
 * 		addNewEquipment(Connection dbconn, int resortPropertyId, String itemType, String itemSize)
 * 		changeEquipmentType(Connection dbconn, int itemId, String newType)
 * 		changeEquipmentSize(Connection dbconn, int itemId, String newSize)
 * 		deleteEquipment(Connection dbconn, int itemId)
 */
public class Equipment extends ResortComponent {
	public Equipment() {

	}

	/*
	 * addNewEquipment(Connection dbconn, int resortPropertyId, String itemType, String itemSize)
	 * 
	 * Purpose: This class performs an insertion of a new equipment item to the resort's equipment inventory
	 * 
	 * Returns: True if insertion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  resortPropertyId: The Id of the property this equipment is being assinged to
	 *  itemType: The type of item being inserted
	 *  itemSize: The size of the item being inserted
	 * 
	 */
	public boolean addNewEquipment(Connection dbconn, int resortPropertyId, String itemType, String itemSize) {
		// Create unique equipment ID
		int newEquipmentId = createNewId(dbconn, "RentalInventory", "itemId");
		System.out.println("The equipment ID is: " + newEquipmentId);

		// verify resort property id, quit if invalid
		if(!existsId(dbconn, resortPropertyId, "ResortProperty", "resortPropertyId")){
			return false;
		}

		// Add content: itemType, itemSize, archived = 0
		PreparedStatement stmt = null;

		try {
			String query = "INSERT INTO RentalInventory " +
						   "(itemId, resortPropertyId, itemType, itemSize, archived) " +
						   "VALUES (?, ?, ?, ?, ?)";

			int archived = 0;

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, newEquipmentId);
			stmt.setInt(2, resortPropertyId);
			stmt.setString(3, itemType);
			stmt.setString(4, itemSize);
			stmt.setInt(5, archived);

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
	 * changeEquipmentType(Connection dbconn, int itemId, String newType)
	 * 
	 * Purpose: This class changes the type of a piece of equipment
	 * 
	 * Returns: True if update successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  itemId: The ID of the item in the database to change its type
	 *  newType: The equipment type to change an item to
	 */
	public boolean changeEquipmentType(Connection dbconn, int itemId, String newType) {
		PreparedStatement stmt = null;
		ResultSet answer = null;
		boolean updated = false;

		try {
			// Get itemId, itemType, itemSize before change
			String itemType = "";
			String itemSize = "";

			String query = "SELECT RentalInventory.itemType, RentalInventory.itemSize " +
						"FROM RentalInventory " +
						"WHERE RentalInventory.itemId = ? AND RentalInventory.archived = 0";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, itemId);
			answer = stmt.executeQuery();

			if(answer.next()) {
				itemType = answer.getString("itemType");
				itemSize = answer.getString("itemSize");
			} else {
				System.out.println("Error: Could not find an equipment item with this ID");
				return false;
			}

			stmt.close();
			answer.close();
			
			// save current version to RentalChangeLog
			int newRentalChangeLogId = createNewId(dbconn, "RentalChangeLog", "rentalChangeLogId");

			String insertIntoChangeLogQuery = "INSERT INTO RentalChangeLog (rentalChangeLogId, itemId, itemType, itemSize, changeDate) VALUES (?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(insertIntoChangeLogQuery);
			stmt.setInt(1, newRentalChangeLogId);
			stmt.setInt(2, itemId);
			stmt.setString(3, itemType);
			stmt.setString(4, itemSize);
			java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
			stmt.setDate(5, today);
			stmt.executeUpdate();
			stmt.close();

			// make updates
			String updateQuery = "UPDATE RentalInventory SET itemType = ? WHERE itemId = ?";

			stmt = dbconn.prepareStatement(updateQuery);
			stmt.setString(1, newType);
			stmt.setInt(2, itemId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update equipment type.");
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
	 * changeEquipmentSize(Connection dbconn, int itemId, String newSize)
	 * 
	 * Purpose: This class changes the size of a piece of equipment
	 * 
	 * Returns: True if update successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  itemId: The ID of the item in the database to change its size
	 *  newSize: The equipment size to change an item to
	 */
	public boolean changeEquipmentSize(Connection dbconn, int itemId, String newSize) {
		PreparedStatement stmt = null;
		ResultSet answer = null;
		boolean updated = false;

		try {
			// Get itemId, itemType, itemSize before change
			String itemType = "";
			String itemSize = "";

			String query = "SELECT RentalInventory.itemType, RentalInventory.itemSize " +
						"FROM RentalInventory " +
						"WHERE RentalInventory.itemId = ? AND RentalInventory.archived = 0";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, itemId);
			answer = stmt.executeQuery();

			if(answer.next()) {
				itemType = answer.getString("itemType");
				itemSize = answer.getString("itemSize");
			} else {
				System.out.println("Error: Could not find an equipment item with this ID");
				return false;
			}

			stmt.close();
			answer.close();
			
			// save current version to RentalChangeLog
			int newRentalChangeLogId = createNewId(dbconn, "RentalChangeLog", "rentalChangeLogId");

			String insertIntoChangeLogQuery = "INSERT INTO RentalChangeLog (rentalChangeLogId, itemId, itemType, itemSize, changeDate) VALUES (?, ?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(insertIntoChangeLogQuery);
			stmt.setInt(1, newRentalChangeLogId);
			stmt.setInt(2, itemId);
			stmt.setString(3, itemType);
			stmt.setString(4, itemSize);
			java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
			stmt.setDate(5, today);
			stmt.executeUpdate();
			stmt.close();

			// make updates
			String updateQuery = "UPDATE RentalInventory SET itemSize = ? WHERE itemId = ?";

			stmt = dbconn.prepareStatement(updateQuery);
			stmt.setString(1, newSize);
			stmt.setInt(2, itemId);

			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to update equipment size.");
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
	 * deleteEquipment(Connection dbconn, int itemId)
	 * 
	 * Purpose: This function 'deletes' a piece of equipment by marking it as archived
	 * 
	 * Returns: True if deletion successful, false otherwise
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  itemId: The ID of the item to delete
	 */
	public boolean deleteEquipment(Connection dbconn, int itemId) {
		PreparedStatement stmt = null;
		ResultSet answer = null;
		boolean updated = false;

		try {
			// Check if item deletable (not currently rented out/reserved)
			String checkDeletableQuery = "SELECT * FROM RentalInventory " +
										 "JOIN ItemInRental ON RentalInventory.itemId = ItemInRental.itemId " +
										 "JOIN RentalXactDetails ON ItemInRental.rentalXactDetailsId = RentalXactDetails.rentalXactDetailsId " +
										 "WHERE RentalInventory.itemId = ? AND RentalXactDetails.returnStatus = 0";
			
			stmt = dbconn.prepareStatement(checkDeletableQuery);
			stmt.setInt(1, itemId);
			answer = stmt.executeQuery();

			// End if Equipment is not Deletable
			if(answer.next()) {
				System.out.println("Error: This Equipment cannot be deleted because it is currently rented out.");
				return false;
			}

			stmt.close();
			answer.close();

			// Mark equipment as archived (pseudo deletion)
			String archiveEquipmentQuery = "UPDATE RentalInventory SET archived = 1 WHERE itemId = ?";

			stmt = dbconn.prepareStatement(archiveEquipmentQuery);
			stmt.setInt(1, itemId);
			
			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0) {
				updated = true;
			} else {
				System.out.println("ERROR: Failed to archive equipment.");
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

	// Helper function to print all resort properties
	public void viewResortProperties(Connection dbconn) {
		boolean tryShowProperties = showAllNames(dbconn, "ResortProperty", "resortPropertyId", "propertyType");
	}
}