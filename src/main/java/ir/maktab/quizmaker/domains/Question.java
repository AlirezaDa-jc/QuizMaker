package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Entity
public class Question extends BaseEntity<Long> {

    @Column(unique = true)
    private String title;


    private String question;

    private Boolean isPublic = false;

    private String answer;

    @OneToMany(mappedBy = "question" , orphanRemoval = true)
    List<QuestionExamScore> scores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private Teacher teacher;


    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToMany
    @JoinTable(name = "Question_Subject",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_id")})
    private Set<Subject> subjects = new HashSet<>();

    public Question() {
    }

    public Question(Exam exam) {
        course = exam.getCourse();
        teacher = exam.getTeacher();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

//    public Set<Exam> getExams() {
//        return exams;
//    }
//
//    public void setExams(Set<Exam> exams) {
//        this.exams = exams;
//    }
//    public void addExam(Exam exam){
//        exams.add(exam);
//        exam.addQuestion(this);
//    }
    public void addSubject(Subject subject){
        subjects.add(subject);
        subject.addQuestion(this);
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
        course.addQuestion(this);
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addQuestion(QuestionExamScore questionExamScore) {
        scores.add(questionExamScore);
    }

    public List<QuestionExamScore> getScores() {
        return scores;
    }

    public void setScores(List<QuestionExamScore> scores) {
        this.scores = scores;
    }

    public void addScores(QuestionExamScore score) {
        scores.add(score);
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", isPublic=" + isPublic +
                ", answer='" + answer + '\'' +
                ", teacher=" + teacher +
                ", course=" + course +
                ", subjects=" + subjects +
                '}';
    }
}
