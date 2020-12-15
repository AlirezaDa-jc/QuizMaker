package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Entity
public class Subject extends BaseEntity<Long> {
    @Column(unique = true,nullable = false)
    private String name;

    @OneToMany(mappedBy = "subject" , orphanRemoval = true)
    Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "subject" , orphanRemoval = true)
    Set<Question> questions = new HashSet<>();

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
}
