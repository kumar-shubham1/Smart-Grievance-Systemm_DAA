# 🚀 Complaint Management System (DAA-Based)

## 📌 Overview
The Complaint Management System is a Java-based desktop application built using Java Swing + MySQL, enhanced with Design and Analysis of Algorithms (DAA).

It provides:
- Complaint submission
- Priority-based processing
- Trend analysis
- Duplicate detection
- Efficient complaint handling

--------------------------------------------------

## 🎯 Objective
- Optimize complaint handling using DAA
- Reduce manual effort
- Improve efficiency
- Demonstrate real-world algorithm usage

--------------------------------------------------

## 🧠 DAA Concepts Used

- Greedy Algorithm → Priority calculation
- Priority Queue (Heap) → Process highest priority first
- HashMap → Fast lookup (O(1))
- Rabin-Karp → Duplicate detection
- Top-K Algorithm → Find most important complaints
- Sliding Window → Trend analysis

--------------------------------------------------

## 🔥 Key Features

1. Priority-Based System
   Priority = (Severity × 0.5) + (Urgency × 0.3) + (Impact × 0.2)

2. Smart Processing
   Uses priority queue for fast handling

3. Duplicate Detection
   Uses Rabin-Karp algorithm

4. Top K Complaints
   Finds highest priority complaints

5. Trend Analysis
   Filters complaints from last N days

6. Fast Search
   Uses HashMap for instant lookup

7. Role-Based System
   - User → Submit complaints
   - Team → Resolve complaints
   - Admin → Manage system

--------------------------------------------------

## 🏗️ Project Structure

src/
 ├── dao/
 ├── model/
 ├── service/
 ├── ui/
 └── util/

--------------------------------------------------

## ⚙️ Tech Stack

- Java
- Swing
- MySQL
- JDBC

--------------------------------------------------

## 🛠️ How to Run

1. Install Java & MySQL
2. Create database: daa_complaint_system
3. Update DBConnection.java credentials
4. Run LoginUI.java

--------------------------------------------------

## 🔑 Sample Users

admin / 123 → ADMIN
user1 / 123 → USER
IT / 123 → TEAM

--------------------------------------------------

## 🚀 Workflow

User:
- Submit complaint
- Priority calculated

Admin:
- View complaints
- Analyze trends
- Detect duplicates

Team:
- View assigned complaints
- Update status

--------------------------------------------------

## 📈 Future Scope

- Web version
- AI-based classification
- Dashboard analytics
- Cloud deployment

--------------------------------------------------

## 👨‍💻 Author

Shubham Kumar(Team Lead)
Parikshit Singh Panwar
Jahnvi Sharma
Avni Negi



--------------------------------------------------

## ⭐ GitHub

Give a star if you like this project!
