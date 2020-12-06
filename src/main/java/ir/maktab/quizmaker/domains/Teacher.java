package ir.maktab.quizmaker.domains;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Teacher extends User {

    private String firstName;
    private String lastName;
    private long nationalCode;

    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    private List<Course> courses = new LinkedList<>();

    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    private List<Question> questions = new LinkedList<>();

    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    private List<Exam> exams = new LinkedList<>();

    public Teacher() {
        setRole("TEACHER");
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.removeTeacher();
    }
}
