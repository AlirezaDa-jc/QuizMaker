package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Exam extends BaseEntity<Long> {
    private String title;

    private String description;

    private int time;

    @ManyToMany
    @JoinTable(name = "Question_Exam",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "exam_id")})
    private List<Question> questions = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
