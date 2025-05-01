import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Rental {
	public Rental() {

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

	private int createRentalXactDetailsId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(rentalXactDetailsId) AS maxId FROM RentalXactDetails";
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

	private int createItemInRentalId(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = "SELECT MAX(itemInRentalId) AS maxId FROM ItemInRental";
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

	public boolean existsItemId(Connection dbconn, int givenItemId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM RentalInventory WHERE itemId = " + givenItemId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No item with this ID found!");
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

	public boolean existsrentalXactDetailsId(Connection dbconn, int givenrentalXactDetailsId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM RentalXactDetails WHERE rentalXactDetailsId = " + givenrentalXactDetailsId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No rental xact details with this ID found!");
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

	private boolean createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId){
		// Validate itemId, rentalXactDetailsId
		if(!existsItemId(dbconn, itemId)){
			return false;
		}

		if(!existsrentalXactDetailsId(dbconn, RentalXactDetailsId)){
			return false;
		}

		// create itemInRentalId
		int newItemInRentalId = createItemInRentalId(dbconn);

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

	public boolean existsSkiPassId(Connection dbconn, int givenSkiPassId) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = "SELECT * FROM SkiPass WHERE skiPassId = " + givenSkiPassId;
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println("ERROR: No ski pass with this ID found!");
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

	public void createNewRentalXactDetails(Connection dbconn, int rentalXactDetailsId, int transactionId, int skiPassId) {
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

	public boolean createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType,
	Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds) {
		// Create Transaction Id
		int newTransactionId = createNewTransactionId(dbconn);

		// verify resortPropertyId, memberId
		if(!existsMemberId(dbconn, memberId)) {
			return false;
		}
		
		if(!existsResortPropertyId(dbconn, resortPropertyId)){
			return false;
		}

		// populate transaction content: resortPropId, memberId, transactionType, transactionDateTime, amount
		createNewTransaction(dbconn, newTransactionId, resortPropertyId, memberId, "Rental", dateTime, amount);

		// Create RentalXactDetailsId
		int newRentalXactDetailsId = createRentalXactDetailsId(dbconn);

		// Validate skiPassId
		if(!existsSkiPassId(dbconn, skiPassId)) {
			return false;
		}

		// fill content for RentalXactDetails: TransactionId, skiPassId, returnStatus = 0, dateReturned = NULL
		createNewRentalXactDetails(dbconn, newRentalXactDetailsId, newTransactionId, skiPassId);

		// For itemId in itemIds
			// createItemInRental()
		for(int i = 0; i < itemIds.size(); i++) {
			int itemId = itemIds.get(i);

			createItemInRental(dbconn, itemId, newRentalXactDetailsId);
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

	public boolean deleteRentalXact(Connection dbconn, int rentalXactDetailsId) {
		PreparedStatement stmt = null;
		ResultSet answer = null;

		try {
			String deleteFromItemInRental = "DELETE FROM ItemInRental WHERE rentalXactDetailsId = ?";

			stmt = dbconn.prepareStatement(deleteFromItemInRental);
			stmt.setInt(1, rentalXactDetailsId);
			stmt.executeUpdate();
			stmt.close();

			String deleteRentalXactDetails = "DELETE FROM RentalXactDetails WHERE rentalXactDetailsId = ?";

			stmt = dbconn.prepareStatement(deleteRentalXactDetails);
			stmt.setInt(1, rentalXactDetailsId);
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
}