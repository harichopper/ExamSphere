package com.oasisinfobyte.examination.repository;

import java.time.LocalDateTime;

/**
 * Lightweight record capturing a persisted exam attempt summary.
 */
public record AttemptRecord(
        long id,
        String username,
        String examTitle,
        int score,
        int totalQuestions,
        double percentage,
        boolean passed,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
