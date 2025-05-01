public class Lesson {
	public Lesson() {

	}

	private int createNewTransactionId(Connection dbconn) {
		return 1;
	}

	private int createLessonXactDetailsId(Connection dbconn) {

		return 1;
	}

	private int createLessonUsageId(Connection dbconn) {
		return 1;
	}

	private int createLessonSessionId(Connection dbconn) {
		return 1;
	}

	private int createLessonTypeId(Connection dbconn) {
		return 1;
	}

	public boolean createLessonXact(Connection dbconn, int resortPropertyId, int memberId, String transactionType,
	String dateTime, int amount, int numSessions) {
		// Create Transaction Id

		// verify resortPropertyId, memberId

		// populate transaction content: resortPropertyId, memberId, transactionType, transactionDateTime, amount

		// Create LessonXactDetailsId

		// Populate LessonXactDetails content: transactionId, numSessions = 0, remainingSessions

		return true;
	}

	public boolean useLesson(Connection dbconn, int lessonXactDetailsId, int lessonSessionId, String usedDate) {
		// Create lesson usage id

		// Validate: lessonXactDetailsId, lessonSessionId

		// Populate LessonUsage: lessonXactDetailsId, lessonSessionId, usedDate, attended

		// decrement remaining sessions, increment numSessions
	}

	public boolean deleteLessonXact(Connection, int lessonXactDetailsId) {
		// Ensure no sessions have been used

		// delete lessonXact, all associated records
	}
}