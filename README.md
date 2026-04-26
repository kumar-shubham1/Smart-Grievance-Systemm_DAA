
### 🚀 Smart Grievance Management System

### 📊 A DAA-Based Complaint Prioritization & Management Platform

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/UI-Java%20Swing-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Architecture-MVC-purple?style=for-the-badge" />
</p>

---

## 🧠 Project Overview

The **Smart Grievance Management System** is a desktop-based Java application that enables efficient complaint handling using **Design & Analysis of Algorithms (DAA)** concepts.

It focuses on:

* ⚡ Smart prioritization of complaints
* 🔍 Detection of duplicate issues
* 📈 Data-driven decision making

---

## 🎯 Objective

* Automate complaint prioritization
* Improve resolution efficiency
* Demonstrate real-world use of **DAA concepts**

---

## 👨‍💻 Team Information

| Role            | Name                   |
| --------------- | ---------------------- |
| 🧑‍💼 Team Lead | **Shubham Kumar**      |
| 👨‍💻 Member    | Parikshit Singh Panwar |
| 👩‍💻 Member    | Avni Negi              |
| 👩‍💻 Member    | Jahnvi Sharma          |

🎓 **Institution:** Graphic Era Deemed to be University, Dehradun
📘 **Course:** 4th Semester DAA (PBL)
👨‍🏫 **Mentor:** Kartikey Arora Sir

---

## 🛠️ Tech Stack

| Layer           | Technology     |
| --------------- | -------------- |
| 💻 Language     | Java (JDK 17+) |
| 🎨 UI           | Java Swing     |
| 🗄️ Database    | MySQL 8.0      |
| 🔌 Connectivity | JDBC           |
| 🧱 Architecture | MVC            |

---

## 🧠 DSA & DAA Concepts Used

### 🔹 Priority Calculation

```text
Priority = (Severity × 0.5) + (Urgency × 0.3) + (Impact × 0.2)
```

### 🔹 Top-K Complaints

* Sorting based on priority
* Identifies most critical issues

### 🔹 Rabin-Karp Algorithm

* Detects duplicate complaints
* String pattern matching

### 🔹 Trend Analysis

* Filters complaints over last N days

### 🔹 Searching

* SQL + keyword-based filtering

---

## ✨ Features

### 👤 User

* Submit complaints
* Automatic priority calculation
* View complaint status

### 👨‍🔧 Team

* View assigned complaints
* Update status
* Search functionality

### 👨‍💼 Admin

* Manage all complaints
* Top-K complaints
* Trend analysis
* Duplicate detection

### 🔐 Authentication

* Role-based login system

---

## 🧩 System Architecture

```
UI (Swing) → DAO (JDBC) → MySQL Database
```

---

## ⚙️ Requirements

* Java JDK **17+**
* MySQL Server **8.0+**
* IntelliJ IDEA (recommended)
* MySQL Connector/J

---

## 🗄️ Database Setup

### 1️⃣ Create Database

```sql
CREATE DATABASE grievance_system;
USE grievance_system;
```

---

### 2️⃣ Create Tables

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(50)
);

CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    category VARCHAR(100),
    severity INT,
    urgency INT,
    impact INT,
    priority DOUBLE,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

### 3️⃣ Insert Sample Data

```sql
INSERT INTO users(username, password, role) VALUES
('admin','1234','ADMIN'),
('user1','1234','USER'),
('it1','1234','IT');
```

---

## 🔌 Database Configuration

```java
String URL = "jdbc:mysql://localhost:3306/grievance_system";
String USER = "root";
String PASSWORD = "1234";
```

---

## ▶️ How to Run

1. Open project in IntelliJ IDEA
2. Add MySQL Connector JAR
3. Run:

```
LoginUI.java
```

---

## 🖥️ Application Flow

```
Login → Role Detection → Dashboard
       → Admin / Team / User Panels
```

---

## 📸 Application Screenshots

### 🔐 Login Screen

![Login](screenshots/login.png)

### 👤 User Dashboard

![User](screenshots/user.png)

### 👨‍🔧 Team Dashboard

![Team](screenshots/team.png)

### 👨‍💼 Admin Dashboard

![Admin](screenshots/admin.png)

---

## 🎨 UI Highlights

* 🌙 Dark-themed UI
* 🎯 Clean layout
* 🎨 Centralized styling using Theme.java

---

## 🧪 Testing

* Manual UI testing
* Role validation
* Database testing
* Edge case handling

---

## 🚀 Future Enhancements

* 🔐 Password hashing
* 📊 Graph dashboards
* 🌐 Web version
* 📱 Mobile app

---

## 📈 Learning Outcomes

* Practical use of DAA
* JDBC & DB integration
* Swing UI design
* MVC architecture

---

## 📌 Conclusion

This project demonstrates how **DAA concepts can be applied to real-world systems**, making complaint handling smarter and more efficient.

---

## ⭐ Acknowledgement

Special thanks to **Kartikey Arora Sir** for guidance and support.

---

## 💙 Team Note

> Built with logic, teamwork, and lots of debugging 😄
> — Team Smart Grievance System

