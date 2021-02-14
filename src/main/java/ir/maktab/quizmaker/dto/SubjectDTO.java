package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class SubjectDTO {
    Set<CourseDTO> courses = new HashSet<>();
    Set<QuestionDTO> questions = new HashSet<>();
    @NotEmpty(message = "Name is mandatory")
    @Valid
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public void addQuestion(QuestionDTO question) {
        questions.add(question);
    }
    public void addCourse(CourseDTO courseDTO) {
        courses.add(courseDTO);
    }
}
