package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
public class MultipleChoiceQuestionDTO extends QuestionDTO{
    @NotEmpty(message = "Options Are mandatory")
    @Valid
    private List<String> options = new LinkedList<>();



    public MultipleChoiceQuestionDTO() {
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
}
