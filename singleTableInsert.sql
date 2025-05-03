CREATE TABLE LessonUsage (
    lessonUsageId NUMBER(*, 0),
    lessonXactDetailsId NUMBER(*, 0) NOT NULL,
    lessonSessionId NUMBER(*, 0) NOT NULL,
    usedDate DATE NOT NULL,
    attended NUMBER(1) NOT NULL, -- 0 for no, 1 for yes
	remSessions NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (lessonUsageId),
    FOREIGN KEY (lessonXactDetailsId) REFERENCES LessonXactDetails(lessonXactDetailsId),
    FOREIGN KEY (lessonSessionId) REFERENCES LessonSession(lessonSessionId)
);