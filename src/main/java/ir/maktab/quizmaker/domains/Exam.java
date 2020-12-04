package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Alireza.d.a
 */
@Entity
public class Exam extends BaseEntity<Long> {
    private String title;

    private String description;

    private int time;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

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
