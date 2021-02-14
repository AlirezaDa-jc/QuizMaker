package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Alireza.d.a
 */
public class StudentQuestionScoreDTO {
    @NotNull(message = "Score is mandatory")
    @Valid
    private int score;

    @NotEmpty(message = "Answer is mandatory")
    @Valid
    private String answer;

    private StudentDTO student;

    private QuestionExamScoreDTO questionExamScore;

    public StudentQuestionScoreDTO() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public QuestionExamScoreDTO getQuestionExamScore() {
        return questionExamScore;
    }

    public void setQuestionExamScore(QuestionExamScoreDTO questionExamScore) {
        this.questionExamScore = questionExamScore;
    }
}
