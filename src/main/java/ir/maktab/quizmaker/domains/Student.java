package ir.maktab.quizmaker.domains;

import javax.persistence.*;
import java.util.*;

/**
 * @author Alireza.d.a
 */
@Entity
public class Student extends User {
    @Column(nullable = false,unique = true)
    private Integer studentCode;

    private String firstName;

    private String lastName;


    @Column(nullable = false,unique = true)
    private Long nationalCode;

    @OneToMany(mappedBy = "student" , orphanRemoval = true)
    Set<StudentQuestionScore> scores = new HashSet<>();


    @ManyToMany(mappedBy = "students",fetch = FetchType.EAGER)
    private List<Exam> exams = new ArrayList<>();

    @ManyToMany(mappedBy = "students",fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

    public Student() {
        setRole("STUDENT");
    }

    public Set<StudentQuestionScore> getScores() {
        return scores;
    }

    public void setScores(Set<StudentQuestionScore> scores) {
        this.scores = scores;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public Integer getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(Integer studentCode) {
        this.studentCode = studentCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(Long nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.removeStudent(this);
    }

    public void addExam(Exam exam) {
        exams.add(exam);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentCode.equals(student.studentCode) &&
                nationalCode.equals(student.nationalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentCode, nationalCode);
    }
}
