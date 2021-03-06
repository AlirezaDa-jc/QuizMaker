package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Entity
public class Exam extends BaseEntity<Long> {

    @Column(unique = true)
    private String title;


    private String description;


    private int time;

    private boolean isAvailable = false;


    @OneToMany(mappedBy = "exam" , orphanRemoval = true)
    List<QuestionExamScore> scores = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "Student_Exam",
            joinColumns = {@JoinColumn(name = "exam_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacherid")
    private Teacher teacher;

    public Exam() {
    }

    public Exam(Course course, Teacher teacher) {
        this.course = course;
        this.teacher = teacher;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

//    public Set<Question> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(Set<Question> questions) {
//        this.questions = questions;
//    }

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

//    public void addQuestion(Question question) {
//        questions.add(question);
//    }
    public void addQuestion(QuestionExamScore questionExamScore) {
        scores.add(questionExamScore);
    }

    public List<QuestionExamScore> getScores() {
        return scores;
    }

    public void addStudent(Student student){
        students.add(student);
        student.addExam(this);
    }

    public void setScores(List<QuestionExamScore>  scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", isAvailable=" + isAvailable +
                ", students=" + students +
                ", course=" + course +
                ", teacher=" + teacher +
                '}';
    }
}
