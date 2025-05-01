import java.sql.*;

public class Equipment {
	public Equipment() {

	}

	private int createEquipmentId(Connection dbconn) {
		return 1;
	}

	public boolean addNewEquipment(Connection dbconn, int resortPropertyId, String itemType, String itemSize) {
		// Create unique equipment ID

		// verify resort property id, quit if invalid

		// Add content: itemType, itemSize, archived = 0

		return true;
	}

	public boolean changeEquipmentType(Connection dbconn, int itemId, String newType) {
		// save current version to RentalChangeLog

		// make updates

		return true;
	}

	public boolean changeEquipmentSize(Connection dbconn, int itemId, String newSize) {
		// save current version to RentalChangeLog

		// make updates
	}

	public boolean deleteEquipment(Connection dbconn, int itemId) {
		// Check item not currently rented out or reserved

		// Mark as archived
	}
}