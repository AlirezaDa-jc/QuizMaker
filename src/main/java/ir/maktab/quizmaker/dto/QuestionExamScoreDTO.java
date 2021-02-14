package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class QuestionExamScoreDTO {
    Set<StudentQuestionScoreDTO> scores = new HashSet<>();
    @NotNull(message = "Score is mandatory")
    @Valid
    private int score;
    private ExamDTO exam;
    private QuestionDTO question;
    public QuestionExamScoreDTO() {
    }

    public QuestionExamScoreDTO(int score, ExamDTO exam, QuestionDTO question) {
        this.score = score;
        this.exam = exam;
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public Set<StudentQuestionScoreDTO> getScores() {
        return scores;
    }

    public void setScores(Set<StudentQuestionScoreDTO> scores) {
        this.scores = scores;
    }
}
