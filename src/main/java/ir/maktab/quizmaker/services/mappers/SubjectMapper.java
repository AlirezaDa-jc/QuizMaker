package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.SubjectDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface SubjectMapper {
    SubjectDTO sourceToDestination(Subject source);
    Subject destinationToSource(SubjectDTO destination);
}
