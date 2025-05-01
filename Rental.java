public class Rental {
	public Rental() {

	}

	private int createNewTransactionId(Connection dbconn) {
		return 1;
	}

	private int createRentalXactDetailsId(Connection dbconn) {
		return 1;
	}

	private int createItemInRentalId(Connection dbconn) {
		return 1;
	}

	private boolean createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId){
		// Validate itemId, rentalXactDetailsId

		// create itemInRentalId

		// Populate Content: itemId, RentalXactDetialsId

		return true;
	}

	public boolean createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType,
	String dateTime, int amount, int skiPassId, ArrayList<Integer> itemIds) {
		// Create Transaction Id

		// verify resortPropertyId, memberId

		// populate transaction content: resortPropId, memberId, transactionType, transactionDateTime, amount

		// Create RentalXactDetailsId

		// Validate skiPassId

		// fill content for RentalXactDetails: TransactionId, skiPassId, returnStatus = 0, dateReturned = NULL

		// For itemId in itemIds
			// createItemInRental()

		return true;
	}

	public boolean setRentalXactReturned(Connection dbconn, int rentalXactDetailsId, String dateReturned) {

		// Update RentalXactDetails content: ReturnStatus = 1, DateReturned = dateReturned

		return true;
	}

	public boolean deleteRentalXact(Connection dbconn, int rentalXactDetailsId) {
		return true;
	}
}