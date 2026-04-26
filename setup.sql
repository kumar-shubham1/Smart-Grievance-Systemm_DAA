CREATE DATABASE IF NOT EXISTS daa_complaint_system;
USE daa_complaint_system;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    severity INT,
    urgency INT,
    impact INT,
    priority DOUBLE,
    status VARCHAR(20) DEFAULT 'NEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Basic data
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', '1234', 'ADMIN');
INSERT IGNORE INTO users (username, password, role) VALUES ('user1', '1234', 'USER');
INSERT IGNORE INTO users (username, password, role) VALUES ('team1', '1234', 'TEAM');
