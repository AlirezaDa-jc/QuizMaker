package ir.maktab.quizmaker.domains;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Alireza.d.a
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DescriptiveQuestion extends Question {

    public DescriptiveQuestion() {
    }

    public DescriptiveQuestion(Exam exam) {
        super(exam);
    }


}
