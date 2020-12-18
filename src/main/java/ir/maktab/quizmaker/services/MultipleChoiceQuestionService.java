package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.repository.MultipleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class MultipleChoiceQuestionService {

    @Autowired
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    public MultipleChoiceQuestion save(MultipleChoiceQuestion multipleChoiceQuestion){
        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }


}
