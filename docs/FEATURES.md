# ExamSphere — Feature Checklist

## Mandatory Internship Requirements

- [x] Login screen with username and password
- [x] Profile update (display name, email) before examination
- [x] Multiple-choice questions displayed one at a time
- [x] Four radio-button options per question (A, B, C, D)
- [x] Next and Previous question navigation
- [x] Visible countdown timer (default 30 minutes)
- [x] Automatic submission when countdown reaches zero
- [x] Manual submission with confirmation dialog
- [x] Result screen: score, time taken, correct/incorrect breakdown
- [x] Confirmation dialog on window close during active exam
- [x] Logout returning to login screen

## Enhanced Features

### Security
- [x] PBKDF2WithHmacSHA256 password hashing (random salt, 310 000 iterations)
- [x] Constant-time password comparison via `MessageDigest.isEqual`
- [x] Change password with current-password verification
- [x] JDBC prepared statements throughout (SQL injection prevention)

### Persistence
- [x] SQLite database (`data/examsphere.db`) auto-created on first run
- [x] User accounts persisted across restarts
- [x] Exam attempts persisted with per-question answers
- [x] Last score shown on Dashboard on login

### Exam UX
- [x] Exam Instructions panel before timer starts
- [x] Mark for Review toggle (amber indicator)
- [x] Clear Answer button
- [x] Question Navigator grid (5 visual states)
- [x] Progress label showing marked-for-review count
- [x] AtomicBoolean guard prevents duplicate submission
- [x] Close-window guard with three options during active exam

### Question Bank
- [x] 40+ questions across 8 Java categories
- [x] Each question has explanation shown in answer review
- [x] Default exam uses 30 deterministic questions

### UI
- [x] Branded Splash Screen with SwingWorker DB init
- [x] Modern dark-theme design system
- [x] Rounded cards, buttons, and input fields
- [x] Three timer visual states (normal / warning / critical)
- [x] Time Taken metric tile on results screen
- [x] Full question-by-question answer review dialog
