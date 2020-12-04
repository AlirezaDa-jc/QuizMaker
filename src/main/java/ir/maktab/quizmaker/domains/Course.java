package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Course extends BaseEntity<Long> {


    private String startDate;

    private String endDate;

    @ManyToOne
    @JoinColumn(name = "subjectid")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private User teacher;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<Exam> exams = new LinkedList<>();

    @OneToMany(mappedBy = "course" , orphanRemoval = true)
    List<Question> questions = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "Student_Course",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private List<Student> students = new LinkedList<>();

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
