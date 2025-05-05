import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Class functionalityTest:
 * 	Author: Seth Jernigan, Marco Pena
 *  Purpose: This class performs an extensive set of test cases to test functionality throughout
 *  the program.
 * 
 */
public class functionalityTest {

	private static Connection dbconn = null;

	public static void main(String[] args) {
		final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        // Seth Login Info
        String username = "sajernigan";
	    String password = "a9330";

        // Load the Oracle JDBC driver by initializing its base class
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }

        try {
            // Make connection to Oracle database, store in dbconn variable
            dbconn = DriverManager.getConnection(oracleURL, username, password);
            System.out.println("Connected to Oracle database.");

            // Execute Tests
			runTests(dbconn);
            
            // close and give a message
            dbconn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {

            System.err.println("*** SQLException:  "
                + "Could not open JDBC connection.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

        }
	}

	private static boolean runTests(Connection dbconn) {
		boolean memberRes = memberTests(dbconn);
		if(!memberRes) {
			System.out.println("--FAILED MEMBER TEST--");
			return false;
		} else {
			System.out.println("--PASSED MEMBER TEST--");
		}

		boolean skiPassRes = skiPassTests(dbconn);
		if(!skiPassRes) {
			System.out.println("--FAILED SKIPASS TEST--");
			return false;
		} else {
			System.out.println("--PASSED SKIPASS TEST--");
		}

		boolean equipInvRes = equipInvTests(dbconn);
		if(!equipInvRes) {
			System.out.println("--FAILED EQUIP INVENTORY TEST--");
			return false;
		} else {
			System.out.println("--PASSED EQUIP INVENTORY TEST--");
		}


		boolean equipRentalRes = equipRentalTests(dbconn);
		if(!equipRentalRes) {
			System.out.println("--FAILED EQUIP RENTAL TEST--");
			return false;
		} else {
			System.out.println("--PASSED EQUIP RENTAL TEST--");
		}


		boolean lessonPurchaseRes = lessonPurchaseTests(dbconn);
		if(!lessonPurchaseRes) {
			System.out.println("--FAILED LESSON PURCHASE TEST--");
			return false;
		} else {
			System.out.println("--PASSED LESSON PURCHASE TEST--");
		}


		System.out.println("--PASSED--");
		return true;
	}

	private static boolean lessonPurchaseTests(Connection dbconn) {
		int resortPropertyId = 3;
		int memberId = 1;
		Timestamp xactDateTime = Timestamp.valueOf("2022-05-03 14:30:00");
		double amount = 90.00;
		int remSessions = 10;
		int lessonSessionId = 1;
		Date usedDate = java.sql.Date.valueOf("2022-05-05");

		boolean createLessonPurchRes = testCreateLessonPurch(dbconn, resortPropertyId, memberId, xactDateTime, amount, remSessions);
		if(!createLessonPurchRes) {
			System.out.println("ERROR: Failed from testCreateLessonPurch");
			return false;
		}

		int lessonXactDetailsId = existsLessonXact(dbconn, resortPropertyId, memberId, xactDateTime, amount, remSessions);
		if(lessonXactDetailsId < 0) {
			System.out.println("ERROR: Failed from existsLessonXact");
			return false;
		}

		boolean deleteLessonXact = testDeleteLessonXact(dbconn, lessonXactDetailsId);
		if(!deleteLessonXact) {
			System.out.println("ERROR: Failed from testDeleteLessonXact");
			return false;
		}

		createLessonPurchRes = testCreateLessonPurch(dbconn, resortPropertyId, memberId, xactDateTime, amount, remSessions);

		lessonXactDetailsId = existsLessonXact(dbconn, resortPropertyId, memberId, xactDateTime, amount, remSessions);

		boolean useLessonRes = testUseLesson(dbconn, lessonXactDetailsId, lessonSessionId, usedDate, 9, 1);
		if(!useLessonRes) {
			System.out.println("ERROR: Failed from testUseLesson");
			return false;
		}

		// will need to then manually delete this lesson...
		
		return true;
	}

	private static boolean testCreateLessonPurch(Connection dbconn2, int resortPropertyId, int memberId,
			Timestamp xactDateTime, double amount, int remSessions) {

		Lesson newLesson = new Lesson();

		boolean successNewRentXact = newLesson.createLessonXact(dbconn2, resortPropertyId, memberId, xactDateTime, amount, remSessions);

		if(!successNewRentXact) {
			System.out.println("ERROR: Internal error with createLessonXact()");
			return false;
		}	
		
		return true;
	}

	private static int existsLessonXact(Connection dbconn2, int resortPropertyId, int memberId, Timestamp xactDateTime,
			double amount, int remSessions) {
		int lessonXactDetailsId = -1;

		// Return the most recently added id of lessonXactDetails that matches these properties, otherwise return -1
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = 
				"SELECT l.lessonXactDetailsId " +
				"FROM LessonXactDetails l " +
				"JOIN Transactions t ON l.transactionId = t.transactionId " +
				"WHERE t.resortPropertyId = ? " +
				"AND t.memberId = ? " +
				"AND t.amount = ? " +
				"ORDER BY l.lessonXactDetailsId DESC";

			stmt = dbconn2.prepareStatement(sql);
			stmt.setInt(1, resortPropertyId);
			stmt.setInt(2, memberId);
			stmt.setDouble(3, amount);
			rs = stmt.executeQuery();

			if (rs.next()) {
				lessonXactDetailsId = rs.getInt("lessonXactDetailsId");
			} else {
				System.out.println("Failed to find lessonXact");
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException in existsLessonXact ***");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources in existsLessonXact.");
			}
		}

		return lessonXactDetailsId;
	}

	private static boolean testDeleteLessonXact(Connection dbconn2, int lessonXactDetailsId) {
		Lesson newLesson = new Lesson();

		boolean successDelLessXact = newLesson.deleteLessonXact(dbconn2, lessonXactDetailsId);

		if(!successDelLessXact) {
			System.out.println("ERROR: Internal error with deleteLessonXact()");
			return false;
		}	

		// confirm that no lessonXact exists with this id anymore
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT(*) AS count FROM LessonXactDetails WHERE lessonXactDetailsId = ?";
			stmt = dbconn2.prepareStatement(sql);
			stmt.setInt(1, lessonXactDetailsId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt("count");
				if (count != 0) {
					System.out.println("ERROR: LessonXactDetails record still exists after deletion.");
					return false;
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error during verification of deleteLessonXact:");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources during lesson deletion check.");
			}
		}

		System.out.println("Successfully verified deletion of LessonXactDetails.");
		return true;
	}

	private static boolean testUseLesson(Connection dbconn2, int lessonXactDetailsId, int lessonSessionId,
			Date usedDate, int i, int j) {

		Lesson newLesson = new Lesson();

		boolean successUseLesson = newLesson.useLesson(dbconn2, lessonXactDetailsId, lessonSessionId, usedDate);

		if(!successUseLesson) {
			System.out.println("ERROR: Internal error with useLesson()");
			return false;
		}	

		// ensure that the lessonXact with this id has i remaining sessions and j numSessions
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT remainingSessions, numSessions FROM LessonXactDetails WHERE lessonXactDetailsId = ?";
			stmt = dbconn2.prepareStatement(sql);
			stmt.setInt(1, lessonXactDetailsId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int actualRem = rs.getInt("remainingSessions");
				int actualNum = rs.getInt("numSessions");

				if (actualRem != i || actualNum != j) {
					System.out.println("ERROR: Session counts mismatch. Expected remSessions=" + i + ", numSessions=" + j +
							" but got remSessions=" + actualRem + ", numSessions=" + actualNum);
					return false;
				}
			} else {
				System.out.println("ERROR: No LessonXactDetails found for ID: " + lessonXactDetailsId);
				return false;
			}
		} catch (SQLException e) {
			System.err.println("SQL error during useLesson verification:");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources.");
			}
		}

		System.out.println("Successfully verified lesson usage update.");
		return true;
	}

	private static boolean equipRentalTests(Connection dbconn) {
		int resortPropertyId = 2;
		int memberId = 1;
		String transactionType = "Rental";
		Timestamp dateTime = Timestamp.valueOf("2022-05-03 14:30:00");
		double amount = 101.00;
		int skiPassId = 1;
		ArrayList<Integer> itemIds = new ArrayList<>();
		itemIds.add(1);
		itemIds.add(2);
		Date dateReturned = java.sql.Date.valueOf("2022-05-05");


		boolean createRentalXactRes = testCreateRentalXact(dbconn, resortPropertyId, memberId, transactionType, dateTime, amount, skiPassId, itemIds);
		if(!createRentalXactRes) {
			System.out.println("ERROR: Error in testCreateRentalXact");
			return false;
		}

		int rentalXactDetailsId = existsRentalXact(dbconn, resortPropertyId, memberId, transactionType, dateTime, amount, skiPassId, itemIds);
		if(rentalXactDetailsId < 0) {
			System.out.println("ERROR: Error in existsRentalXact");
			return false;
		}

		boolean returnRentalXactRes = testReturnRental(dbconn, rentalXactDetailsId, dateReturned);
		if(!returnRentalXactRes) {
			System.out.println("ERROR: Error in testReturnRental");
			return false;
		}

		boolean deleteRentalXactRest = testDeleteRentXact(dbconn, rentalXactDetailsId);
		if(!deleteRentalXactRest) {
			System.out.println("ERROR: Error in testDeleteRentXact");
			return false;
		}
		
		return true;
	}

	private static boolean testCreateRentalXact(Connection dbconn2, int resortPropertyId, int memberId,
			String transactionType, Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds) {

		Rental newRental = new Rental();

		boolean successNewRentXact = newRental.createRentalXact(dbconn2, resortPropertyId, memberId, transactionType, dateTime, amount, skiPassId, itemIds);

		if(!successNewRentXact) {
			System.out.println("ERROR: Internal error with createRentalXact()");
			return false;
		}		
		
		return true;
	}

	private static int existsRentalXact(Connection dbconn2, int resortPropertyId, int memberId, String transactionType,
        Timestamp dateTime, double amount, int skiPassId, ArrayList<Integer> itemIds) {

		int rentalXactDetailsId = -1;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Find the most recent matching transaction
			String query = "SELECT r.rentalXactDetailsId " +
                       "FROM RentalXactDetails r JOIN Transactions t ON (r.transactionId = t.transactionId) " +
					   "WHERE t.amount = ? AND t.transactionType = ? AND t.memberId = ?";

			stmt = dbconn2.prepareStatement(query);
			stmt.setDouble(1, amount);
			stmt.setString(2, transactionType);
			stmt.setInt(3, memberId);

			rs = stmt.executeQuery();

			if (rs.next()) {
				rentalXactDetailsId = rs.getInt("rentalXactDetailsId");
			} else {
				System.out.println("No matching rental transaction found.");
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException in existsRentalXact");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources in existsRentalXact.");
			}
		}

		return rentalXactDetailsId;
	}

	private static boolean testReturnRental(Connection dbconn2, int rentalXactDetailsId, Date dateReturned) {
		Rental newRental = new Rental();

		boolean successReturnRental = newRental.setRentalXactReturned(dbconn2, rentalXactDetailsId, dateReturned);

		if(!successReturnRental) {
			System.out.println("ERROR: Internal error with setRentalXactReturned()");
			return false;
		}

		// check that the rentalXact with this id has returnStatus == 1
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String checkQuery = "SELECT returnStatus, dateReturned FROM RentalXactDetails WHERE rentalXactDetailsId = ?";
			stmt = dbconn2.prepareStatement(checkQuery);
			stmt.setInt(1, rentalXactDetailsId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int returnStatus = rs.getInt("returnStatus");
				Date dbDateReturned = rs.getDate("dateReturned");

				if (returnStatus != 1 || dbDateReturned == null || !dbDateReturned.equals(dateReturned)) {
					System.out.println("ERROR: RentalXactDetails not correctly updated.");
					return false;
				}
			} else {
				System.out.println("ERROR: No rentalXactDetails entry found for the given ID.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during testReturnRental check ***");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources in testReturnRental.");
				return false;
			}
		}

		System.out.println("Success: Rental marked as returned.");
		return true;
	}

	private static boolean testDeleteRentXact(Connection dbconn2, int rentalXactDetailsId) {
		Rental newRental = new Rental();

		boolean successDeleteRentXact = newRental.deleteRentalXact(dbconn2, rentalXactDetailsId);

		if(!successDeleteRentXact) {
			System.out.println("ERROR: Internal error with deleteRentalXact()");
			return false;
		}

		// Ensure that no rental xact exists with this id
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String checkQuery = "SELECT COUNT(*) AS count FROM RentalXactDetails WHERE rentalXactDetailsId = ?";
			stmt = dbconn2.prepareStatement(checkQuery);
			stmt.setInt(1, rentalXactDetailsId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt("count");
				if (count != 0) {
					System.out.println("ERROR: RentalXactDetails record still exists after deletion.");
					return false;
				}
			} else {
				System.out.println("ERROR: Failed to query RentalXactDetails after deletion.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during testDeleteRentXact check ***");
			System.err.println("\tMessage: " + e.getMessage());
			System.err.println("\tSQLState: " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources in testDeleteRentXact.");
				return false;
			}
		}

		System.out.println("Success: Rental transaction deleted and verified.");
		return true;
	}

	private static boolean equipInvTests(Connection dbconn) {
		int resortPropertyId = 2;
		String itemType = "ski";
		String itemType2 = "pole";
		String itemSize = "150cm";
		String itemSize2 = "170cm";

		boolean newEquipRes = testCreateNewEquip(dbconn, resortPropertyId, itemType, itemSize);
		if(!newEquipRes) {
			return false;
		}

		int itemId = testNewEquipExists(dbconn, resortPropertyId, itemType, itemSize);
		if(itemId < 0) {
			return false;
		}

		boolean changeTypeRes = testChangeType(dbconn, itemId, itemType2);
		if(!changeTypeRes) {
			return false;
		}

		boolean changeSizeRes = testChangeSize(dbconn, itemId, itemSize2);
		if(!changeSizeRes) {
			return false;
		}

		boolean deleteEquipRes = testDeleteEquip(dbconn, itemId);
		if(!deleteEquipRes) {
			return false;
		}


		return true;
	}

	private static boolean testCreateNewEquip(Connection dbconn2, int resortPropertyId, String itemType,
			String itemSize) {
		Equipment newEquip = new Equipment();

		boolean successNewEqip = newEquip.addNewEquipment(dbconn2, resortPropertyId, itemType, itemSize);

		if(!successNewEqip) {
			System.out.println("ERROR: Internal error with addNewEquipment()");
			return false;
		}
		
	    return true;
	}

	private static int testNewEquipExists(Connection dbconn2, int resortPropertyId, String itemType, String itemSize) {
		int itemId = -1;

		// return the id of the most recent item added with these specs, otherwise return -1
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT itemId FROM RentalInventory " +
						"WHERE resortPropertyId = ? AND itemType = ? AND itemSize = ? " +
						"AND ROWNUM = 1 ORDER BY itemId DESC";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, resortPropertyId);
			stmt.setString(2, itemType);
			stmt.setString(3, itemSize);
			rs = stmt.executeQuery();

			if (rs.next()) {
				itemId = rs.getInt("itemId");
			} else {
				System.out.println("No matching rental equipment found.");
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException during testNewEquipExists");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
			}
		}

		return itemId;
	}

	private static boolean testChangeType(Connection dbconn2, int itemId, String itemType2) {
		Equipment newEquip = new Equipment();

		boolean successChangeType = newEquip.changeEquipmentType(dbconn2, itemId, itemType2);

		if(!successChangeType) {
			System.out.println("ERROR: Internal error with changeEquipmentType()");
			return false;
		}

		// check that the item of specified id now has itemType == itemType2
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT itemType FROM RentalInventory WHERE itemId = ?";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, itemId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String actualItemType = rs.getString("itemType");
				if (!actualItemType.equals(itemType2)) {
					System.out.println("ERROR: itemType was not updated correctly. Found: " + actualItemType);
					return false;
				}
			} else {
				System.out.println("ERROR: No item found with itemId " + itemId);
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException during testChangeType");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Item type successfully updated.");
		return true;
	}

	private static boolean testChangeSize(Connection dbconn2, int itemId, String itemSize2) {
		Equipment newEquip = new Equipment();

		boolean successChangeSize = newEquip.changeEquipmentSize(dbconn2, itemId, itemSize2);

		if(!successChangeSize) {
			System.out.println("ERROR: Internal error with changeEquipmentSize()");
			return false;
		}

		// check that the item of specified id now has itemsize == itemsize2
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Query the RentalInventory table to check the updated item size for the specified itemId
			String query = "SELECT itemSize FROM RentalInventory WHERE itemId = ?";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, itemId);
			rs = stmt.executeQuery();

			// If a record is found, check if the itemSize matches itemSize2
			if (rs.next()) {
				String actualItemSize = rs.getString("itemSize");
				if (!actualItemSize.equals(itemSize2)) {
					System.out.println("ERROR: itemSize was not updated correctly. Found: " + actualItemSize);
					return false;
				}
			} else {
				System.out.println("ERROR: No item found with itemId " + itemId);
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException during testChangeSize");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Item size successfully updated.");
		return true;
	}

	private static boolean testDeleteEquip(Connection dbconn2, int itemId) {
		Equipment newEquip = new Equipment();

		boolean successDelEquip = newEquip.deleteEquipment(dbconn2, itemId);

		if(!successDelEquip) {
			System.out.println("ERROR: Internal error with deleteEquipment()");
			return false;
		}

		// check that the item with this id now has archived = 1 (shouldn't actually be gone)
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Query to check if the item is now archived
			String query = "SELECT archived FROM RentalInventory WHERE itemId = ?";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, itemId);
			rs = stmt.executeQuery();

			// If a record is found, check if the 'archived' flag is set to 1
			if (rs.next()) {
				int archivedStatus = rs.getInt("archived");
				if (archivedStatus != 1) {
					System.out.println("ERROR: Equipment not archived. Found archived status: " + archivedStatus);
					return false;
				}
			} else {
				System.out.println("ERROR: No item found with itemId " + itemId);
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException during testDeleteEquip");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Item successfully archived.");
		return true;
	}

	private static boolean skiPassTests(Connection dbconn) {
		int memberId = 1;
		int resortPropertyId = 1;
		Timestamp xactDateTime = Timestamp.valueOf("2022-05-03 14:30:00");
		double amount = 100.00;
		int remainingUses = 10;
		Date expirationDate = java.sql.Date.valueOf("2022-12-20");

		boolean createSkiPassRes = testCreateSkiPass(dbconn, memberId, resortPropertyId, xactDateTime, amount, remainingUses, expirationDate);
		if(!createSkiPassRes) {
			return false;
		}

		int skiPassId = testSkiPassMade(dbconn, memberId, resortPropertyId, xactDateTime, amount, remainingUses, expirationDate);
		if(skiPassId < 0) {
			return false;
		}

		boolean skiPassUsedRes = testSkiPassUsed(dbconn, skiPassId, 9);
		if(!skiPassUsedRes) {
			return false;
		}

		boolean resetRemUsesRes = testSkiPassRemUse(dbconn, skiPassId, 0);
		if(!resetRemUsesRes) {
			return false;
		}

		boolean deleteSkiPassRes = testDelSkiPass(dbconn, skiPassId);
		if(!deleteSkiPassRes) {
			return false;
		}

		boolean skiPassQueryRes = testSkiPassQuery();
		if(!skiPassQueryRes) {
			return false;
		}

		// Edge Deletion cases unchecked...
		// Also ensure they go to archive

		return true;
	}

	private static boolean testSkiPassQuery() {
		SkiPass newSkiPass = new SkiPass();

		boolean successSkiPassQry = newSkiPass.skiPassQuery(dbconn, 1);

		if(!successSkiPassQry) {
			System.out.println("ERROR: Internal error with skiPassQuery()");
			return false;
		}

		return true;
	}

	private static boolean testCreateSkiPass(Connection dbconn2, int memberId, int resortPropertyId,
			Timestamp xactDateTime, double amount, int remainingUses, Date expirationDate) {

		SkiPass newSkiPass = new SkiPass();

		boolean successNewSkiPass = newSkiPass.newSkiPass(dbconn2, memberId, resortPropertyId, xactDateTime, amount, remainingUses, expirationDate);

		if(!successNewSkiPass) {
			System.out.println("ERROR: Internal error with newSkiPass()");
			return false;
		}

		return true;
	}

	private static int testSkiPassMade(Connection dbconn2, int memberId, int resortPropertyId, Timestamp xactDateTime,
			double amount, int remainingUses, Date expirationDate) {
		int skiPassId = -1;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query =
				"SELECT sp.skiPassId " +
				"FROM SkiPass sp " +
				"JOIN SkiPassXactDetails sx ON sp.skiPassId = sx.skiPassId " +
				"JOIN Transactions t ON sx.transactionId = t.transactionId " +
				"WHERE t.memberId = ? " +
				"AND t.resortPropertyId = ? " +
				"AND t.transactionDateTime = ? " +
				"AND t.amount = ? " +
				"AND sp.remainingUses = ? " +
				"AND sp.expirationDate = ? " +
				"ORDER BY sp.skiPassId DESC";

			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, memberId);
			stmt.setInt(2, resortPropertyId);
			stmt.setTimestamp(3, xactDateTime);
			stmt.setDouble(4, amount);
			stmt.setInt(5, remainingUses);
			stmt.setDate(6, expirationDate);

			rs = stmt.executeQuery();

			if (rs.next()) {
				skiPassId = rs.getInt("skiPassId");
			} else {
				System.out.println("No matching SkiPass found.");
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during SkiPass lookup.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return -1;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return -1;
			}
		}

		System.out.println("SkiPass created successfully and verified");
		return skiPassId;
	}

	private static boolean testSkiPassUsed(Connection dbconn2, int skiPassId, int i) {

		SkiPass newSkiPass = new SkiPass();

		boolean successSkiPassUsed = newSkiPass.skiPassUsed(dbconn2, skiPassId);

		if(!successSkiPassUsed) {
			System.out.println("ERROR: Internal error with skiPassUsed()");
			return false;
		}

		// check that remaining uses for the ski pass with this id equals i
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT remainingUses FROM SkiPass WHERE skiPassId = ?";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, skiPassId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int remaining = rs.getInt("remainingUses");
				if (remaining != i) {
					System.out.println("ERROR: remainingUses expected to be " + i + ", but got " + remaining);
					return false;
				}
			} else {
				System.out.println("ERROR: No ski pass found with ID: " + skiPassId);
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException while checking skiPass usage.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Ski pass used successfully and remainingUses verified.");
		return true;
	}

	private static boolean testSkiPassRemUse(Connection dbconn2, int skiPassId, int i) {
		SkiPass newSkiPass = new SkiPass();

		boolean successResetRemUses = newSkiPass.resetRemainingUses(dbconn2, skiPassId, i);

		if(!successResetRemUses) {
			System.out.println("ERROR: Internal error with resetRemainingUses()");
			return false;
		}

		// check that ski pass with this id now has i remaining uses
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT remainingUses FROM SkiPass WHERE skiPassId = ?";
			stmt = dbconn2.prepareStatement(query);
			stmt.setInt(1, skiPassId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				int remaining = rs.getInt("remainingUses");
				if (remaining != i) {
					System.out.println("ERROR: remainingUses expected to be " + i + ", but got " + remaining);
					return false;
				}
			} else {
				System.out.println("ERROR: No ski pass found with ID: " + skiPassId);
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException while checking skiPass remainingUses.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Ski pass remaining uses reset successfully to " + i + ".");
		return true;
	}

	private static boolean testDelSkiPass(Connection dbconn2, int skiPassId) {
		SkiPass newSkiPass = new SkiPass();

		boolean successDeleteSkiPass = newSkiPass.deleteSkiPass(dbconn2, skiPassId);

		if(!successDeleteSkiPass) {
			System.out.println("ERROR: Internal error with deleteSkiPass()");
			return false;
		}

		// check that there are no entries with this skiPassId any longer in SkiPass, SkiPassXactDetails
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Check that there is no entry with this skiPassId in the SkiPass table
			String checkSkiPassQuery = "SELECT COUNT(*) FROM SkiPass WHERE skiPassId = ?";
			stmt = dbconn2.prepareStatement(checkSkiPassQuery);
			stmt.setInt(1, skiPassId);
			rs = stmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				System.out.println("ERROR: Ski pass still exists in SkiPass table.");
				return false;
			}

			// Check that there is no entry with this skiPassId in the SkiPassXactDetails table
			String checkXactDetailsQuery = "SELECT COUNT(*) FROM SkiPassXactDetails WHERE skiPassId = ?";
			stmt = dbconn2.prepareStatement(checkXactDetailsQuery);
			stmt.setInt(1, skiPassId);
			rs = stmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				System.out.println("ERROR: Ski pass still exists in SkiPassXactDetails table.");
				return false;
			}

		} catch (SQLException e) {
			System.err.println("*** SQLException during SkiPass deletion check.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Ski pass deleted successfully and verified in both tables.");
		return true;
	}

	private static boolean memberTests(Connection dbconn) {
		String memName = "Test Name";
		String memPhone = "111-1112";
		String memPhone2 = "222-2221";
		String memEmail = "test@test.edu";
		String memEmail2 = "sample@gmail.com";
		Date memDob = java.sql.Date.valueOf("1999-09-05");
		String emContName = "Emerg Name";
		String emContName2 = "Emerg Name II";
		String emContPhone = "911-9119";
		String emContPhone2 = "114-4411";

		boolean addMembRes = addMember(dbconn, memName, memPhone, memEmail, memDob, emContName, emContPhone);
		if(!addMembRes) {
			return false;
		}

		int newMemId = checkMembExists(dbconn, memName, memPhone, memEmail, memDob, emContName, emContPhone);
		if(newMemId < 0) {
			return false;
		}

		boolean memContactUpdateRes = testUpdateMemberPhoneEmail(dbconn, newMemId, memPhone2, memEmail2);
		if(!memContactUpdateRes) {
			return false;
		}

		boolean emContUpdateRes = testUpdateEmerContact(dbconn, newMemId, emContName2, emContPhone2);
		if(!emContUpdateRes) {
			return false;
		}

		boolean delMembRes = testDeleteMember(dbconn, newMemId);
		if(!delMembRes) {
			return false;
		}

		boolean memberQueryRes = testMemberQuery(dbconn, newMemId);
		if(!memberQueryRes) {
			return false;
		}

		return true;
	}

	private static boolean addMember(Connection dbconn, String memberName, String phoneNumber,
							  String email, Date dateOfBirth,
							  String emergContName, String emergContPhone) {
		Member newMember = new Member();

		try {
			boolean successNewMember = newMember.insertMember(dbconn, memberName, phoneNumber, email, dateOfBirth,
														  emergContName, emergContPhone);

			if(!successNewMember) {
				System.out.println("ERROR: Internal error with insertMember()");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("ERROR: SQL error with insertMember()");
			return false;
		}

		System.out.println("Successfully added a member");
		return true;
	}

	public static int checkMembExists(Connection dbconn, String memberName, String phoneNumber,
		String email, Date dateOfBirth, String emergContName, String emergContPhone) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int memberId = -1;

		try {
			String memberQuery = "SELECT memberId FROM MemberAccount " +
                             "WHERE name = ? AND phoneNumber = ? AND email = ? AND dateOfBirth = ? " +
                             "AND ROWNUM = 1 ORDER BY memberId DESC";

			stmt = dbconn.prepareStatement(memberQuery);
			stmt.setString(1, memberName);
			stmt.setString(2, phoneNumber);
			stmt.setString(3, email);
			stmt.setDate(4, dateOfBirth);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				System.out.println("ERROR: No matching member found.");
				return -1;
			}

			memberId = rs.getInt("memberId");

			rs.close();
			stmt.close();

			// Verify at least one matching EmergencyContact
			String emergencyQuery = "SELECT COUNT(*) AS count FROM EmergencyContact " +
                                "WHERE name = ? AND phoneNumber = ?";
			stmt = dbconn.prepareStatement(emergencyQuery);
			stmt.setString(1, emergContName);
			stmt.setString(2, emergContPhone);
			rs = stmt.executeQuery();

			if (!rs.next() || rs.getInt("count") < 1) {
				System.out.println("ERROR: Emergency contact not found.");
				return -1;
			}
		} catch (SQLException e) {
	
			System.err.println("*** SQLException:  "
				+ "SQL Error in finding matching member.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return -1;

		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Error closing the query resources.");
				return -1;
			}
		}

		System.out.println("Success: Member and Emergency Contact exist!");
		return memberId;
	}

	public static boolean testUpdateMemberPhoneEmail(Connection dbconn, int memberId, String newPhoneNumber, String newEmail) {
		Member newMember = new Member();

		boolean successUpdatePhone = newMember.updatePhoneNumber(dbconn, memberId, newPhoneNumber);

		if(!successUpdatePhone) {
			System.out.println("ERROR: Internal error with updatePhoneNumber()");
			return false;
		}

		boolean successUpdateEmail = newMember.updateEmail(dbconn, memberId, newEmail);

		if(!successUpdateEmail) {
			System.out.println("ERROR: Internal error with updateEmail()");
			return false;
		}

		// Add sql to find entry with memberId and ensure updated phone number and email
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String verifyQuery = "SELECT phoneNumber, email FROM MemberAccount WHERE memberId = ?";
			stmt = dbconn.prepareStatement(verifyQuery);
			stmt.setInt(1, memberId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String dbPhone = rs.getString("phoneNumber");
				String dbEmail = rs.getString("email");

				if (!newPhoneNumber.equals(dbPhone) || !newEmail.equals(dbEmail)) {
					System.out.println("ERROR: Phone or email not correctly updated in DB.");
					return false;
				}
			} else {
				System.out.println("ERROR: Member not found for verification.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during verification.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Successfully updated member phone and email.");
		return true;
	}

	private static boolean testUpdateEmerContact(Connection dbconn2, int newMemId, String emContName2,
			String emContPhone2) {
		
		Member newMember = new Member();

		boolean successUpdateEmCont = newMember.updateEmergencyContact(dbconn2, newMemId, emContName2, emContPhone2);

		if(!successUpdateEmCont) {
			System.out.println("ERROR: Internal error with updateEmergencyContact()");
			return false;
		}

		// Add code to ensure the member's emergency contact was correctly updated
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String verifyQuery =
				"SELECT ec.name, ec.phoneNumber " +
				"FROM MemberAccount ma " +
				"JOIN EmergencyContact ec ON ma.emergencyContactId = ec.emergencyContactId " +
				"WHERE ma.memberId = ?";

			stmt = dbconn2.prepareStatement(verifyQuery);
			stmt.setInt(1, newMemId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String dbName = rs.getString("name");
				String dbPhone = rs.getString("phoneNumber");

				if (!emContName2.equals(dbName) || !emContPhone2.equals(dbPhone)) {
					System.out.println("ERROR: Emergency contact info not correctly updated in DB.");
					return false;
				}
			} else {
				System.out.println("ERROR: No emergency contact found for this member.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during emergency contact verification.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}
		
		System.out.println("Successfully updated emergency contact.");
		return true;
	}

	private static boolean testDeleteMember(Connection dbconn2, int newMemId) {
		Member newMember = new Member();

		try {
			boolean successDelMember = newMember.deleteMember(dbconn2, newMemId);

			if(!successDelMember) {
				System.out.println("ERROR: Internal error with deleteMember()");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("ERROR: SQL error with deleteMember()");
			return false;
		}

		// Add code to check that member with this ID no longer exists
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String verifyQuery = "SELECT memberId FROM MemberAccount WHERE memberId = ?";
			stmt = dbconn2.prepareStatement(verifyQuery);
			stmt.setInt(1, newMemId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println("ERROR: Member record still exists after deletion.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException during delete verification.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing query resources.");
				return false;
			}
		}

		System.out.println("Successfully deleted member and verified.");
		return true;
	}

	private static boolean testMemberQuery(Connection dbconn2, int newMemId) {
		Member newMember = new Member();

		boolean successMemberQuery = newMember.memberQuery(dbconn2, newMemId);

		if(!successMemberQuery) {
			System.out.println("ERROR: Internal error with memberQuery()");
			return false;
		}

		return true;
	}

}
