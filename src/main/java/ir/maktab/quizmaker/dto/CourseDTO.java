package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */

public class CourseDTO {
    Set<QuestionDTO> questions = new HashSet<>();
    @NotEmpty(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    @Valid
    private String name;
    @NotEmpty(message = "StartDate is mandatory")
    @NotNull(message = "Name is mandatory")
    @Valid
    private String startDate;
    @NotEmpty(message = "End Date is mandatory")
    @NotNull(message = "Name is mandatory")
    @Valid
    private String endDate;
    private SubjectDTO subject;
    private TeacherDTO teacher;
    private Set<ExamDTO> exams = new HashSet<>();

    private Set<StudentDTO> students = new HashSet<>();

    public CourseDTO() {
    }

    public CourseDTO(SubjectDTO subjectDTO) {
        this.subject = subjectDTO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
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

    public Set<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDTO> students) {
        this.students = students;
    }
}
