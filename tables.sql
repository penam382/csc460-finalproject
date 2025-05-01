-- =================================
-- EMPLOYEE RELATED TABLES
-- =================================

CREATE TABLE EmployeeRole (
    employeeRoleId INT,
    employeeRoleName VARCHAR2(100) NOT NULL,
    hourlyWage NUMBER(10, 2) NOT NULL,
    PRIMARY KEY (employeeRoleId)
); 

CREATE TABLE Employee (
    employeeId INT NOT NULL,
    employeeRoleId INT NOT NULL,
    employeeAssignmentId INT,
    employeeName VARCHAR2(100) NOT NULL,
    age INT,
    gender VARCHAR2(10),
    dateOfBirth DATE NOT NULL,
    raceEthnicity VARCHAR2(20),
    PRIMARY KEY (employeeId),
    FOREIGN KEY (employeeRoleId) REFERENCES EmployeeRole(employeeRoleId)
);

CREATE TABLE EmployeeAssignment (
    employeeAssignmentId INT,
    employeeId INT NOT NULL,
    resortPropertyId INT NOT NULL,
    employeeRoleId INT NOT NULL,
    assignmentStartDate DATE NOT NULL,
    PRIMARY KEY (employeeAssignmentId),
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId),
    FOREIGN KEY (employeeRoleId) REFERENCES EmployeeRole(employeeRoleId)
);

CREATE TABLE EmployeeTimesheet (
    employeeTimesheetId INT,
    employeeId INT NOT NULL,
    employeeRoleId INT NOT NULL,
    monthYear DATE NOT NULL,
    hoursWorked NUMBER(6,2) NOT NULL,
    PRIMARY KEY (employeeTimesheetId),
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId),
    FOREIGN KEY (employeeRoleId) REFERENCES EmployeeRole(employeeRoleId)
);

CREATE TABLE PayrollTransaction (
    payrollTransactionId INT,
    employeeTimesheetId INT NOT NULL,
    paymentDate DATE NOT NULL,
    amountPayed NUMBER(10,2) NOT NULL,
    PRIMARY KEY (payrollTransactionId),
    FOREIGN KEY (employeeTimesheetId) REFERENCES EmployeeTimesheet(employeeTimesheetId)
);



-- =================================
-- RESORT SPECIFIC TABLES
-- =================================

CREATE TABLE ResortProperty (
    resortPropertyId INT,
    propertyType VARCHAR2(50) NOT NULL,
    propertyName VARCHAR2(100) NOT NULL,
    physicalLocation VARCHAR2(200) NOT NULL,
    PRIMARY KEY (resortPropertyId),
    CONSTRAINT chk_property_type CHECK (propertyType IN ('LODGE', 'GIFT_SHOP', 'RENTAL_CENTER', 'VISITOR_CENTER', 'SKI_SCHOOL', 'FREE_PARKING', 'PAID_PARKING'))
);

CREATE TABLE Shuttle (
    shuttleId INT,
    resortPropertyId INT NOT NULL,
    licensePlate VARCHAR2(20) NOT NULL,
    capacity INT NOT NULL,
    PRIMARY KEY (shuttleId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

CREATE TABLE ShuttleUsage (
    shuttleUsageId INT,
    shuttleId INT NOT NULL,
    fromLocation VARCHAR2(100) NOT NULL,
    toLocation VARCHAR2(100) NOT NULL,
    dateTime TIMESTAMP NOT NULL,
    numberPassengers INT NOT NULL,
    PRIMARY KEY (shuttleUsageId),
    FOREIGN KEY (shuttleId) REFERENCES Shuttle(shuttleId)
);


-- =================================
-- TRANSACTION TABLES
-- ================================= 

CREATE TABLE Transactions (                 -- <------- using transaction(s) with an s because Transaction is a reserved word in SQL
    transactionId INT,
    resortPropertyId INT NOT NULL,
    memberId INT NOT NULL,
    transactionType VARCHAR2(50) NOT NULL,
    transactionDateTime TIMESTAMP NOT NULL,
    amount NUMBER(10,2) NOT NULL,
    PRIMARY KEY (transactionId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId),
    FOREIGN KEY (memberId) REFERENCES MemberAccount(memberId)
);

CREATE TABLE RentalXactDetails (
    rentalXactDetailsId INT,
    transactionId INT NOT NULL,
    skiPassId INT NOT NULL,
    returnStatus NUMBER(1) NOT NULL, -- 0 if not returned, 1 if returned
    dateReturned DATE,
    PRIMARY KEY (rentalXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId)
);

CREATE TABLE LodgeXactDetails (
    lodgeXactDetailsId INT,
    transactionId INT NOT NULL,
    stayStartDate DATE NOT NULL,
    stayEndDate DATE NOT NULL,
    roomType VARCHAR2(50) NOT NULL,
    PRIMARY KEY (lodgeXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId)
);

CREATE TABLE LessonXactDetails (
    lessonXactDetailsId INT,
    transactionId INT NOT NULL,
    numSessions INT NOT NULL,
    remainingSessions INT NOT NULL,
    PRIMARY KEY (lessonXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId)
);

CREATE TABLE SkiPassXactDetails (
    skiPassXactDetailsId INT,
    transactionId INT NOT NULL,
    skiPassId INT NOT NULL,
    PRIMARY KEY (skiPassXactDetailsId),
    FOREIGN KEY (transactionId) REFERENCES Transactions(transactionId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId)
);


-- =================================
-- MEMBER TABLES
-- =================================

CREATE TABLE MemberAccount (
    memberId INT,
    name VARCHAR2(100) NOT NULL,
    phoneNumber VARCHAR2(20),
    email VARCHAR2(100),
    dateOfBirth DATE,
    emergencyContactId INT,
    PRIMARY KEY (memberId),
    FOREIGN KEY (emergencyContactId) REFERENCES EmergencyContact(emergencyContactId)
);

CREATE TABLE EmergencyContact (
    emergencyContactId INT,
    name VARCHAR2(100) NOT NULL,
    phoneNumber VARCHAR2(20) NOT NULL,
    PRIMARY KEY (emergencyContactId)
);




-- =================================
-- SKI PASSES (includes lift) TABLES
-- =================================

CREATE TABLE SkiPass (
    skiPassId INT,
    numberOfUses INT NOT NULL,
    remainingUses INT NOT NULL,
    expirationDate DATE NOT NULL,
    PRIMARY KEY (skiPassId)
);

CREATE TABLE SkiPassArchive (
    skiPassArchiveId INT,
    memberId INT,
    numberOfUses INT NOT NULL,
    PRIMARY KEY (skiPassId),
    FOREIGN KEY (nemberId) REFERENCES MemberAccount(memberId)
);

CREATE TABLE LiftUsage (
    liftUsageId INT,
    liftId INT NOT NULL,
    skiPassId INT NOT NULL,
    memberId INT NOT NULL,
    usageDateTime TIMESTAMP NOT NULL,
    PRIMARY KEY (liftUsageId),
    FOREIGN KEY (liftId) REFERENCES Lift(liftId),
    FOREIGN KEY (skiPassId) REFERENCES SkiPass(skiPassId),
    FOREIGN KEY (memberId) REFERENCES MemberAccount(memberId)
);

CREATE TABLE Lift (
    liftId INT,
    liftName VARCHAR2(100) NOT NULL,
    abilityLevel VARCHAR2(20) NOT NULL, -- beginner, intermediate, expert
    openTime VARCHAR2(10) NOT NULL,
    closeTime VARCHAR2(10) NOT NULL,
    isOpen NUMBER(1) NOT NULL, -- 0 for closed, 1 for open
    PRIMARY KEY (liftId)
);

CREATE TABLE LiftTrailAccess (
    liftTrailAccessId INT,
    liftId INT NOT NULL,
    trailId INT NOT NULL,
    PRIMARY KEY (liftTrailAccessId),
    FOREIGN KEY (liftId) REFERENCES Lift(liftId),
    FOREIGN KEY (trailId) REFERENCES Trail(trailId)
);

CREATE TABLE Trail (
    trailId INT,
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
    itemId INT,
    resortPropertyId INT NOT NULL,
    itemType VARCHAR2(50) NOT NULL, -- ski, snowboard, poles, boots, etc.
    itemSize VARCHAR2(30) NOT NULL, -- varies by item type
    archived NUMBER(1) NOT NULL, -- 0 not archived, 1 archived
    PRIMARY KEY (itemId),
    FOREIGN KEY (resortPropertyId) REFERENCES ResortProperty(resortPropertyId)
);

CREATE TABLE ItemInRental (
    itemInRentalId INT,
    itemId INT NOT NULL,
    rentalXactDetailsId INT NOT NULL,
    PRIMARY KEY (itemInRentalId),
    FOREIGN KEY (itemId) REFERENCES RentalInventory(itemId),
    FOREIGN KEY (rentalXactDetailsId) REFERENCES RentalXactDetails(rentalXactDetailsId)
);

CREATE TABLE RentalChangeLog (
    rentalChangeLogId INT,
    itemId INT NOT NULL,
    itemType VARCHAR2(50) NOT NULL, -- ski, snowboard, poles, boots, etc.
    itemSize VARCHAR2(30) NOT NULL, -- varies by item type
    changeDate DATE NOT NULL
    PRIMARY KEY (rentalChangeLogId),
    FOREIGN KEY (itemId) REFERENCES RentalInventory(itemId)
);

-- =================================
-- LESSON AND INSTRUCTOR TABLES
-- =================================

CREATE TABLE LessonInstructor (
    instructorId INT,
    employeeId INT NOT NULL,
    instructorLevel VARCHAR2(20) NOT NULL, -- Level I, II, III
    PRIMARY KEY (instructorId),
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)
);

CREATE TABLE LessonType (
    lessonTypeId INT,
    lessonTypeName VARCHAR2(100) NOT NULL,
    isPrivate NUMBER(1) NOT NULL, -- 0 for group, 1 for private
    PRIMARY KEY (lessonTypeId)
);

CREATE TABLE LessonSession (
    lessonSessionId INT,
    instructorId INT NOT NULL,
    lessonTypeId INT NOT NULL,
    sessionDate DATE NOT NULL,
    startTime VARCHAR2(10) NOT NULL,
    endTime VARCHAR2(10) NOT NULL,
    PRIMARY KEY (lessonSessionId),
    FOREIGN KEY (instructorId) REFERENCES LessonInstructor(instructorId),
    FOREIGN KEY (lessonTypeId) REFERENCES LessonType(lessonTypeId)
);

CREATE TABLE LessonUsage (
    lessonUsageId INT,
    lessonXactDetailsId INT NOT NULL,
    lessonSessionId INT NOT NULL,
    usedDate DATE NOT NULL,
    attended NUMBER(1) NOT NULL, -- 0 for no, 1 for yes
    PRIMARY KEY (lessonUsageId),
    FOREIGN KEY (lessonXactDetailsId) REFERENCES LessonXactDetails(lessonXactDetailsId),
    FOREIGN KEY (lessonSessionId) REFERENCES LessonSession(lessonSessionId)
);
