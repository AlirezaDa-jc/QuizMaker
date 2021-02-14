package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.dto.QuestionDTO;
import org.mapstruct.MapperConfig;

/**
 * @author Alireza.d.a
 */
@MapperConfig
public interface QuestionMapper {
    QuestionDTO sourceToDestination(Question source);
    Question destinationToSource(QuestionDTO destination);
}
