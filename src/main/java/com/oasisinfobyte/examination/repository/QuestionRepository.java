package com.oasisinfobyte.examination.repository;

import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Question;

import java.sql.*;
import java.util.*;

/**
 * JDBC-backed repository for Question persistence in the SQLite questions table.
 */
public class QuestionRepository {

    private final DatabaseManager dbManager;

    public QuestionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Question> findAll() {
        String sql = "SELECT id,question_text,option_a,option_b,option_c,option_d,"
                   + "correct_index,explanation,topic FROM questions ORDER BY id";
        List<Question> result = new ArrayList<>();
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("QuestionRepository.findAll failed", e);
        }
        return Collections.unmodifiableList(result);
    }

    public Optional<Question> findById(int id) {
        String sql = "SELECT id,question_text,option_a,option_b,option_c,option_d,"
                   + "correct_index,explanation,topic FROM questions WHERE id=?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("QuestionRepository.findById failed", e);
        }
        return Optional.empty();
    }

    public int count() {
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM questions")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("QuestionRepository.count failed", e);
        }
    }

    private Question mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String text = rs.getString("question_text");
        List<String> options = List.of(
                rs.getString("option_a"),
                rs.getString("option_b"),
                rs.getString("option_c"),
                rs.getString("option_d")
        );
        int correctIdx = rs.getInt("correct_index");
        String explanation = rs.getString("explanation");
        String topic = rs.getString("topic");
        return new Question(id, text, options, correctIdx, explanation, topic);
    }
}
