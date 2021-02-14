package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.dto.QuestionExamScoreDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface QuestionExamScoreMapper {

    QuestionExamScoreDTO sourceToDestination(QuestionExamScore source);
    QuestionExamScore destinationToSource(QuestionExamScoreDTO destination);
}