# Event Booking System

## Goal

Allow users to browse events, book tickets and make payments.

---

## Core Entities

### User

* id
* name
* email
* password

### Event

* id
* title
* description
* location
* startTime
* endTime
* capacity

### Booking

* id
* userId
* eventId
* status
* createdAt

### Payment

* id
* bookingId
* amount
* status
* transactionReference

---

## Relationships

User (1) ---- (*) Booking

Event (1) ---- (*) Booking

Booking (1) ---- (1) Payment

---

## Business Rules

1. A user can create many bookings.
2. An event can have many bookings.
3. Event capacity cannot be exceeded.
4. Payment must be successful before a booking becomes CONFIRMED.
5. Canceled bookings should release reserved capacity.

---

## API Endpoints

### Authentication

POST /auth/register

POST /auth/login

### Events

POST /events

GET /events

GET /events/{id}

PATCH /events/{id}

DELETE /events/{id}

### Bookings

POST /bookings

GET /bookings/{id}

GET /bookings/user/{userId}

DELETE /bookings/{id}

### Payments

POST /payments

GET /payments/{id}

---

## Future Features

* Email notifications
* QR tickets
* Multiple ticket types
* Admin dashboard
* Refunds
* Event analytics
