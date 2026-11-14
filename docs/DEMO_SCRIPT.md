# ExamSphere — Demo Script

## Title Card (2 seconds)

```
Hariharan R
Java Development
Task 4 — Online Examination System
Oasis Infobyte SIP
```

---

## Walkthrough (~3 minutes)

### 0:00 — Launch
- Double-click `run.bat` or run `java -jar target/online-examination-system-1.0.0.jar`
- Show the **Splash Screen** with the ExamSphere branding and progress bar
- Database initialises automatically; splash transitions to Login

### 0:15 — Login
- Show the **Login screen**: split layout with ExamSphere branding on left, login card on right
- Enter credentials: `student` / `student123`
- Click **LOGIN** (or press Enter)
- Briefly mention: SHA-256 is replaced with PBKDF2 — passwords never stored in plaintext

### 0:35 — Dashboard
- Show the **Dashboard**: welcome message, 30-question / 30-min / 60% exam card
- Point out the **Last Score** area (shows "—" on first visit)
- Click **👤 Profile** button

### 0:50 — Profile
- Show the **Profile panel**: username (read-only), Full Name, Email
- Click **EDIT PROFILE**, update the Full Name
- Click **SAVE CHANGES** — success message appears
- Scroll to **Change Password** section
- Demonstrate: enter wrong current password → error "Current password is incorrect"
- Click **← Back to Dashboard**

### 1:10 — Start Exam
- Click **START EXAM →**
- Show the **Exam Instructions panel**: 30 questions, 30 minutes, navigation rules
- Click **▶ Start Exam Now** — timer starts at 30:00

### 1:25 — Active Exam
- Show **Question 1**: question text, four radio-button options, Question Navigator
- Select option **B** — navigator button turns green
- Click **Next →** to Q2; select an answer
- Click **☆ Mark** on Q2 — navigator button turns amber ("Marked for Review")
- Click **✕ Clear** on Q3 — confirm answer is removed (button returns to dark)
- Use the **Question Navigator** to jump back to Q1
- Show the **timer** changing colour at low time (mention 60s→amber, 30s→red)

### 1:55 — Submission
- Click **SUBMIT EXAM**
- Show the **confirmation dialog**: answered count, unanswered count, marked count, time remaining
- Click **Submit Now**

### 2:10 — Results
- Show the **Results screen**: percentage, Pass/Fail badge
- Point out the five metric tiles: Total, Correct, Incorrect, Unanswered, **Time Taken**
- Click **VIEW ANSWERS**
- Show the **Answer Review dialog**: correct answers (green), wrong answers (red), explanations

### 2:35 — Close Guard Demo
- Navigate back to Dashboard, start another exam (click Start)
- Try to close the window → three-option dialog appears: Continue / Exit & Submit / Exit & Discard
- Click **Continue Exam**

### 2:50 — Logout
- Click **⎋ Logout** — confirm dialog
- Login screen reappears

### 3:00 — End

---

## LinkedIn Announcement Draft

> 🎉 Excited to share my latest project — **ExamSphere**, an Online Examination System built as
> Task 4 of my Java Development Internship at **Oasis Infobyte**!
>
> ExamSphere is a fully functional Java 21 desktop application featuring:
> ✅ PBKDF2 password security with SQLite persistence
> ✅ 40+ MCQ questions across 8 Java categories
> ✅ 30-minute countdown timer with auto-submit
> ✅ Mark for Review, Clear Answer, and Question Navigator
> ✅ Complete result screen with time-taken and answer explanations
> ✅ Branded splash screen, exam instructions, and close-window safety guard
>
> Built with Java Swing, Maven, SQLite JDBC, and JUnit 5.
>
> Huge thanks to the Oasis Infobyte team for this learning opportunity!
>
> #oasisinfobyte #javadevelopment #internship #java #swing #sqlite
