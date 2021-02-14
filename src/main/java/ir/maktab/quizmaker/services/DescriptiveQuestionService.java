package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import ir.maktab.quizmaker.dto.DescriptiveQuestionDTO;
import ir.maktab.quizmaker.repository.DescriptiveQuestionRepository;
import ir.maktab.quizmaker.services.mappers.CourseMapperImpl;
import ir.maktab.quizmaker.services.mappers.DescriptiveQuestionMapperImpl;
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
    public DescriptiveQuestionDTO convertToDto(DescriptiveQuestion descriptiveQuestion){
        DescriptiveQuestionDTO descriptiveQuestionDTO =
                new DescriptiveQuestionMapperImpl().sourceToDestination(descriptiveQuestion);
        descriptiveQuestionDTO.setCourse(new CourseMapperImpl().sourceToDestination(descriptiveQuestion.getCourse()));
        return descriptiveQuestionDTO;

    }

    public DescriptiveQuestion convertToEntity(DescriptiveQuestionDTO descriptiveQuestionDTO) {
        DescriptiveQuestion descriptiveQuestion =
                new DescriptiveQuestionMapperImpl().destinationToSource(descriptiveQuestionDTO);
        descriptiveQuestion.setCourse(new CourseMapperImpl().destinationToSource(descriptiveQuestionDTO.getCourse()));
        return descriptiveQuestion;
    }
}
