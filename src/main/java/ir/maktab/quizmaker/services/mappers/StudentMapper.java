package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.dto.StudentDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface StudentMapper {
    StudentDTO sourceToDestination(Student source);
    Student destinationToSource(StudentDTO destination);
}
