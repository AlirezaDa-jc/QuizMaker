package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.dto.ExamDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface ExamMapper {
    ExamDTO sourceToDestination(Exam source);
    Exam destinationToSource(ExamDTO destination);
}
