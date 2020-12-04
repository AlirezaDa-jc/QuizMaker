package ir.maktab.quizmaker.domains;

import ir.maktab.quizmaker.base.domains.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Entity
public class Subject extends BaseEntity<Long> {
    private String name;

    @OneToMany(mappedBy = "subject" , orphanRemoval = true)
    List<Question> questions = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
