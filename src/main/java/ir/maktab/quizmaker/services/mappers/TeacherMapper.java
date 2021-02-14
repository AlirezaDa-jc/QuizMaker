package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.dto.TeacherDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface TeacherMapper {
    TeacherDTO sourceToDestination(Teacher source);
    Teacher destinationToSource(TeacherDTO destination);
}

