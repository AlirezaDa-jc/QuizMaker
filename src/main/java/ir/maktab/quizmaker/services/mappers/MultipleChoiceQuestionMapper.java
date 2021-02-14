package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.dto.MultipleChoiceQuestionDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface MultipleChoiceQuestionMapper {
    MultipleChoiceQuestionDTO sourceToDestination(MultipleChoiceQuestion source);
    MultipleChoiceQuestion destinationToSource(MultipleChoiceQuestionDTO destination);
}
