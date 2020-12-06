package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Student extends BaseEntity<Long> {
    @Column(nullable = false,unique = true)
    private int studentCode;

    private String firstName;

    private String lastName;

    @Column(nullable = false,unique = true)
    private long nationalCode;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "students")
    private List<Course> courses = new LinkedList<>();

    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setStudent(this);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
}
