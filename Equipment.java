import java.sql.*;

public class Equipment {
	public Equipment() {

	}

	private int createEquipmentId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(itemId) AS maxId FROM RentalInventory";
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

	private int createNewRentalChangeLogId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(rentalChangeLogId) AS maxId FROM RentalChangeLog";
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

	public boolean addNewEquipment(Connection dbconn, int resortPropertyId, String itemType, String itemSize) {
		// Create unique equipment ID
		int newEquipmentId = createEquipmentId(dbconn);

		// verify resort property id, quit if invalid
		if(!existsResortPropertyId(dbconn, resortPropertyId)){
			return false;
		}

		// Add content: itemType, itemSize, archived = 0
		PreparedStatement stmt = null;

		try {
			String query = "INSERT INTO RentalInventory " +
						   "(itemId, resortPropertyId, itemType, itemSize) " +
						   "VALUES (?, ?, ?, ?)";

			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, newEquipmentId);
			stmt.setInt(2, resortPropertyId);
			stmt.setString(3, itemType);
			stmt.setString(4, itemSize);

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

		return true;
	}

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
			int newRentalChangeLogId = createNewRentalChangeLogId(dbconn);

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
			int newRentalChangeLogId = createNewRentalChangeLogId(dbconn);

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
}