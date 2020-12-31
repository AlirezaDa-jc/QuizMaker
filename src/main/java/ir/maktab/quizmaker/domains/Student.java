package ir.maktab.quizmaker.domains;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Exam> exams = new HashSet<>();

    @ManyToMany(mappedBy = "students",fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

    public Student() {
        setRole("STUDENT");
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public void setExams(Set<Exam> exams) {
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

}
