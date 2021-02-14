package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import ir.maktab.quizmaker.dto.DescriptiveQuestionDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface DescriptiveQuestionMapper {
    DescriptiveQuestionDTO sourceToDestination(DescriptiveQuestion source);
    DescriptiveQuestion destinationToSource(DescriptiveQuestionDTO destination);
}

