package ir.maktab.quizmaker.domains;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Alireza.d.a
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DescriptiveQuestion extends Question{
    private String answer;

    public DescriptiveQuestion() {
    }

    public DescriptiveQuestion(Exam exam) {
        super(exam);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
