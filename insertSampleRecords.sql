--================================
-- ResortProperty Creation
--================================

INSERT INTO ResortProperty (resortPropertyId, propertyType, propertyName, physicalLocation) 
	VALUES (1, 'VISITOR_CENTER', 'Alpine Guest Center', '123 Mountain Rd. Unit 1');

INSERT INTO ResortProperty (resortPropertyId, propertyType, propertyName, physicalLocation) 
	VALUES (2, 'RENTAL_CENTER', 'Rocky Rentals', '110 Peak St.');

INSERT INTO ResortProperty (resortPropertyId, propertyType, propertyName, physicalLocation) 
	VALUES (3, 'SKI_SCHOOL', 'Snow Bunny School', '1 Snowman Drive');


-- MEMBER RELATED

--================================
-- EmergencyContact Creation
--================================

INSERT INTO EmergencyContact (emergencyContactId, name, phoneNumber) 
    VALUES (1, 'Jane Doe', '123-4567');

INSERT INTO EmergencyContact (emergencyContactId, name, phoneNumber) 
    VALUES (2, 'Gary Oak', '321-5555');

INSERT INTO EmergencyContact (emergencyContactId, name, phoneNumber) 
    VALUES (3, 'Delia Ketchum', '555-9876');

INSERT INTO EmergencyContact (emergencyContactId, name, phoneNumber) 
    VALUES (4, 'Brock Stone', '999-2222');

INSERT INTO EmergencyContact (emergencyContactId, name, phoneNumber) 
    VALUES (5, 'Misty Waterflower', '222-1111');

--================================
-- MemberAccount Creation
--================================

INSERT INTO MemberAccount (memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) 
    VALUES (1, 'John Smith', '333-6677', 'jsmith@gmail.com', TO_DATE('1990-05-15', 'YYYY-MM-DD'), 1);

INSERT INTO MemberAccount (memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) 
    VALUES (2, 'Ash Ketchum', '555-1234', 'ash@paletttown.com', TO_DATE('1995-07-10', 'YYYY-MM-DD'), 2);

INSERT INTO MemberAccount (memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) 
    VALUES (3, 'Gary Ketchum', '444-5555', 'gary@vermilion.com', TO_DATE('1993-04-12', 'YYYY-MM-DD'), 3);

INSERT INTO MemberAccount (memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) 
    VALUES (4, 'Brock Harrison', '111-7777', 'brock@pewter.com', TO_DATE('1991-08-22', 'YYYY-MM-DD'), 4);

INSERT INTO MemberAccount (memberId, name, phoneNumber, email, dateOfBirth, emergencyContactId) 
    VALUES (5, 'Misty Williams', '222-8888', 'misty@cerulean.com', TO_DATE('1992-03-17', 'YYYY-MM-DD'), 5);



-- TRANSACTION RELATED

--================================
-- Transactions Creation TODO
--================================

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (1, 1, 1, 'SkiPass Purchase', TO_TIMESTAMP('2023-11-01 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 150.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (2, 2, 1, 'Equipment Rental', TO_TIMESTAMP('2023-11-01 14:45:00', 'YYYY-MM-DD HH24:MI:SS'), 60.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (3, 3, 1, 'Lesson Purchase', TO_TIMESTAMP('2023-11-01 14:50:00', 'YYYY-MM-DD HH24:MI:SS'), 100.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (4, 2, 2, 'Equipment Rental', TO_TIMESTAMP('2023-11-01 15:25:00', 'YYYY-MM-DD HH24:MI:SS'), 75.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (5, 3, 2, 'Lesson Purchase', TO_TIMESTAMP('2023-11-01 15:26:00', 'YYYY-MM-DD HH24:MI:SS'), 90.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (6, 1, 2, 'SkiPass Purchase', TO_TIMESTAMP('2023-11-01 15:35:00', 'YYYY-MM-DD HH24:MI:SS'), 150.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (7, 2, 2, 'Equipment Rental', TO_TIMESTAMP('2023-11-02 09:30:00', 'YYYY-MM-DD HH24:MI:SS'), 200.00);

INSERT INTO Transactions (transactionId, resortPropertyId, memberId, transactionType, transactionDateTime, amount) 
    VALUES (8, 2, 2, 'Equipment Rental', TO_TIMESTAMP('2023-11-09 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 100.00);


-- SKIPASS RELATED

--================================
-- SkiPass Creation TODO
--================================

INSERT INTO SkiPass (skiPassId, numberOfUses, remainingUses, expirationDate) 
    VALUES (1, 1, 9, TO_DATE('2024-11-01', 'YYYY-MM-DD'));

INSERT INTO SkiPass (skiPassId, numberOfUses, remainingUses, expirationDate) 
    VALUES (2, 1, 9, TO_DATE('2024-11-01', 'YYYY-MM-DD'));

--================================
-- SkiPassXactDetails TODO
--================================

INSERT INTO SkiPassXactDetails (skiPassXactDetailsId, transactionId, skiPassId) 
    VALUES (1, 1, 1);

INSERT INTO SkiPassXactDetails (skiPassXactDetailsId, transactionId, skiPassId) 
    VALUES (2, 6, 2);



-- RENTAL RELATED

--================================
-- RentalInventory Creation
--================================

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (1, 2, 'Skis', '170cm', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (2, 2, 'Boots', '10', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (3, 2, 'Ski Poles', '120cm', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (4, 2, 'Helmet', 'Medium', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (5, 2, 'Snowboard', '155cm', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (6, 2, 'Skis', '160cm', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (7, 2, 'Boots', '9', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (8, 2, 'Helmet', 'Large', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (9, 2, 'Ski Poles', '110cm', 0);

INSERT INTO RentalInventory (itemId, resortPropertyId, itemType, itemSize, archived) 
    VALUES (10, 2, 'Snowboard', '165cm', 0);


--================================
-- RentalXactDetails Creation TODO
--================================

INSERT INTO RentalXactDetails (rentalXactDetailsId, transactionId, skiPassId, returnStatus, dateReturned) 
    VALUES (1, 2, 1, 0, NULL);

INSERT INTO RentalXactDetails (rentalXactDetailsId, transactionId, skiPassId, returnStatus, dateReturned) 
    VALUES (2, 4, 2, 1, TO_DATE('2023-11-01', 'YYYY-MM-DD'));

INSERT INTO RentalXactDetails (rentalXactDetailsId, transactionId, skiPassId, returnStatus, dateReturned) 
    VALUES (3, 7, 2, 1, TO_DATE('2023-11-02', 'YYYY-MM-DD'));

INSERT INTO RentalXactDetails (rentalXactDetailsId, transactionId, skiPassId, returnStatus, dateReturned) 
    VALUES (4, 8, 2, 0, NULL);

--================================
-- ItemInRental Creation TODO
--================================

INSERT INTO ItemInRental (itemInRentalId, itemId, rentalXactDetailsId) 
    VALUES (1, 1, 1);

INSERT INTO ItemInRental (itemInRentalId, itemId, rentalXactDetailsId) 
    VALUES (2, 2, 2);

INSERT INTO ItemInRental (itemInRentalId, itemId, rentalXactDetailsId) 
    VALUES (3, 6, 3);

INSERT INTO ItemInRental (itemInRentalId, itemId, rentalXactDetailsId) 
    VALUES (4, 9, 3);

INSERT INTO ItemInRental (itemInRentalId, itemId, rentalXactDetailsId) 
    VALUES (5, 6, 4);


-- EMPLOYEE RELATED

INSERT INTO EmployeeRole (employeeRoleId, employeeRoleName, hourlyWage)
	VALUES (1, 'SKI_INSTRUCTOR', 15);

INSERT INTO EmployeeAssignment (employeeAssignmentId, resortPropertyId, assignmentStartDate)
	VALUES (1, 3, TO_DATE('2020-12-01', 'YYYY-MM-DD'));


INSERT INTO Employee (employeeId, employeeRoleId, employeeAssignmentId, employeeName, age, gender, dateOfBirth, raceEthnicity)
	VALUES (1, 1, 1, 'Cam Jordan', 31, 'male', TO_DATE('1994-12-01', 'YYYY-MM-DD'), 'african-american');

INSERT INTO Employee (employeeId, employeeRoleId, employeeAssignmentId, employeeName, age, gender, dateOfBirth, raceEthnicity)
	VALUES (2, 1, 1, 'Tyler Shough', 26, 'male', TO_DATE('1999-05-06', 'YYYY-MM-DD'), 'white');


INSERT INTO Employee (employeeId, employeeRoleId, employeeAssignmentId, employeeName, age, gender, dateOfBirth, raceEthnicity)
	VALUES (3, 1, 1, 'Nika Muhl', 22, 'female"', TO_DATE('2002-02-26', 'YYYY-MM-DD'), 'white');

INSERT INTO Employee (employeeId, employeeRoleId, employeeAssignmentId, employeeName, age, gender, dateOfBirth, raceEthnicity)
	VALUES (4, 1, 1, 'Sabrina Ionescu', 24, 'female', TO_DATE('2000-08-01', 'YYYY-MM-DD'), 'white');

INSERT INTO Employee (employeeId, employeeRoleId, employeeAssignmentId, employeeName, age, gender, dateOfBirth, raceEthnicity)
	VALUES (5, 1, 1, 'Mookie Betts', 28, 'male', TO_DATE('1997-01-01', 'YYYY-MM-DD'), 'african-american');


-- LESSON RELATED

--================================
-- LessonInstructor Creation
--================================

INSERT INTO LessonInstructor (instructorId, employeeId, instructorLevel) 
    VALUES (1, 1, 'Level I');

INSERT INTO LessonInstructor (instructorId, employeeId, instructorLevel) 
    VALUES (2, 2, 'Level II');

INSERT INTO LessonInstructor (instructorId, employeeId, instructorLevel) 
    VALUES (3, 3, 'Level III');

INSERT INTO LessonInstructor (instructorId, employeeId, instructorLevel) 
    VALUES (4, 4, 'Level I');

INSERT INTO LessonInstructor (instructorId, employeeId, instructorLevel) 
    VALUES (5, 5, 'Level I');

--================================
-- LessonType Creation
--================================

INSERT INTO LessonType (lessonTypeId, lessonTypeName, isPrivate) 
    VALUES (1, 'Individual Intermediate Lesson', 1);

INSERT INTO LessonType (lessonTypeId, lessonTypeName, isPrivate) 
    VALUES (2, 'Group Beginner Lesson', 0);

INSERT INTO LessonType (lessonTypeId, lessonTypeName, isPrivate) 
    VALUES (3, 'Individual Beginner Lesson', 1);

INSERT INTO LessonType (lessonTypeId, lessonTypeName, isPrivate) 
    VALUES (4, 'Group Intermediate Lesson', 0);

INSERT INTO LessonType (lessonTypeId, lessonTypeName, isPrivate) 
    VALUES (5, 'Individual Expert Lesson', 1);


--================================
-- LessonSession Creation
--================================

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (1, 1, 1, TO_DATE('2023-11-01', 'YYYY-MM-DD'), '4:30pm', '6:00pm');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (2, 2, 2, TO_DATE('2023-11-01', 'YYYY-MM-DD'), '2:30pm', '4:00pm');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (3, 3, 5, TO_DATE('2023-11-02', 'YYYY-MM-DD'), '8:00am', '10:00am');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (4, 2, 4, TO_DATE('2023-11-02', 'YYYY-MM-DD'), '12:00pm', '2:00pm');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (5, 5, 3, TO_DATE('2023-11-02', 'YYYY-MM-DD'), '2:30pm', '4:00pm');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (6, 5, 4, TO_DATE('2023-11-09', 'YYYY-MM-DD'), '2:30pm', '3:30pm');

INSERT INTO LessonSession (lessonSessionId, instructorId, lessonTypeId, sessionDate, startTime, endTime) 
    VALUES (7, 3, 5, TO_DATE('2023-11-10', 'YYYY-MM-DD'), '7:00am', '9:30am');


--================================
-- LessonXactDetails Creation TODO
--================================

INSERT INTO LessonXactDetails (lessonXactDetailsId, transactionId, numSessions, remainingSessions) 
    VALUES (1, 3, 1, 2);

INSERT INTO LessonXactDetails (lessonXactDetailsId, transactionId, numSessions, remainingSessions) 
    VALUES (2, 5, 1, 2);

INSERT INTO LessonXactDetails (lessonXactDetailsId, transactionId, numSessions, remainingSessions) 
    VALUES (3, 5, 2, 1);

INSERT INTO LessonXactDetails (lessonXactDetailsId, transactionId, numSessions, remainingSessions) 
    VALUES (4, 5, 3, 0);

INSERT INTO LessonXactDetails (lessonXactDetailsId, transactionId, numSessions, remainingSessions) 
    VALUES (5, 3, 2, 1);

--================================
-- LessonUsage Creation TODO
--================================

INSERT INTO LessonUsage (lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) 
    VALUES (1, 1, 1, TO_DATE('2023-11-01', 'YYYY-MM-DD'), 1, 3);

INSERT INTO LessonUsage (lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) 
    VALUES (2, 2, 2, TO_DATE('2023-11-01', 'YYYY-MM-DD'), 1, 3);

INSERT INTO LessonUsage (lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) 
    VALUES (3, 3, 5, TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1, 2);

INSERT INTO LessonUsage (lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) 
    VALUES (4, 4, 6, TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1, 1);

INSERT INTO LessonUsage (lessonUsageId, lessonXactDetailsId, lessonSessionId, usedDate, attended, remSessions) 
    VALUES (5, 5, 7, TO_DATE('2023-11-10', 'YYYY-MM-DD'), 1, 2);





-- LIFT TRAIL RELATED

--================================
-- Lift Creation
--================================

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (1, 'Step Ascent', 'expert', '7:00am', '5:00pm', 1);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (2, 'Beginner Breeze', 'beginner', '8:00am', '4:00pm', 1);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (3, 'Summit Soar', 'intermediate', '7:30am', '4:30pm', 1);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (4, 'Bunny Hop Express', 'beginner', '9:00am', '3:30pm', 1);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (5, 'Glacier Rise', 'intermediate', '8:00am', '5:00pm', 1);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (6, 'Twilight Traverse', 'expert', '6:45am', '6:00pm', 0);

INSERT INTO Lift (liftId, liftName, abilityLevel, openTime, closeTime, isOpen) 
    VALUES (7, 'Balloon Tram', 'intermediate', '8:00am', '3:30pm', 0);


--================================
-- Trail Creation
--================================

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (1, 'Sidewinder', 'North Basin', 'Main Leave Off', 1, 'intermediate', 'groomed');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (2, 'Snowfield', 'East Knoll', 'Lodge Drop', 1, 'beginner', 'groomed');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (3, 'Frostbite Run', 'Upper Peak', 'Midway Lodge', 1, 'intermediate', 'park');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (4, 'Blizzard Bluff', 'South Ridge', 'Base Camp', 1, 'expert', 'glade');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (5, 'Gentle Glide', 'West Grove', 'Green Hollow', 1, 'beginner', 'groomed');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (6, 'Avalanche Alley', 'Summit Crest', 'Thunder Pass', 0, 'expert', 'mogul');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (7, 'Shiver Slide', 'West Grove', 'Base Camp', 0, 'intermediate', 'groomed');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (8, 'Speedbump', 'Upper Peak', 'Main Leave Off', 1, 'intermediate', 'mogul');

INSERT INTO Trail (trailId, trailName, startLoc, endLoc, isOpen, difficulty, category) 
    VALUES (9, 'Handshake', 'Sunrise Sunset', 'Thunder Pass', 1, 'intermediate', 'groomed');


--================================
-- LiftTrailAccess Creation
--================================

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (1, 1, 1);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (2, 2, 2);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (3, 3, 3);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (4, 4, 5);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (6, 6, 4);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (7, 5, 3);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (8, 6, 6);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (9, 4, 7);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (10, 3, 8);

INSERT INTO LiftTrailAccess (liftTrailAccessId, liftId, trailId) 
    VALUES (11, 7, 9);


--================================
-- LiftUsage Creation TODO
--================================

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (1, 1, 1, 1, TO_TIMESTAMP('2023-11-01 16:50:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (2, 2, 2, 2, TO_TIMESTAMP('2023-11-01 15:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (3, 6, 1, 1, TO_TIMESTAMP('2023-11-02 08:30:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (4, 3, 1, 1, TO_TIMESTAMP('2023-11-02 08:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (5, 6, 1, 1, TO_TIMESTAMP('2023-11-02 09:02:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (6, 2, 2, 2, TO_TIMESTAMP('2023-11-09 15:09:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (7, 2, 2, 2, TO_TIMESTAMP('2023-11-09 15:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO LiftUsage (liftUsageId, liftId, skiPassId, memberId, usageDateTime) 
	VALUES (8, 3, 2, 2, TO_TIMESTAMP('2023-11-10 08:30:00', 'YYYY-MM-DD HH24:MI:SS'));



