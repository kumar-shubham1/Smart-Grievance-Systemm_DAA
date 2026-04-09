# 🚀 Smart Complaint Management System (DAA-Based)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)
![DAA](https://img.shields.io/badge/DAA-Algorithms-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Project-Completed-brightgreen?style=for-the-badge)

---

## 📌 Overview
A Java Swing + MySQL based Complaint Management System enhanced with **Design and Analysis of Algorithms (DAA)**.

### ✨ What it does:
- 📥 Submit complaints
- ⚡ Priority-based processing
- 🔍 Duplicate detection
- 📊 Trend analysis
- 🧠 Smart algorithm-driven system

---

## 🎯 Objective
- Apply DAA in real-world system  
- Improve complaint resolution efficiency  
- Reduce manual work  
- Build scalable architecture  

---

## 🧠 DAA Concepts Used

| Concept | Implementation |
|--------|--------------|
| Greedy | Priority calculation |
| Heap (Priority Queue) | Process highest priority |
| HashMap | O(1) lookup |
| Rabin-Karp | Duplicate detection |
| Top-K | Important complaints |
| Sliding Window | Trend analysis |

---

## 🔥 Features

### ✅ Priority System
Priority = (Severity × 0.5) + (Urgency × 0.3) + (Impact × 0.2)

### 🔍 Duplicate Detection
Uses Rabin-Karp hashing

### 📊 Trend Analysis
Filter complaints by last N days

### 🥇 Top K Complaints
Heap-based optimization

### 👥 Role-Based Access
- User → Submit
- Team → Resolve
- Admin → Manage

---

## 🏗️ Architecture

UI (Swing)
↓
Service (DAA Logic)
↓
DAO (Database)
↓
MySQL

---

## 📁 Project Structure

src/
 ├── dao/
 ├── model/
 ├── service/
 ├── ui/
 └── util/

---

## ⚙️ Tech Stack
- Java
- Swing
- MySQL
- JDBC

---

## 🛠️ Setup

### Database
CREATE DATABASE daa_complaint_system;

### Run
Run LoginUI.java

---

## 🔑 Sample Users
admin / 123 → ADMIN  
user1 / 123 → USER  

---

## 🚀 Workflow

User → Submit complaint  
Admin → Analyze + manage  
Team → Resolve  

---

## 📈 Future Scope
- Web app (Spring Boot)
- AI classification
- Dashboard

---

## 👨‍💻 Authors
Shubham Kumar  
Parikshit Singh  
Jahnvi Sharma  
Avni Negi  

---

## ⭐ Star the repo if you like it!
