-- SQL Schema for Hotel Reservation System

CREATE TABLE IF NOT EXISTS rooms (
    room_id VARCHAR(50) PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL,
    base_rate DECIMAL(10, 2) NOT NULL CHECK (base_rate >= 0),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    type VARCHAR(20) NOT NULL -- 'STANDARD', 'DELUXE', 'SUITE'
);

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    user_phone VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
    reservation_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    user_phone VARCHAR(20) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    is_cancelled BOOLEAN NOT NULL DEFAULT FALSE,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
