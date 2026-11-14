package com.oasisinfobyte.examination.repository;

import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.Answer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JDBC-backed repository for persisting and loading exam attempts.
 */
public class AttemptRepository {

    private final DatabaseManager dbManager;

    public AttemptRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Saves an attempt and returns the generated attempt ID.
     */
    public long saveAttempt(AttemptRecord attempt) throws SQLException {
        String sql = "INSERT INTO attempts "
                + "(username,exam_title,score,total_questions,percentage,passed,start_time,end_time) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, attempt.username());
            ps.setString(2, attempt.examTitle());
            ps.setInt(3, attempt.score());
            ps.setInt(4, attempt.totalQuestions());
            ps.setDouble(5, attempt.percentage());
            ps.setInt(6, attempt.passed() ? 1 : 0);
            ps.setString(7, attempt.startTime() != null
                    ? attempt.startTime().toString() : LocalDateTime.now().toString());
            ps.setString(8, attempt.endTime() != null
                    ? attempt.endTime().toString() : LocalDateTime.now().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1L;
            }
        }
    }

    /**
     * Saves all per-question answers for a given attempt ID.
     */
    public void saveAnswers(long attemptId, Map<Integer, Answer> answers) throws SQLException {
        String sql = "INSERT INTO attempt_answers (attempt_id,question_idx,answer) VALUES (?,?,?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            for (Map.Entry<Integer, Answer> entry : answers.entrySet()) {
                ps.setLong(1, attemptId);
                ps.setInt(2, entry.getKey());
                ps.setString(3, entry.getValue() != null ? entry.getValue().name() : null);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Returns the most recent attempt for the given username, or empty if none exists.
     */
    public Optional<AttemptRecord> findMostRecentAttempt(String username) throws SQLException {
        String sql = "SELECT id,username,exam_title,score,total_questions,percentage,"
                + "passed,start_time,end_time FROM attempts "
                + "WHERE username=? ORDER BY end_time DESC LIMIT 1";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    private AttemptRecord mapRow(ResultSet rs) throws SQLException {
        return new AttemptRecord(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("exam_title"),
                rs.getInt("score"),
                rs.getInt("total_questions"),
                rs.getDouble("percentage"),
                rs.getInt("passed") == 1,
                parseDateTime(rs.getString("start_time")),
                parseDateTime(rs.getString("end_time"))
        );
    }

    private LocalDateTime parseDateTime(String s) {
        try { return s != null ? LocalDateTime.parse(s) : LocalDateTime.now(); }
        catch (Exception e) { return LocalDateTime.now(); }
    }
}
