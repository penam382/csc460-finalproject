public class SkiPass {

	public SkiPass() {

	}

	public int createNewXactId(Connection dbconn) {
		return 1;
	}

	public int createNewSkiPassXactDetailsId(Connection dbconn) {

	}

	public int createSkiPassId(Connection dbconn) {
		return 1;
	}

	public boolean newSkiPass(Connection dbconn, int memberId, int resortPropertyId, String xactType, String xactDateTime,
	int amount, int remainingUses, String expirationDate) {
		// createNewXactId

		// create new Transaction, content: xactId, resortPropertyId, memberId, type, dateTime, amount

		// createNewSkiPassId

		// create new SkiPass, content: skiPassId, numUses = 0, remainingUses, expirDate

		// createNewSkiPassXactDetailsId

		// create new skiPassXactDetails, content: id, transactionId, skiPassId

		return true;
	}

	public boolean skiPassUsed(Connection dbconn, String skiPassId) {
		// deduct 1 from ski pass remaining uses, incrememnt number of uses

		return true;
	}

	public boolean resetRemainingUses(Connection dbconn, String skiPassId, int newRemUses) {
		return true;
	}

	public boolean deleteSkiPass(Connection dbconn, String skiPassId) {
		// Check expired and no remaining uses OR confirmed refund?

		// Move to SkiPassArchive

		// Remove all related records
		return true;
	}

	public String skiPassQuery(Connection dbconn, int skiPassId){
		// For a given ski pass, list all lift rides and equpment rentals associated with it, along with
		//	timestamps and return status

		
	}

}