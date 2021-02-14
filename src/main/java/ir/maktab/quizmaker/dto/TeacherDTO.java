package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class TeacherDTO extends UserDTO {
    @NotEmpty(message = "FirstName is mandatory")
    @Valid
    private String firstName;
    @NotEmpty(message = "LastName is mandatory")
    @Valid
    private String lastName;
    @NotNull(message = "NationalCode is mandatory")
    @Valid
    private Long nationalCode;


    private Set<CourseDTO> courses = new HashSet<>();

    private Set<QuestionDTO> questions = new HashSet<>();

    private Set<ExamDTO> exams = new HashSet<>();

    public TeacherDTO() {
        setRole("TEACHER");
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

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    public Set<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionDTO> questions) {
        this.questions = questions;
    }

    public Set<ExamDTO> getExams() {
        return exams;
    }

    public void setExams(Set<ExamDTO> exams) {
        this.exams = exams;
    }
    public void addCourse(CourseDTO course) {
        courses.add(course);
    }

    public void removeCourse(CourseDTO course) {
        courses.remove(course);
//        course.removeTeacher();
    }
}
