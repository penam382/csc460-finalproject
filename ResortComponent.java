import java.sql.*;

/*
 * Class ResortComponent:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class is a superclass used by classes that contains common behavior between add/update/delete classes
 *  for the ski resort.
 * 
 *  Inst Methods:
 * 		createNewId(Connection dbconn, String tableName, String columnName)
 * 		existsId(Connection dbconn, int givenId, String tableName, String idColumn)
 * 		createNewTransaction(Connection dbconn, int transactionId, int resortPropertyId, int memberId, String transactionType, Timestamp xactDateTime, double amount)
 * 		deleteFromWhere(Connection dbconn, String tableName, String idName, int givenId)
 */
public class ResortComponent {

	/*
	 * createNewId(Connection dbconn, String tableName, String columnName)
	 * 
	 * Purpose: This function creates a new unique ID for specified table
	 * 
	 * Returns: The created unique ID
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  tableName: The table creating a new ID for
	 *  columnName: The ID column name for the table.
	 * 
	 */
	public int createNewId(Connection dbconn, String tableName, String columnName) {
		Statement stmt = null;
		ResultSet answer = null;
		int newId = 1;

		try{
			String query = String.format("SELECT MAX(%s) AS maxId FROM %s", columnName, tableName);
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

	/*
	 * existsId(Connection dbconn, int givenId, String tableName, String idColumn)
	 * 
	 * Purpose: This function determine whether there exists a queried ID in a given table
	 * 
	 * Returns: True if entry with ID found, false otherwise.
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  givenId: The id being searched for in tableName
	 *  tableName: The name of the table being searched in
	 *  idColumn: The name of the id column in tableName
	 * 
	 */
	public boolean existsId(Connection dbconn, int givenId, String tableName, String idColumn) {
		Statement stmt = null;
		ResultSet answer = null;
		boolean found = false;

		try{
			String query = String.format("SELECT * FROM %s WHERE %s = %s", tableName, idColumn, givenId);
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			int numResults = 0;
			while(answer.next() && numResults < 1) {
				numResults++;
			}

			if(numResults == 1) {
				found = true;
			} else {
				System.out.println(String.format("ERROR: No %s with this ID found!", idColumn));
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

	/*
	 * createNewTransaction(Connection dbconn, int transactionId, int resortPropertyId, int memberId, String transactionType, Timestamp xactDateTime, double amount)
	 * 
	 * Purpose: This function creates a new transaction with specified params
	 * 
	 * Returns: True if successful, false otherwise.
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  transactionId: The unique ID made for this transaction
	 *  resortPropertyId: The property that the transaction is assoc. with.
	 *  memberId: The id of the member making the transaction
	 *  transactionType: The type of the transaction
	 *  xactDateTime: The timestamp of the transaction
	 *  amount: The cost of the transaction.
	 * 
	 */
	public boolean createNewTransaction(Connection dbconn, int transactionId, int resortPropertyId, int memberId, String transactionType,
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
	 * deleteFromWhere(Connection dbconn, String tableName, String idName, int givenId)
	 * 
	 * Purpose: This function calls a DELETE FROM WHERE call to the database
	 * 
	 * Returns: True if successful, false otherwise.
	 * 
	 * Parameters:
	 * 	dbconn: The connection to the database
	 *  tableName: The table to delete from
	 *  idName: The name of the id column in tableName
	 *  givenId: The id corresponding to record to delete from the table.
	 * 
	 */
	public boolean deleteFromWhere(Connection dbconn, String tableName, String idName, int givenId) {
		PreparedStatement stmt = null;

		try {
			String deleteQuery = String.format("DELETE FROM %s WHERE %s = %s", tableName, idName, givenId);
			stmt = dbconn.prepareStatement(deleteQuery);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "Could not perform operations to delete Ski Pass.");
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
}
