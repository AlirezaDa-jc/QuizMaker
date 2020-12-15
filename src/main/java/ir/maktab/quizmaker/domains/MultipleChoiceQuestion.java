package ir.maktab.quizmaker.domains;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Alireza.d.a
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class MultipleChoiceQuestion extends Question {

    @ElementCollection
    @CollectionTable(name="options", joinColumns=@JoinColumn(name="question_id"))
    @Column(name="options")
    private Set<String> options = new HashSet<>();

    public Set<String> getOptions() {
        return options;
    }

    public void setOptions(Set<String> options) {
        this.options = options;
    }
}
