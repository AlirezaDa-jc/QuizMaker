package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.dto.CourseDTO;
import ir.maktab.quizmaker.dto.MultipleChoiceQuestionDTO;
import ir.maktab.quizmaker.repository.MultipleChoiceQuestionRepository;
import ir.maktab.quizmaker.services.mappers.CourseMapperImpl;
import ir.maktab.quizmaker.services.mappers.MultipleChoiceQuestionMapperImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * @author Alireza.d.a
 */
@Service
public class MultipleChoiceQuestionService {


    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    private final CourseService courseService;

    public MultipleChoiceQuestionService(MultipleChoiceQuestionRepository multipleChoiceQuestionRepository, CourseService courseService) {
        this.multipleChoiceQuestionRepository = multipleChoiceQuestionRepository;
        this.courseService = courseService;
    }

    public MultipleChoiceQuestion save(MultipleChoiceQuestion multipleChoiceQuestion){
        List<String> options = multipleChoiceQuestion.getOptions();
        options.add(multipleChoiceQuestion.getAnswer());
        options.removeAll(Collections.singleton(""));
        multipleChoiceQuestion.setOptions(options);
        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }
    public MultipleChoiceQuestionDTO convertToDto(MultipleChoiceQuestion multipleChoiceQuestion){
        MultipleChoiceQuestionDTO multipleChoiceQuestionDTO = new MultipleChoiceQuestionMapperImpl().sourceToDestination(multipleChoiceQuestion);
        CourseDTO courseDTO = new CourseMapperImpl().sourceToDestination(multipleChoiceQuestion.getCourse());
        multipleChoiceQuestionDTO.setCourse(courseDTO);
        return multipleChoiceQuestionDTO;
    }

    public MultipleChoiceQuestion convertToEntity(MultipleChoiceQuestionDTO multipleChoiceQuestionDTO) {
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestionMapperImpl().destinationToSource(multipleChoiceQuestionDTO);
        Course course = new CourseMapperImpl().destinationToSource(multipleChoiceQuestionDTO.getCourse());
        multipleChoiceQuestion.setCourse(course);
        return multipleChoiceQuestion;
    }

}
