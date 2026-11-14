package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.model.User;

import java.util.Optional;

/**
 * Thread-safe singleton session manager tracking currently authenticated user,
 * active exam instance, and latest exam result.
 */
public class SessionManager {
    private static volatile SessionManager instance;

    private User currentUser;
    private Exam currentExam;
    private ExamResult latestResult;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public synchronized void setLoggedInUser(User user) {
        this.currentUser = user;
    }

    public synchronized Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public synchronized void setCurrentExam(Exam exam) {
        this.currentExam = exam;
    }

    public synchronized Optional<Exam> getCurrentExam() {
        return Optional.ofNullable(currentExam);
    }

    public synchronized void setLatestResult(ExamResult result) {
        this.latestResult = result;
    }

    public synchronized Optional<ExamResult> getLatestResult() {
        return Optional.ofNullable(latestResult);
    }

    public synchronized void clearExamSession() {
        this.currentExam = null;
    }

    public synchronized void clear() {
        this.currentUser = null;
        this.currentExam = null;
        this.latestResult = null;
    }
}
