-- Parking Lot Management System Database Schema

CREATE TABLE IF NOT EXISTS parking_spots (
    spot_id VARCHAR(20) PRIMARY KEY,
    floor_number INT NOT NULL,
    spot_type VARCHAR(20) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL,
    is_occupied BOOLEAN DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vehicles (
    license_plate VARCHAR(20) PRIMARY KEY,
    vehicle_type VARCHAR(20) NOT NULL,
    has_handicapped_card BOOLEAN DEFAULT 0,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    spot_id VARCHAR(20),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)
);

CREATE TABLE IF NOT EXISTS tickets (
    ticket_id VARCHAR(50) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    spot_id VARCHAR(20) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(50) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    parking_fee DECIMAL(10,2) NOT NULL,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS fines (
    fine_id INTEGER PRIMARY KEY AUTOINCREMENT,
    license_plate VARCHAR(20) NOT NULL,
    fine_date TIMESTAMP NOT NULL,
    fine_reason VARCHAR(100) NOT NULL,
    fine_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'UNPAID'
);

CREATE TABLE IF NOT EXISTS floors (
    floor_number INT PRIMARY KEY,
    total_spots INT NOT NULL,
    occupied_spots INT DEFAULT 0
);