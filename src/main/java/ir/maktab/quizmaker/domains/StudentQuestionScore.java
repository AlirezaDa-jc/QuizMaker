package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Alireza.d.a
 */
@Entity
public class StudentQuestionScore extends BaseEntity<Long> {

    private int score;

    private String answer;

    @ManyToOne
    private Student student;

    @ManyToOne
    private QuestionExamScore questionExamScore;

    public StudentQuestionScore() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public QuestionExamScore getQuestionExamScore() {
        return questionExamScore;
    }

    public void setQuestionExamScore(QuestionExamScore questionExamScore) {
        this.questionExamScore = questionExamScore;
    }
}
