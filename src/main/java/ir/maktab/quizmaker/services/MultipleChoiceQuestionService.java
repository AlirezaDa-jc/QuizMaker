package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.repository.MultipleChoiceQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * @author Alireza.d.a
 */
@Service
public class MultipleChoiceQuestionService {

    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    public MultipleChoiceQuestionService(MultipleChoiceQuestionRepository multipleChoiceQuestionRepository) {
        this.multipleChoiceQuestionRepository = multipleChoiceQuestionRepository;
    }

    public MultipleChoiceQuestion save(MultipleChoiceQuestion multipleChoiceQuestion){
        List<String> options = multipleChoiceQuestion.getOptions();
        options.add(multipleChoiceQuestion.getAnswer());
        options.removeAll(Collections.singleton(""));
        multipleChoiceQuestion.setOptions(options);
        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }


}
