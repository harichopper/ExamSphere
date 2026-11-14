package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.model.Question;
import com.oasisinfobyte.examination.service.ExamService;
import com.oasisinfobyte.examination.service.ResultService;
import com.oasisinfobyte.examination.service.SessionManager;
import com.oasisinfobyte.examination.ui.components.QuestionCard;
import com.oasisinfobyte.examination.ui.components.QuestionNavigator;
import com.oasisinfobyte.examination.ui.components.RoundedButton;
import com.oasisinfobyte.examination.ui.components.RoundedPanel;
import com.oasisinfobyte.examination.ui.components.TimerPanel;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Live examination workspace — 30-minute timer, mark-for-review, clear-answer,
 * manual/auto submission, AtomicBoolean guard against duplicate finalisation.
 */
public class ExamPanel extends JPanel {

    private static final int DEFAULT_DURATION_SECONDS = 1800; // 30 minutes
    private static final int DEFAULT_DURATION_MINUTES = 30;

    private final ExamService examService;
    private final ResultService resultService;
    private final SessionManager sessionManager;
    private final Consumer<ExamResult> onExamFinished;

    private TimerPanel timerPanel;
    private QuestionCard questionCard;
    private QuestionNavigator questionNavigator;
    private JLabel progressTextLabel;
    private RoundedButton prevButton;
    private RoundedButton nextButton;
    private RoundedButton markReviewButton;
    private RoundedButton clearAnswerButton;
    private RoundedButton submitButton;

    private final AtomicBoolean submitted = new AtomicBoolean(false);

    public ExamPanel(ExamService examService, ResultService resultService,
                     SessionManager sessionManager, Consumer<ExamResult> onExamFinished) {
        this.examService    = examService;
        this.resultService  = resultService;
        this.sessionManager = sessionManager;
        this.onExamFinished = onExamFinished;

        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));
        initUI();
        initKeyboardShortcuts();
    }

    // ── UI construction ───────────────────────────────────────────────────────

    private void initUI() {
        // Header: title + timer
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);

        JPanel leftMeta = new JPanel();
        leftMeta.setLayout(new BoxLayout(leftMeta, BoxLayout.Y_AXIS));
        leftMeta.setOpaque(false);
        JLabel titleLabel = new JLabel("Java Comprehensive Assessment");
        titleLabel.setFont(UIConstants.FONT_HEADER);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        progressTextLabel = new JLabel("Question 1 of 30");
        progressTextLabel.setFont(UIConstants.FONT_SMALL);
        progressTextLabel.setForeground(UIConstants.TEXT_SECONDARY);
        leftMeta.add(titleLabel);
        leftMeta.add(Box.createVerticalStrut(4));
        leftMeta.add(progressTextLabel);
        topHeader.add(leftMeta, BorderLayout.WEST);

        timerPanel = new TimerPanel(DEFAULT_DURATION_SECONDS);
        timerPanel.setExpirationListener(this::handleAutoSubmission);
        topHeader.add(timerPanel, BorderLayout.EAST);
        add(topHeader, BorderLayout.NORTH);

        // Main content: question card (left) + sidebar (right)
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.72; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 16);

        questionCard = new QuestionCard();
        questionCard.setOnAnswerSelected(ans -> {
            if (!submitted.get()) {
                sessionManager.getCurrentExam().ifPresent(exam -> {
                    examService.selectAnswer(exam.getCurrentQuestionIndex(), ans);
                    refreshUI();
                });
            }
        });
        mainContent.add(questionCard, gbc);

        // Sidebar
        gbc.gridx = 1; gbc.weightx = 0.28; gbc.insets = new Insets(0, 0, 0, 0);
        RoundedPanel sidebar = new RoundedPanel(12, UIConstants.BG_CARD);
        sidebar.setLayout(new BorderLayout(0, 12));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel navTitle = new JLabel("Question Navigator");
        navTitle.setFont(UIConstants.FONT_BODY_BOLD);
        navTitle.setForeground(UIConstants.TEXT_PRIMARY);
        sidebar.add(navTitle, BorderLayout.NORTH);

        questionNavigator = new QuestionNavigator(30);
        questionNavigator.setOnQuestionClicked(idx -> {
            if (!submitted.get()) {
                examService.jumpToQuestion(idx);
                loadQuestionUI();
            }
        });
        sidebar.add(questionNavigator, BorderLayout.CENTER);
        sidebar.add(buildLegend(), BorderLayout.SOUTH);
        mainContent.add(sidebar, gbc);
        add(mainContent, BorderLayout.CENTER);

        // Bottom bar: nav buttons left, submit right
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        JPanel navGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navGroup.setOpaque(false);

        prevButton = RoundedButton.createOutline("← Previous");
        prevButton.addActionListener(e -> { if (examService.previousQuestion()) loadQuestionUI(); });
        navGroup.add(prevButton);

        nextButton = RoundedButton.createPrimary("Next →");
        nextButton.addActionListener(e -> { if (examService.nextQuestion()) loadQuestionUI(); });
        navGroup.add(nextButton);

        clearAnswerButton = RoundedButton.createOutline("✕ Clear");
        clearAnswerButton.addActionListener(e -> handleClearAnswer());
        navGroup.add(clearAnswerButton);

        markReviewButton = new RoundedButton("☆ Mark", UIConstants.COLOR_WARNING,
                UIConstants.COLOR_WARNING.darker(), UIConstants.TEXT_PRIMARY, 10);
        markReviewButton.addActionListener(e -> handleMarkForReview());
        navGroup.add(markReviewButton);

        bottomBar.add(navGroup, BorderLayout.WEST);

        submitButton = RoundedButton.createDanger("SUBMIT EXAM");
        submitButton.addActionListener(e -> confirmAndSubmit());
        bottomBar.add(submitButton, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        legend.setOpaque(false);
        legend.add(legendRow(UIConstants.ACCENT_PRIMARY,  "Current"));
        legend.add(Box.createVerticalStrut(3));
        legend.add(legendRow(UIConstants.COLOR_SUCCESS,   "Answered"));
        legend.add(Box.createVerticalStrut(3));
        legend.add(legendRow(UIConstants.COLOR_WARNING,   "Marked for Review"));
        legend.add(Box.createVerticalStrut(3));
        legend.add(legendRow(UIConstants.BG_ELEVATED,     "Unanswered"));
        return legend;
    }

    private JPanel legendRow(Color color, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 1));
        row.setOpaque(false);
        RoundedPanel dot = new RoundedPanel(4, color);
        dot.setPreferredSize(new Dimension(12, 12));
        row.add(dot);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_MUTED);
        row.add(lbl);
        return row;
    }

    private void initKeyboardShortcuts() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (submitted.get()) return;
                if      (e.getKeyCode() == KeyEvent.VK_LEFT)                             { if (examService.previousQuestion()) loadQuestionUI(); }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)                            { if (examService.nextQuestion())     loadQuestionUI(); }
                else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER)       { confirmAndSubmit(); }
            }
        });
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void startExam() {
        submitted.set(false);
        examService.startNewExam("Java Comprehensive Assessment", DEFAULT_DURATION_MINUTES);
        timerPanel.reset(DEFAULT_DURATION_SECONDS);
        timerPanel.start();
        questionCard.setOptionsEnabled(true);
        submitButton.setEnabled(true);
        clearAnswerButton.setEnabled(true);
        markReviewButton.setEnabled(true);
        loadQuestionUI();
        requestFocusInWindow();
    }

    // ── Handlers ─────────────────────────────────────────────────────────────

    private void handleClearAnswer() {
        if (submitted.get()) return;
        sessionManager.getCurrentExam().ifPresent(exam -> {
            exam.clearAnswer(exam.getCurrentQuestionIndex());
            loadQuestionUI();
        });
    }

    private void handleMarkForReview() {
        if (submitted.get()) return;
        sessionManager.getCurrentExam().ifPresent(exam -> {
            int idx = exam.getCurrentQuestionIndex();
            if (exam.isMarkedForReview(idx)) {
                exam.clearReviewMark(idx);
                markReviewButton.setText("☆ Mark");
            } else {
                exam.markForReview(idx);
                markReviewButton.setText("★ Marked");
            }
            refreshUI();
        });
    }

    private void confirmAndSubmit() {
        if (submitted.get()) return;
        sessionManager.getCurrentExam().ifPresent(exam -> {
            int answered = exam.getAllAnswers().size();
            int total    = exam.getQuestions().size();
            int marked   = exam.getMarkedForReview().size();
            int remaining = timerPanel.getRemainingSeconds();
            int mins = remaining / 60, secs = remaining % 60;

            String msg = String.format(
                    "<html>Submit your exam?<br><br>"
                    + "Answered: <b>%d</b> of <b>%d</b><br>"
                    + "Unanswered: <b>%d</b><br>"
                    + "Marked for review: <b>%d</b><br>"
                    + "Time remaining: <b>%02d:%02d</b>"
                    + (answered < total ? "<br><span style='color:#F59E0B;'>Unanswered questions score 0.</span>" : "")
                    + "</html>",
                    answered, total, total - answered, marked, mins, secs);

            Object[] opts = {"Submit Now", "Continue Exam"};
            int choice = JOptionPane.showOptionDialog(this, msg, "Confirm Submission",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[1]);
            if (choice == 0) finalizeSubmission(false);
        });
    }

    private void handleAutoSubmission() {
        if (submitted.get()) return;
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "<html><b>Time's up!</b><br>Your exam has been automatically submitted.</html>",
                    "Time Expired", JOptionPane.WARNING_MESSAGE);
            finalizeSubmission(true);
        });
    }

    private synchronized void finalizeSubmission(boolean isAuto) {
        if (!submitted.compareAndSet(false, true)) return;

        timerPanel.stop();
        questionCard.setOptionsEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        clearAnswerButton.setEnabled(false);
        markReviewButton.setEnabled(false);
        submitButton.setEnabled(false);

        examService.submitExam();
        sessionManager.getCurrentExam().ifPresent(exam -> {
            ExamResult result = resultService.calculateResult(exam);
            if (onExamFinished != null) onExamFinished.accept(result);
        });
    }

    // ── UI refresh ────────────────────────────────────────────────────────────

    private void loadQuestionUI() {
        sessionManager.getCurrentExam().ifPresent(exam -> {
            int idx   = exam.getCurrentQuestionIndex();
            int total = exam.getQuestions().size();
            Question q = exam.getCurrentQuestion();
            Answer sel = exam.getAnswer(idx).orElse(null);

            int marked = exam.getMarkedForReview().size();
            String progress = "Question " + (idx + 1) + " of " + total;
            if (marked > 0) progress += "  |  ★ " + marked + " marked";
            progressTextLabel.setText(progress);

            questionCard.displayQuestion(idx + 1, q, sel);
            prevButton.setEnabled(idx > 0);
            nextButton.setEnabled(idx < total - 1);
            markReviewButton.setText(exam.isMarkedForReview(idx) ? "★ Marked" : "☆ Mark");

            refreshUI(exam);
        });
    }

    private void refreshUI() { sessionManager.getCurrentExam().ifPresent(this::refreshUI); }

    private void refreshUI(Exam exam) {
        questionNavigator.updateState(
                exam.getCurrentQuestionIndex(),
                exam.getAllAnswers(),
                exam.getMarkedForReview()
        );
    }
}
