package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Entity
public class QuestionExamScore extends BaseEntity<Long> {
//Junction Or Tree*
    private int score;

    @ManyToOne
    @JoinColumn(name = "examid")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "questionid")
    private Question question;

    @OneToMany(mappedBy = "questionExamScore" , orphanRemoval = true)
    Set<StudentQuestionScore> scores = new HashSet<>();

    public QuestionExamScore() {
    }

    public QuestionExamScore(int score, Exam exam, Question question) {
        this.score = score;
        this.exam = exam;
        this.question = question;
    }

    public QuestionExamScore(Question question, Exam exam) {
        this.question = question;
        this.exam = exam;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        exam.addQuestion(this);
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        question.addScores(this);
    }
    public void addScoreStudent(StudentQuestionScore studentQuestionScore) {
        scores.add(studentQuestionScore);
    }
}
