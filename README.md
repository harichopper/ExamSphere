# ExamSphere — Online Examination System

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Framework](https://img.shields.io/badge/UI-Java%20Swing-blue.svg)]()
[![Build](https://img.shields.io/badge/Build-Maven%203.9+-brightgreen.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-33%2B%20Passing-success.svg)]()
[![Database](https://img.shields.io/badge/Database-SQLite%203.45-lightblue.svg)]()
[![Internship](https://img.shields.io/badge/OASIS%20INFOBYTE-Task%204-purple.svg)](https://oasisinfobyte.com/)

> **"Test your knowledge. Track your progress."**
> A production-quality desktop examination platform built for the
> **OASIS INFOBYTE Java Development Internship — Task 4**.

---

## 📌 Project Overview

**ExamSphere** is a Java 21 Swing desktop application for standardised digital examinations.
It features a modern dark-theme design system, SQLite-backed persistence, PBKDF2 password security,
a 40-question bank across 8 Java categories, and a full exam lifecycle from splash-screen through
results review.

---

## ⚡ Core Features

### Authentication & Security
- PBKDF2WithHmacSHA256 password hashing with random 16-byte salts (310 000 iterations).
- Login screen with show/hide password, Enter-key submission, and inline error feedback.
- Session teardown on logout; session guard on window close during active exam.

### Profile Management
- View and edit display name and email address with validation.
- Change password with current-password verification (min 8 characters).
- Persisted immediately to SQLite via JDBC.

### SQLite Persistence
- Database file: `data/examsphere.db` (auto-created on first run).
- Tables: `users`, `exams`, `questions`, `attempts`, `attempt_answers`.
- Seed data inserted on first run: 2 demo users, 40 questions, 1 default exam.
- Attempt history stored; last score shown on the Dashboard.

### Exam Engine
- 40-question bank across 8 categories: Java Fundamentals, OOP, Collections, Exception Handling,
  Multithreading, JDBC & Databases, Data Structures, Logical Reasoning.
- Default exam: 30 questions, 30-minute countdown.
- One answer per question; free navigation with Previous / Next and Question Navigator grid.
- **Mark for Review** toggle and **Clear Answer** button.
- Navigator shows colour-coded states: Current (indigo), Answered (green),
  Marked (amber), Answered+Marked (green/amber border), Unanswered (dark).

### Timer & Submission
- 30-minute countdown with Normal / Warning (≤60 s) / Critical (≤30 s) colour states.
- Auto-submission at 00:00 with confirmation dialog.
- Manual submission shows answered / unanswered / marked counts and time remaining.
- `AtomicBoolean` guard prevents duplicate finalisation.

### Results & Review
- Score, percentage, pass/fail (60% threshold), time taken (MM:SS / HH:MM:SS).
- Metric tiles: Total, Correct, Incorrect, Unanswered, Time Taken.
- Full question-by-question review with selected answer, correct answer, and explanation.

### Splash Screen & Instructions
- Branded splash screen with progress bar while the database initialises.
- Exam Instructions panel (shown before the timer starts) listing all rules.

### Close-Window Safety
- Closing the window during an active exam shows a three-option dialog:
  Continue Exam, Exit & Submit, or Exit & Discard.

---

## 🛠️ Technology Stack

| Component | Technology |
|---|---|
| Language | Java 21 LTS |
| GUI | Java Swing (pure, no JavaFX) |
| Database | SQLite 3.45 via `org.xerial:sqlite-jdbc` |
| Security | PBKDF2WithHmacSHA256 (`javax.crypto`) |
| Testing | JUnit 5 (Jupiter 5.10.2) + jqwik 1.8.4 |
| Build | Apache Maven 3.9+ with Shade fat-JAR |
| Design | Custom dark-theme component library |

---

## 🚀 Quick Start

### Demo Credentials

| Role | Username | Password |
|---|---|---|
| Student | `student` | `student123` |
| Admin   | `admin`   | `admin123`   |

Accounts are seeded automatically on first launch. Passwords are stored as PBKDF2 hashes — never plaintext.

### Running the Application

**Option A — Windows batch script:**
```
run.bat
```

**Option B — Fat JAR:**
```
java -jar target/online-examination-system-1.0.0.jar
```

**Option C — Maven:**
```
mvn clean package
mvn exec:java
```

The database file `data/examsphere.db` is created automatically on first run.

---

## 🧪 Automated Testing

```
mvn clean test
```
Or double-click `test.bat`.

33+ tests covering authentication, JDBC repositories, exam lifecycle, scoring, PBKDF2 hashing,
and validation.

---

## 📁 Project Structure

```
Java-Task4-OnlineExaminationSystem/
├── pom.xml
├── README.md
├── run.bat
├── test.bat
├── docs/
│   ├── ARCHITECTURE.md
│   ├── DATABASE_SCHEMA.md
│   ├── DEMO_SCRIPT.md
│   ├── FEATURES.md
│   ├── TESTING.md
│   └── USER_GUIDE.md
├── screenshots/
└── src/
    ├── main/java/com/oasisinfobyte/examination/
    │   ├── Main.java
    │   ├── database/          DatabaseManager, DatabaseInitializer
    │   ├── model/             User, Question, Answer, Exam, ExamResult
    │   ├── repository/        UserRepository, QuestionRepository,
    │   │                      AttemptRepository, AttemptRecord
    │   ├── service/           AuthenticationService, ExamService,
    │   │                      ResultService, SessionManager
    │   ├── ui/                LoginFrame, DashboardFrame, DashboardPanel,
    │   │                      SplashScreen, ExamInstructionsPanel,
    │   │                      ExamPanel, ProfilePanel, ResultPanel, ReviewDialog
    │   ├── ui/components/     RoundedButton, RoundedPanel, QuestionCard,
    │   │                      QuestionNavigator, TimerPanel, StatusBadge,
    │   │                      ModernTextField, ModernPasswordField
    │   ├── ui/util/           UIConstants
    │   └── util/              PasswordUtil, ValidationUtil, QuestionBank
    └── test/java/com/oasisinfobyte/examination/
        ├── model/             ModelEntitiesTest
        ├── repository/        RepositoriesTest
        ├── service/           AuthenticationServiceTest, ExamServiceTest,
        │                      ResultServiceTest
        └── util/              PasswordUtilTest, ValidationUtilTest
```

---

## 🎨 Design System

| Token | Value |
|---|---|
| Primary Background | `#0B1020` |
| Card Background | `#172033` |
| Primary Accent (Indigo) | `#6366F1` |
| Secondary Accent (Cyan) | `#06B6D4` |
| Success (Emerald) | `#22C55E` |
| Warning (Amber) | `#F59E0B` |
| Danger (Red) | `#EF4444` |

---

## 📄 Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Features](docs/FEATURES.md)
- [Database Schema](docs/DATABASE_SCHEMA.md)
- [Testing](docs/TESTING.md)
- [User Guide](docs/USER_GUIDE.md)
- [Demo Script](docs/DEMO_SCRIPT.md)

---

## 🏆 Internship Context

| Field | Value |
|---|---|
| Internship | OASIS INFOBYTE Java Development Internship (OIBSIP) |
| Task | Java Task 4 — Online Examination System |
| Developer | Hariharan R |
| GitHub | [harichopper](https://github.com/harichopper) |
| Repository | [OIBSIP](https://github.com/harichopper/OIBSIP) |
