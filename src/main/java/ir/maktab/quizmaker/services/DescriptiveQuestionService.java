package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import ir.maktab.quizmaker.repository.DescriptiveQuestionRepository;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class DescriptiveQuestionService {

    private final DescriptiveQuestionRepository descriptiveQuestionRepository;

    public DescriptiveQuestionService(DescriptiveQuestionRepository descriptiveQuestionRepository) {
        this.descriptiveQuestionRepository = descriptiveQuestionRepository;
    }

    public DescriptiveQuestion save(DescriptiveQuestion descriptiveQuestion) {
        return descriptiveQuestionRepository.save(descriptiveQuestion);
    }

}
