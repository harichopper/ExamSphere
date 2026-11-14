# ExamSphere — User Guide

## Launching the Application

**Windows:** Double-click `run.bat`  
**Command line:** `java -jar target/online-examination-system-1.0.0.jar`  
**Maven:** `mvn clean package` then `mvn exec:java`

The database is created automatically at `data/examsphere.db` on first launch.

---

## Logging In

1. The splash screen appears briefly while the database initialises.
2. On the Login screen, enter your username and password.
3. Demo credentials: `student` / `student123`
4. Click **LOGIN** or press **Enter**.
5. Use the "Show Password" checkbox to reveal the password if needed.

---

## Dashboard

After login you see:
- A personalised welcome message.
- The available exam card showing 30 questions, 30 minutes, 60% passing score.
- Your last score (if you have a previous attempt).
- **Profile** and **Logout** buttons in the top-right.

---

## Editing Your Profile

1. Click **👤 Profile** on the Dashboard.
2. Click **EDIT PROFILE** to enable the Name and Email fields.
3. Update the values and click **SAVE CHANGES**.
4. To change your password, fill in the **Change Password** section below and click **CHANGE PASSWORD**.
   - Enter your current password.
   - Enter a new password (minimum 8 characters).
   - Confirm the new password.

---

## Starting an Exam

1. Click **START EXAM →** on the Dashboard.
2. Read the **Exam Instructions** panel carefully.
3. Click **▶ Start Exam Now** — the 30-minute timer starts immediately.

---

## During the Exam

| Action | How |
|---|---|
| Select an answer | Click any radio button or the row panel |
| Move to next question | Click **Next →** or press **→** arrow key |
| Move to previous question | Click **← Previous** or press **←** arrow key |
| Jump to any question | Click its numbered button in the Question Navigator |
| Mark for review | Click **☆ Mark** (turns amber in navigator) |
| Unmark | Click **★ Marked** again |
| Clear your answer | Click **✕ Clear** |
| Submit early | Click **SUBMIT EXAM** and confirm in the dialog |

The **timer** in the top-right changes from white → amber (≤60 s) → red (≤30 s).

---

## Submitting

**Manual:** Click **SUBMIT EXAM**. A dialog shows your answered / unanswered / marked counts
and time remaining. Click **Submit Now** to finalise, or **Continue Exam** to return.

**Automatic:** When the timer reaches 00:00 a notification appears and the exam submits automatically.

---

## Results

After submission you see:
- Your percentage score and Pass / Fail status.
- Five metric tiles: Total, Correct, Incorrect, Unanswered, Time Taken.
- Buttons to **VIEW ANSWERS**, **RETAKE EXAM**, **DASHBOARD**, or **LOGOUT**.

Click **VIEW ANSWERS** to open the full question review showing:
- Your selected answer (highlighted red if wrong).
- The correct answer (highlighted green).
- An explanation for each question.

---

## Logging Out

Click **⎋ Logout** on the Dashboard or the Results screen.

If you attempt to log out during an active exam, a safety dialog appears with options to
Continue Exam, Exit & Submit, or Exit & Discard.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| "Database initialization failed" on startup | Ensure the application has write permission to the working directory |
| Login fails with correct password | Delete `data/examsphere.db` to reset seed data |
| Application won't start | Verify Java 21+ is installed: `java -version` |
| `run.bat` says "mvn not found" | Run `mvn clean package` manually with the full Maven path |
