package com.oasisinfobyte.examination.database;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Question;
import com.oasisinfobyte.examination.util.PasswordUtil;
import com.oasisinfobyte.examination.util.QuestionBank;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates the ExamSphere SQLite schema and inserts seed data on first run.
 */
public class DatabaseInitializer {

    private final DatabaseManager dbManager;

    public DatabaseInitializer(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void initialize() throws SQLException {
        createTables();
        seedUsers();
        seedQuestions();
        seedDefaultExam();
    }

    private void createTables() throws SQLException {
        Connection conn = dbManager.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id            TEXT PRIMARY KEY,
                    username      TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    full_name     TEXT NOT NULL DEFAULT '',
                    email         TEXT NOT NULL DEFAULT '',
                    created_at    TEXT NOT NULL
                )""");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS exams (
                    id               INTEGER PRIMARY KEY AUTOINCREMENT,
                    title            TEXT    NOT NULL,
                    duration_minutes INTEGER NOT NULL DEFAULT 30,
                    question_ids     TEXT    NOT NULL
                )""");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS questions (
                    id            INTEGER PRIMARY KEY,
                    question_text TEXT    NOT NULL,
                    option_a      TEXT    NOT NULL,
                    option_b      TEXT    NOT NULL,
                    option_c      TEXT    NOT NULL,
                    option_d      TEXT    NOT NULL,
                    correct_index INTEGER NOT NULL,
                    explanation   TEXT    NOT NULL DEFAULT '',
                    topic         TEXT    NOT NULL DEFAULT 'General'
                )""");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS attempts (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    username        TEXT    NOT NULL,
                    exam_title      TEXT    NOT NULL,
                    score           INTEGER NOT NULL,
                    total_questions INTEGER NOT NULL,
                    percentage      REAL    NOT NULL,
                    passed          INTEGER NOT NULL,
                    start_time      TEXT    NOT NULL,
                    end_time        TEXT    NOT NULL
                )""");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS attempt_answers (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    attempt_id   INTEGER NOT NULL REFERENCES attempts(id),
                    question_idx INTEGER NOT NULL,
                    answer       TEXT
                )""");
        }
    }

    private void seedUsers() throws SQLException {
        Connection conn = dbManager.getConnection();
        try (Statement check = conn.createStatement();
             ResultSet rs = check.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.getInt(1) > 0) return;
        }
        String sql = "INSERT INTO users (id,username,password_hash,full_name,email,created_at) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            insertUser(ps, "student", "student123", "Student Demo",  "student@examsphere.com");
            insertUser(ps, "admin",   "admin123",   "Administrator", "admin@examsphere.com");
        }
    }

    private void insertUser(PreparedStatement ps, String username, String plainPassword,
                             String fullName, String email) throws SQLException {
        ps.setString(1, UUID.randomUUID().toString());
        ps.setString(2, username);
        ps.setString(3, PasswordUtil.hashPassword(plainPassword));
        ps.setString(4, fullName);
        ps.setString(5, email);
        ps.setString(6, LocalDateTime.now().toString());
        ps.executeUpdate();
    }

    private void seedQuestions() throws SQLException {
        Connection conn = dbManager.getConnection();
        try (Statement check = conn.createStatement();
             ResultSet rs = check.executeQuery("SELECT COUNT(*) FROM questions")) {
            if (rs.getInt(1) > 0) return;
        }
        String sql = """
            INSERT INTO questions
              (id,question_text,option_a,option_b,option_c,option_d,correct_index,explanation,topic)
            VALUES (?,?,?,?,?,?,?,?,?)""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Question q : QuestionBank.getDefaultQuestions()) {
                Map<Answer, String> opts = q.getOptions();
                ps.setInt(1,    q.getId());
                ps.setString(2, q.getQuestionText());
                ps.setString(3, opts.get(Answer.A));
                ps.setString(4, opts.get(Answer.B));
                ps.setString(5, opts.get(Answer.C));
                ps.setString(6, opts.get(Answer.D));
                ps.setInt(7,    q.getCorrectAnswer().getIndex());
                ps.setString(8, q.getExplanation());
                ps.setString(9, q.getTopic());
                ps.executeUpdate();
            }
        }
    }

    private void seedDefaultExam() throws SQLException {
        Connection conn = dbManager.getConnection();
        try (Statement check = conn.createStatement();
             ResultSet rs = check.executeQuery("SELECT COUNT(*) FROM exams")) {
            if (rs.getInt(1) > 0) return;
        }
        // Collect up to 30 question IDs
        List<Integer> ids = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id FROM questions ORDER BY id LIMIT 30")) {
            while (rs.next()) ids.add(rs.getInt(1));
        }
        String questionIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        String sql = "INSERT INTO exams (title,duration_minutes,question_ids) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "Java Comprehensive Assessment");
            ps.setInt(2, 30);
            ps.setString(3, questionIds);
            ps.executeUpdate();
        }
    }
}
