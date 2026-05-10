
INSERT INTO stations (name) VALUES ('Cluj-Napoca');
INSERT INTO stations (name) VALUES ('Alba Iulia');
INSERT INTO stations (name) VALUES ('Sibiu');
INSERT INTO stations (name) VALUES ('Brasov');
INSERT INTO stations (name) VALUES ('Bucuresti');

INSERT INTO trains (name, capacity) VALUES ('IR 1734', 150);
INSERT INTO trains (name, capacity) VALUES ('R 2501', 80);

INSERT INTO routes (name) VALUES ('Cluj -> Bucuresti (via Sibiu)');
INSERT INTO routes (name) VALUES ('Brasov -> Bucuresti (Direct)');

INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (1, 1, 1, 0);
INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (1, 2, 2, 120);
INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (1, 3, 3, 210);
INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (1, 4, 4, 300);
INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (1, 5, 5, 450);

INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (2, 4, 1, 0);
INSERT INTO route_stops (route_id, station_id, stop_order, minutes_from_start) VALUES (2, 5, 2, 150);

INSERT INTO train_schedules (train_id, route_id, departure_time, delay_minutes) VALUES (1, 1, '08:00:00', 0);
INSERT INTO train_schedules (train_id, route_id, departure_time, delay_minutes) VALUES (2, 2, '14:00:00', 0);

INSERT INTO users (name, email, password, role) VALUES
    ('Admin User', 'admin@trainapp.com', '$2a$10$jyS1/n/l8xHaeDzs.ub.8.ufW5hyAVUZvzx.YxPebACF4lhDAogz.', 'ADMIN');

INSERT INTO users (name, email, password, role) VALUES
    ('Ion Popescu', 'ion@trainapp.com', '$2a$10$jyS1/n/l8xHaeDzs.ub.8.ufW5hyAVUZvzx.YxPebACF4lhDAogz.', 'USER');

ALTER SEQUENCE IF EXISTS station_seq RESTART WITH 10;