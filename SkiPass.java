public class SkiPass {

	public SkiPass() {
		
	}

	public int createSkiPassId(Connection dbconn) {

	}

	public boolean createSkiPass(int memberId){
		// Create Unique ID

		// Somehow associate with memberId

		// Prepare content: numberOfUses, remainingUses, expirationDate

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
	}


}