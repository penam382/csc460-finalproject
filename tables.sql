-- =================================
-- EMPLOYEE RELATED TABLES
-- =================================

CREATE TABLE EmployeeRole (
    employeeRoleId NUMBER(*, 0),
    employeeRoleName VARCHAR2(100) NOT NULL,
    hourlyWage NUMBER(10, 2) NOT NULL,
    PRIMARY KEY (employeeRoleId)
); 

CREATE TABLE Employee (
    employeeId NUMBER(*, 0) NOT NULL,
    employeeRoleId NUMBER(*, 0) NOT NULL,
    employeeAssignmentId NUMBER(*, 0),
    employeeName VARCHAR2(100) NOT NULL,
    age NUMBER(3),
    gender VARCHAR2(10),
    dateOfBirth DATE NOT NULL,
    raceEthnicity VARCHAR2(20),
    PRIMARY KEY (employeeId),
    FOREIGN KEY (employeeRoleId) REFERENCES EmployeeRole(employeeRoleId)
);

CREATE TABLE EmployeeAssignment (
    employeeAssignmentId NUMBER(*, 0),
    resortPropertyId NUMBER(*, 0) NOT NULL,
    assignmentStartDate DATE NOT NULL,
    PRIMARY KEY (employeeAssignmentId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

CREATE TABLE EmployeeTimesheet (
    employeeTimesheetId NUMBER(*, 0),
    employeeId NUMBER(*, 0) NOT NULL,
    employeeRoleId NUMBER(*, 0) NOT NULL,
    monthYear DATE NOT NULL,
    hoursWorked NUMBER(6,2) NOT NULL,
    PRIMARY KEY (employeeTimesheetId),
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId),
    FOREIGN KEY (employeeRoleId) REFERENCES EmployeeRole(employeeRoleId)
);

CREATE TABLE PayrollTransaction (
    payrollTransactionId NUMBER(*, 0),
    employeeTimesheetId NUMBER(*, 0) NOT NULL,
    paymentDate DATE NOT NULL,
    amountPayed NUMBER(10,2) NOT NULL,
    PRIMARY KEY (payrollTransactionId),
    FOREIGN KEY (employeeTimesheetId) REFERENCES EmployeeTimesheet(employeeTimesheetId)
);



-- =================================
-- RESORT SPECIFIC TABLES
-- =================================

CREATE TABLE ResortProperty (
    resortPropertyId NUMBER(*, 0),
    propertyType VARCHAR2(50) NOT NULL,
    propertyName VARCHAR2(100) NOT NULL,
    physicalLocation VARCHAR2(200) NOT NULL,
    PRIMARY KEY (resortPropertyId),
    CONSTRAINT chk_property_type CHECK (propertyType IN ('LODGE', 'GIFT_SHOP', 'RENTAL_CENTER', 'VISITOR_CENTER', 'SKI_SCHOOL', 'FREE_PARKING', 'PAID_PARKING'))
);

CREATE TABLE Shuttle (
    shuttleId NUMBER(*, 0),
    resortPropertyId NUMBER(*, 0) NOT NULL,
    licensePlate VARCHAR2(20) NOT NULL,
    capacity NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (shuttleId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

CREATE TABLE ShuttleUsage (
    shuttleUsageId NUMBER(*, 0),
    shuttleId NUMBER(*, 0) NOT NULL,
    fromLocation VARCHAR2(100) NOT NULL,
    toLocation VARCHAR2(100) NOT NULL,
    dateTime TIMESTAMP NOT NULL,
    numberPassengers NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (shuttleUsageId),
    FOREIGN KEY (shuttleId) REFERENCES Shuttle(shuttleId)
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
-- MEMBER TABLES
-- =================================
CREATE TABLE EmergencyContact (
    emergencyContactId NUMBER(*, 0),
    name VARCHAR2(100) NOT NULL,
    phoneNumber VARCHAR2(20) NOT NULL,
    PRIMARY KEY (emergencyContactId)
);

CREATE TABLE MemberAccount (
    memberId NUMBER(*, 0),
    name VARCHAR2(100) NOT NULL,
    phoneNumber VARCHAR2(20),
    email VARCHAR2(100),
    dateOfBirth DATE,
    emergencyContactId NUMBER(*, 0),
    PRIMARY KEY (memberId),
    FOREIGN KEY (emergencyContactId) REFERENCES EmergencyContact(emergencyContactId)
);

-- =================================
-- SKI PASSES (includes lift) TABLES
-- =================================

CREATE TABLE SkiPass (
    skiPassId NUMBER(*, 0),
    numberOfUses INT NOT NULL,
    remainingUses INT NOT NULL,
    expirationDate DATE NOT NULL,
    PRIMARY KEY (skiPassId)
);

CREATE TABLE SkiPassArchive (
    skiPassArchiveId NUMBER(*, 0),
    memberId NUMBER(*, 0),
    numberOfUses NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (skiPassArchiveId),
    FOREIGN KEY (memberId) REFERENCES MemberAccount(memberId)
);

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

CREATE TABLE Lift (
    liftId NUMBER(*, 0),
    liftName VARCHAR2(100) NOT NULL,
    abilityLevel VARCHAR2(20) NOT NULL, -- beginner, intermediate, expert
    openTime VARCHAR2(10) NOT NULL,
    closeTime VARCHAR2(10) NOT NULL,
    isOpen NUMBER(1) NOT NULL, -- 0 for closed, 1 for open
    PRIMARY KEY (liftId)
);

CREATE TABLE LiftTrailAccess (
    liftTrailAccessId NUMBER(*, 0),
    liftId NUMBER(*, 0) NOT NULL,
    trailId NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (liftTrailAccessId),
    FOREIGN KEY (liftId) REFERENCES Lift(liftId),
    FOREIGN KEY (trailId) REFERENCES Trail(trailId)
);

CREATE TABLE Trail (
    trailId NUMBER(*, 0),
    trailName VARCHAR2(100) NOT NULL,
    startLoc VARCHAR2(100) NOT NULL,
    endLoc VARCHAR2(100) NOT NULL,
    isOpen NUMBER(1) NOT NULL, -- 0 for closed, 1 for open
    difficulty VARCHAR2(20) NOT NULL, -- beginner, intermediate, expert
    category VARCHAR2(50) NOT NULL, -- groomed, park, moguls, glade
    PRIMARY KEY (trailId)
);


-- =================================
-- RENTAL TABLES
-- =================================

CREATE TABLE RentalInventory (
    itemId NUMBER(*, 0),
    resortPropertyId NUMBER(*, 0) NOT NULL,
    itemType VARCHAR2(50) NOT NULL, -- ski, snowboard, poles, boots, etc.
    itemSize VARCHAR2(30) NOT NULL, -- varies by item type
    archived NUMBER(1) NOT NULL, -- 0 not archived, 1 archived
    PRIMARY KEY (itemId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

CREATE TABLE ItemInRental (
    itemInRentalId NUMBER(*, 0),
    itemId NUMBER(*, 0) NOT NULL,
    rentalXactDetailsId NUMBER(*, 0) NOT NULL,
    PRIMARY KEY (itemInRentalId),
    FOREIGN KEY (itemId) REFERENCES RentalInventory(itemId),
    FOREIGN KEY (rentalXactDetailsId) REFERENCES RentalXactDetails(rentalXactDetailsId)
);

CREATE TABLE RentalChangeLog (
    rentalChangeLogId NUMBER(*, 0),
    itemId NUMBER(*, 0) NOT NULL,
    itemType VARCHAR2(50) NOT NULL, -- ski, snowboard, poles, boots, etc.
    itemSize VARCHAR2(30) NOT NULL, -- varies by item type
    changeDate DATE NOT NULL,
    PRIMARY KEY (rentalChangeLogId),
    FOREIGN KEY (itemId) REFERENCES RentalInventory(itemId)
);

-- =================================
-- LESSON AND INSTRUCTOR TABLES
-- =================================

CREATE TABLE LessonInstructor (
    instructorId NUMBER(*, 0),
    employeeId NUMBER(*, 0) NOT NULL,
    instructorLevel VARCHAR2(20) NOT NULL, -- Level I, II, III
    PRIMARY KEY (instructorId),
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)
);

CREATE TABLE LessonType (
    lessonTypeId NUMBER(*, 0),
    lessonTypeName VARCHAR2(100) NOT NULL,
    isPrivate NUMBER(1) NOT NULL, -- 0 for group, 1 for private
    PRIMARY KEY (lessonTypeId)
);

CREATE TABLE LessonSession (
    lessonSessionId NUMBER(*, 0),
    instructorId NUMBER(*, 0) NOT NULL,
    lessonTypeId NUMBER(*, 0) NOT NULL,
    sessionDate DATE NOT NULL,
    startTime VARCHAR2(10) NOT NULL,
    endTime VARCHAR2(10) NOT NULL,
    PRIMARY KEY (lessonSessionId),
    FOREIGN KEY (instructorId) REFERENCES LessonInstructor(instructorId),
    FOREIGN KEY (lessonTypeId) REFERENCES LessonType(lessonTypeId)
);

CREATE TABLE LessonUsage (
    lessonUsageId NUMBER(*, 0),
    lessonXactDetailsId NUMBER(*, 0) NOT NULL,
    lessonSessionId NUMBER(*, 0) NOT NULL,
    usedDate DATE NOT NULL,
    attended NUMBER(1) NOT NULL, -- 0 for no, 1 for yes
    PRIMARY KEY (lessonUsageId),
    FOREIGN KEY (lessonXactDetailsId) REFERENCES LessonXactDetails(lessonXactDetailsId),
    FOREIGN KEY (lessonSessionId) REFERENCES LessonSession(lessonSessionId)
);
