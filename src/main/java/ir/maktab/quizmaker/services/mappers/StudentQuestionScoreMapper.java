package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.StudentQuestionScore;
import ir.maktab.quizmaker.dto.StudentQuestionScoreDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface StudentQuestionScoreMapper {
    StudentQuestionScoreDTO sourceToDestination(StudentQuestionScore source);
    StudentQuestionScore destinationToSource(StudentQuestionScoreDTO destination);
}
