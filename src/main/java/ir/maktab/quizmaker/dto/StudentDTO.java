package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class StudentDTO extends UserDTO {
    Set<StudentQuestionScoreDTO> scores = new HashSet<>();
    @NotNull(message = "Student Code is mandatory")
    @Valid
    private Integer studentCode;
    @NotEmpty(message = "First Name is mandatory")
    @Valid
    private String firstName;
    @NotEmpty(message = "LastName is mandatory")
    @Valid
    private String lastName;
    @NotNull(message = "National Code is mandatory")
    @Valid
    private Long nationalCode;
    private List<ExamDTO> exams = new ArrayList<>();

    private Set<CourseDTO> courses = new HashSet<>();

    public StudentDTO() {
        setRole("STUDENT");
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

    public Set<StudentQuestionScoreDTO> getScores() {
        return scores;
    }

    public void setScores(Set<StudentQuestionScoreDTO> scores) {
        this.scores = scores;
    }

    public List<ExamDTO> getExams() {
        return exams;
    }

    public void setExams(List<ExamDTO> exams) {
        this.exams = exams;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    public void removeCourse(CourseDTO course) {
        courses.remove(course);
//        course.removeStudent(this);
    }
}
