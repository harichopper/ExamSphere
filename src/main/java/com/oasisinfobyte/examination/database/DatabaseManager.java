package com.oasisinfobyte.examination.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the SQLite JDBC connection lifecycle for ExamSphere.
 * Implements AutoCloseable for use in try-with-resources.
 */
public class DatabaseManager implements AutoCloseable {
    private final Connection connection;

    /**
     * Opens (or creates) the SQLite database file at the given path.
     * Creates parent directories if they do not exist.
     *
     * @param dbFilePath path to the .db file (e.g. "data/examsphere.db")
     * @throws SQLException if the JDBC connection cannot be established
     */
    public DatabaseManager(String dbFilePath) throws SQLException {
        try {
            Path path = Paths.get(dbFilePath);
            Files.createDirectories(path.getParent());
        } catch (Exception e) {
            throw new SQLException("Cannot create database directory for: " + dbFilePath, e);
        }
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        // Enable foreign-key enforcement
        try (var stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    /** Returns the active JDBC Connection. */
    public Connection getConnection() {
        return connection;
    }

    /** Closes the underlying JDBC connection. */
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
