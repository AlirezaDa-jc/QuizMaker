package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Alireza.d.a
 */
@Entity
public class Question extends BaseEntity<Long> {
    private String title;

    private String question;

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "subjectid")
    private Subject subject;

//    private Blob Baraye Image . Sakhtan !

//    private List<String> options = new LinkedList<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
