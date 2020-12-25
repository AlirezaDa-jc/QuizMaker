package ir.maktab.quizmaker.domains;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Alireza.d.a
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class MultipleChoiceQuestion extends Question {

    @ElementCollection
    @CollectionTable(name="options", joinColumns=@JoinColumn(name="question_id"))
    @Column(name="options")
    private List<String> options = new LinkedList<>();
//Int Answer TODO
    public MultipleChoiceQuestion() {
    }

    public MultipleChoiceQuestion(Exam exam) {
        super(exam);
        options.add("");
        options.add("");
        options.add("");
        options.add("");
        options.add("");
        options.add("");
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void addOptions(String option) {
        options.add(option);
    }

    public void removeOption(int index) {
        options.remove(index);
    }
}
