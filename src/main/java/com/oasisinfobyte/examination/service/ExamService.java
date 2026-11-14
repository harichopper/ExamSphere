package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.Question;
import com.oasisinfobyte.examination.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service managing exam lifecycles, navigation, and answering.
 */
public class ExamService {
    private final QuestionRepository questionRepository;
    private final SessionManager sessionManager;

    public ExamService(QuestionRepository questionRepository, SessionManager sessionManager) {
        this.questionRepository = questionRepository;
        this.sessionManager = sessionManager;
    }

    public Exam startNewExam(String examTitle, int durationMinutes) {
        List<Question> questions = questionRepository.findAll();
        Exam exam = new Exam(examTitle, questions, durationMinutes);
        exam.start();
        sessionManager.setCurrentExam(exam);
        return exam;
    }

    public void selectAnswer(int questionIndex, Answer answer) {
        Exam exam = getActiveExam();
        exam.setAnswer(questionIndex, answer);
    }

    public Optional<Answer> getAnswer(int questionIndex) {
        Exam exam = getActiveExam();
        return exam.getAnswer(questionIndex);
    }

    public boolean nextQuestion() {
        Exam exam = getActiveExam();
        return exam.nextQuestion();
    }

    public boolean previousQuestion() {
        Exam exam = getActiveExam();
        return exam.previousQuestion();
    }

    public void jumpToQuestion(int questionIndex) {
        Exam exam = getActiveExam();
        exam.setCurrentQuestionIndex(questionIndex);
    }

    public void submitExam() {
        Exam exam = getActiveExam();
        if (!exam.isSubmitted()) {
            exam.submit();
        }
    }

    private Exam getActiveExam() {
        return sessionManager.getCurrentExam()
                .orElseThrow(() -> new IllegalStateException("No active exam session available"));
    }
}
