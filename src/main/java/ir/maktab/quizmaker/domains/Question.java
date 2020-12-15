package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Entity
public class Question extends BaseEntity<Long> {
    private String title;

    private String question;

    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private Teacher teacher;


    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "subjectid")
    private Subject subject;

    @ManyToMany(mappedBy = "questions")
    private Set<Exam> exams = new HashSet<>();

    public Question() {
    }

    public Question(Exam exam) {
        exams.add(exam);
        subject = exam.getCourse().getSubject();
        course = exam.getCourse();
        teacher = exam.getTeacher();
    }

//    private Blob Baraye Image . Sakhtan !



    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public void setExams(Set<Exam> exams) {
        this.exams = exams;
    }

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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
        course.addQuestion(this);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        if(isPublic)
            subject.addQuestion(this);
    }
}
