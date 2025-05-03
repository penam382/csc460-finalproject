-- =================================
-- EMPLOYEE RELATED TABLES
-- =================================

CREATE TABLE EmployeeAssignment (
    employeeAssignmentId NUMBER(*, 0),
    resortPropertyId NUMBER(*, 0) NOT NULL,
    assignmentStartDate DATE NOT NULL,
    PRIMARY KEY (employeeAssignmentId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

-- =================================
-- TRANSACTION TABLES
-- ================================= 

CREATE TABLE Transactions (                 -- <------- using transaction(s) with an s because Transaction is a reserved word in SQL
    transactionId NUMBER(*, 0),
    resortPropertyId NUMBER(*, 0) NOT NULL,
    memberId NUMBER(*, 0) NOT NULL,
    transactionType VARCHAR2(50) NOT NULL,
    transactionDateTime TIMESTAMP NOT NULL,
    amount NUMBER(10,2) NOT NULL,
    PRIMARY KEY (transactionId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId),
    FOREIGN KEY (memberId) REFERENCES MemberAccount(memberId)
);

CREATE TABLE RentalXactDetails (
    rentalXactDetailsId NUMBER(*, 0),
    transactionId NUMBER(*, 0) NOT NULL,
    skiPassId NUMBER(*, 0) NOT NULL,
    returnStatus NUMBER(1) NOT NULL, -- 0 if not returned, 1 if returned
    dateReturned DATE,
    PRIMARY KEY (rentalXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId)
);

CREATE TABLE LodgeXactDetails (
    lodgeXactDetailsId NUMBER(*, 0),
    transactionId NUMBER(*, 0) NOT NULL,
    stayStartDate DATE NOT NULL,
    stayEndDate DATE NOT NULL,
    roomType VARCHAR2(50) NOT NULL,
    PRIMARY KEY (lodgeXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId)
);

CREATE TABLE LessonXactDetails (
    lessonXactDetailsId NUMBER(*, 0),
    transactionId NUMBER(*, 0) NOT NULL,
    numSessions NUMBER(*, 0) NOT NULL,
    remainingSessions NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (lessonXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId)
);

CREATE TABLE SkiPassXactDetails (
    skiPassXactDetailsId NUMBER(*, 0),
    transactionId NUMBER(*, 0) NOT NULL,
    skiPassId NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (skiPassXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId)
);

-- =================================
-- SKI PASSES (includes lift) TABLES
-- =================================

CREATE TABLE LiftUsage (
    liftUsageId NUMBER(*, 0),
    liftId NUMBER(*, 0) NOT NULL,
    skiPassId NUMBER(*, 0) NOT NULL,
    memberId NUMBER(*, 0) NOT NULL,
    usageDateTime TIMESTAMP NOT NULL,
    PRIMARY KEY (liftUsageId),
    FOREIGN KEY (liftId) REFERENCES Lift(liftId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId),
    FOREIGN KEY (memberId) REFERENCES MemberAccount(memberId)
);

CREATE TABLE LiftTrailAccess (
    liftTrailAccessId NUMBER(*, 0),
    liftId NUMBER(*, 0) NOT NULL,
    trailId NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (liftTrailAccessId),
    FOREIGN KEY (liftId) REFERENCES Lift(liftId),
    FOREIGN KEY (trailId) REFERENCES Trail(trailId)
);

-- =================================
-- RENTAL TABLES
-- =================================

CREATE TABLE ItemInRental (
    itemInRentalId NUMBER(*, 0),
    itemId NUMBER(*, 0) NOT NULL,
    rentalXactDetailsId NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (itemInRentalId),
    FOREIGN KEY (itemId) REFERENCES RentalInventory(itemId),
    FOREIGN KEY (rentalXactDetailsId) REFERENCES RentalXactDetails(rentalXactDetailsId)
);

-- =================================
-- LESSON AND INSTRUCTOR TABLES
-- =================================

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
