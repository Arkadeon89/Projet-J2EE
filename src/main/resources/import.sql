-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
BEGIN;
INSERT INTO planes(id, operator, model, immatriculation, capacity) 
VALUES(NEXTVAL('planes_sequence_in_database'), 'AirbusIndustrie', 'AIRBUS A380', 'F-ABCD', 10);
INSERT INTO planes(id, operator, model, immatriculation, capacity) 
VALUES(NEXTVAL('planes_sequence_in_database'), 'Boeing', 'BOEING CV2', 'F-AZER', 15);
COMMIT;
