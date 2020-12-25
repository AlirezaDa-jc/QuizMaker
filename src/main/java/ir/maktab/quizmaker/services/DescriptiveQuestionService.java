package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import ir.maktab.quizmaker.repository.DescriptiveQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class DescriptiveQuestionService {

    @Autowired
    private DescriptiveQuestionRepository descriptiveQuestionRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExamService examService;

    public DescriptiveQuestion save(DescriptiveQuestion descriptiveQuestion) {
        return descriptiveQuestionRepository.save(descriptiveQuestion);
    }

}
