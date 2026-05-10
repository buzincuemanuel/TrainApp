INSERT INTO stations (id, name) VALUES (1, 'Cluj-Napoca');
INSERT INTO stations (id, name) VALUES (2, 'Alba Iulia');
INSERT INTO stations (id, name) VALUES (3, 'Sibiu');
INSERT INTO stations (id, name) VALUES (4, 'Brasov');
INSERT INTO stations (id, name) VALUES (5, 'Bucuresti');

INSERT INTO trains (id, name, capacity) VALUES (1, 'IR 1734', 150);
INSERT INTO trains (id, name, capacity) VALUES (2, 'R 2501', 80);

INSERT INTO routes (id, name) VALUES (1, 'Cluj -> Bucuresti (via Sibiu)');
INSERT INTO routes (id, name) VALUES (2, 'Brasov -> Bucuresti (Direct)');

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
    ('Admin User', 'admin@trainapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHi', 'ADMIN');

INSERT INTO users (name, email, password, role) VALUES
    ('Ion Popescu', 'ion@trainapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHi', 'USER');