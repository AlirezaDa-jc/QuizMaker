package ir.maktab.quizmaker.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Student extends User {
    @Column(nullable = false,unique = true)
    private int studentCode;

    private String firstName;

    private String lastName;

    @Column(nullable = false,unique = true)
    private long nationalCode;

    @ManyToMany(mappedBy = "students")
    private List<Exam> exams = new LinkedList<>();
/*
Performance
 */
//        @ManyToMany(fetch = FetchType.EAGER,mappedBy = "students")
    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new LinkedList<>();

    public Student() {
        setRole("STUDENT");
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
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

    public long getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(long nationalCode) {
        this.nationalCode = nationalCode;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.removeStudent(this);
    }
}
