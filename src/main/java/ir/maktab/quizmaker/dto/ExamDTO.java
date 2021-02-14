package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class ExamDTO {
    List<QuestionExamScoreDTO> scores = new LinkedList<>();
    @NotEmpty(message = "Title is mandatory")
    @Valid
    private String title;
    @NotEmpty(message = "Description is mandatory")
    @Valid
    private String description;
//    @NotEmpty(message = "Time is mandatory")
    @Valid
    private int time;
    private boolean isAvailable = false;
    private Set<StudentDTO> students = new HashSet<>();


    private CourseDTO course;

    private TeacherDTO teacher;

    public ExamDTO() {
    }

    public ExamDTO(CourseDTO course, TeacherDTO teacher) {
        this.course = course;
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<QuestionExamScoreDTO> getScores() {
        return scores;
    }

    public void setScores(List<QuestionExamScoreDTO> scores) {
        this.scores = scores;
    }

    public Set<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDTO> students) {
        this.students = students;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }
}
