-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO planes(id, operator, model, immatriculation, capacity) 
VALUES(NEXTVAL('planes_sequence_in_database'), 'AirbusIndustrie', 'AIRBUS A380', 'F-ABCD', 2);
INSERT INTO planes(id, operator, model, immatriculation, capacity) 
VALUES(NEXTVAL('planes_sequence_in_database'), 'Boeing', 'BOEING CV2', 'F-AZER', 15);
INSERT INTO flights(id, number, origin, destination, departure_date, departure_time, arrival_date, arrival_time, plane_id)
VALUES(NEXTVAL('flights_sequence_in_database'), '468-556', 'Paris', 'New York', '2023-12-26', '08:56:00.000', '2023-12-26', '16:34:00.000', 1);
INSERT INTO passengers(id, surname, firstname, email_address)
VALUES(NEXTVAL('passengers_sequence_in_database'), 'Houziaux', 'Teva', 'oui.non@gmail.com');
