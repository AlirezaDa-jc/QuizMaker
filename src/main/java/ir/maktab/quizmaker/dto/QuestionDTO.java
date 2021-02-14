package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
public class QuestionDTO {
    List<QuestionExamScoreDTO> scores = new ArrayList<>();
    @NotEmpty(message = "Title is mandatory")
    @Valid
    private String title;
    @NotEmpty(message = "Question is mandatory")
    @Valid
    private String question;
    private Boolean isPublic = false;
    @NotEmpty(message = "Answer is mandatory")
    @Valid
    private String answer;
    private TeacherDTO teacher;

    private CourseDTO course;

    private Set<SubjectDTO> subjects = new HashSet<>();

    public QuestionDTO() {
    }

    public QuestionDTO(ExamDTO exam) {
        course = exam.getCourse();
        teacher = exam.getTeacher();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<QuestionExamScoreDTO> getScores() {
        return scores;
    }

    public void setScores(List<QuestionExamScoreDTO> scores) {
        this.scores = scores;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public Set<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectDTO> subjects) {
        this.subjects = subjects;
    }
}
