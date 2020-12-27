package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.repository.MultipleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * @author Alireza.d.a
 */
@Service
public class MultipleChoiceQuestionService {

    @Autowired
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExamService examService;

    public MultipleChoiceQuestion save(MultipleChoiceQuestion multipleChoiceQuestion){
        List<String> options = multipleChoiceQuestion.getOptions();
        options.add(multipleChoiceQuestion.getAnswer());
        options.removeAll(Collections.singleton(""));
        multipleChoiceQuestion.setOptions(options);
        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }


}
