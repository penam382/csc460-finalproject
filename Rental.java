public class Rental {
	public Rental() {

	}

	private int createTransactionId(Connection dbconn) {
		return 1;
	}

	private int createRentalXactDetailsId(Connection dbconn) {
		return 1;
	}

	private int createItemInRentalId(Connection dbconn) {
		return 1;
	}

	private boolean createItemInRental(Connection dbconn, int itemId, int RentalXactDetailsId){
		return true;
	}

	public boolean createRentalXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType,
	String dateTime, int amount, int skiPassId, int itemId) {
		// Create Transaction Id

		// verify resortPropertyId, memberId

		// populate transaction content: transactionType, transactionDateTime, amount

		// Create RentalXactDetailsId

		// Validate skiPassId

		// fill content for RentalXactDetails: TransactionId, skiPassId, returnStatus, dateReturned

		// CreateItemInRental
			// check itemId
			// call createItemInRental

		return true;
	}

	public boolean setRentalXactReturned(Connection dbconn, int rentalXactDetailsId) {
		return true;
	}

	public boolean deleteRentalXact(Connection dbconn, int rentalXactDetailsId) {
		return true;
	}
}