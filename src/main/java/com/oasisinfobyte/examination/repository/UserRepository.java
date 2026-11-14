package com.oasisinfobyte.examination.repository;

import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JDBC-backed repository for User persistence in the SQLite users table.
 */
public class UserRepository {

    private final DatabaseManager dbManager;

    public UserRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        String sql = "SELECT id,username,password_hash,full_name,email,created_at "
                   + "FROM users WHERE LOWER(username)=LOWER(?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.findByUsername failed", e);
        }
        return Optional.empty();
    }

    public User save(User user) {
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        String sql = """
            INSERT INTO users (id,username,password_hash,full_name,email,created_at)
            VALUES (?,?,?,?,?,?)
            ON CONFLICT(username) DO UPDATE SET
              password_hash=excluded.password_hash,
              full_name=excluded.full_name,
              email=excluded.email
            """;
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getCreatedAt() != null
                    ? user.getCreatedAt().toString()
                    : LocalDateTime.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.save failed", e);
        }
        return user;
    }

    public boolean existsByUsername(String username) {
        if (username == null) return false;
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(username)=LOWER(?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.existsByUsername failed", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("full_name"),
                rs.getString("email"),
                parseDateTime(rs.getString("created_at"))
        );
    }

    private LocalDateTime parseDateTime(String s) {
        try { return s != null ? LocalDateTime.parse(s) : LocalDateTime.now(); }
        catch (Exception e) { return LocalDateTime.now(); }
    }
}
