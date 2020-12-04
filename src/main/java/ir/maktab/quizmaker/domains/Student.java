package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Student extends BaseEntity<Long> {
    private int studentCode;
    private String firstName;
    private String lastName;
    private long nationalCode;

    @ManyToMany(mappedBy = "students")
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
    }
}
