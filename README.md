# Train Ticketing Application

TrainApp is a Java Spring Boot application used for managing train schedules, searching routes, booking tickets, and handling administrative operations for a railway system.

The application supports:
- ticket booking with overbooking prevention
- route searching with direct connections and transfers
- train and route management
- booking management
- train delay notifications sent by email

## Technologies Used

- Java 25
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Mail
- Thymeleaf
- Bootstrap 5
- H2 Database

## Architecture

The project follows a layered architecture:

```text
Controller -> Service -> Repository
```

### Main Features
- BFS / graph traversal for route searching
- Pessimistic locking for preventing race conditions and overbooking
- Role-based authentication using Spring Security

---

# Functionalities and Examples

# A) Ticket Booking System

Users can reserve one or multiple seats for a train route.  
Before saving a booking, the system checks the train capacity to prevent overbooking.

## Scenario 1: Successful Booking

### Input
```text
Train: IR 1734
Route: Cluj-Napoca -> Bucuresti
Requested seats: 2
```

### Output
```text
Booking completed successfully.
```

### Additional Result
```text
Confirmation email sent to the user.
```

---

## Scenario 2: Overbooking Prevention

### Input
```text
Train capacity: 150
Already booked seats: 149
Requested seats: 2
```

### Output
```text
Not enough available seats. Only 1 seat(s) left.
```

---

# B) Route Finder

The application searches for:
- direct routes
- routes with one transfer
- routes with two transfers

Departure and arrival times are calculated based on the train schedule and delays.

## Scenario 1: Direct Route

### Input
```text
Departure: Cluj-Napoca
Arrival: Sibiu
```

### Output
```text
Direct Route

IR 1734
Departure: 08:00
Arrival: 11:30
Duration: 3h 30m
```

---

## Scenario 2: Route with Transfer

### Input
```text
Departure: Cluj-Napoca
Arrival: Constanta
```

### Output
```text
Route with 1 Transfer

Leg 1:
Cluj-Napoca -> Bucuresti
IR 1734
08:00 - 15:30

Transfer at Bucuresti: 30 minutes

Leg 2:
Bucuresti -> Constanta
IR 1900
16:00 - 18:00
```

---

## Scenario 3: No Route Found

### Input
```text
Departure: Cluj-Napoca
Arrival: London
```

### Output
```text
No routes found between these stations.
```

---

# C) Administrator Operations

The administrator panel is protected using role-based authentication and allows management of trains, routes, bookings, and delays.

---

# C.1) Train Management

## Add Train

### Input
```text
Name: IC 531
Capacity: 200
```

### Output
```text
Train added successfully.
```

---

## Modify Train

### Input
```text
Update capacity of IC 531 to 250
```

### Output
```text
Train updated successfully.
```

---

## Remove Train

### Input
```text
Delete train IC 531
```

### Output
```text
Train removed successfully.
```

---

# C.2) Route and Station Management

## Add Route

### Input
```text
Route: Timisoara -> Arad
```

### Output
```text
Route created successfully.
```

---

## Add Stations to Route

### Input
```text
Station: Timisoara
Order: 1
Minutes: 0

Station: Arad
Order: 2
Minutes: 50
```

### Output
```text
Stations added successfully.
```

---

# C.3) View Bookings

Administrators can view all bookings for a train schedule.

## Input
```text
Select train: IR 1734
Action: View Bookings
```

## Output
```text
Passenger: Ion Popescu
Email: ion@trainapp.com
Seats: 2
Route Segment: Cluj-Napoca -> Sibiu
```

---

# C.4) Delay Notifications

Administrators can specify delays for trains.  
All passengers with reservations for the affected train are notified automatically.

## Input
```text
Train: IR 1734
Delay: 45 minutes
```

## Output
```text
Delay updated successfully.
```

### Email Notification Example
```text
Subject: Train Delay Notification

Dear Ion Popescu,

Your train IR 1734 has a delay of 45 minutes.
```

### Additional Result
```text
Updated times are automatically reflected in future searches.
```