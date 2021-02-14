package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.dto.CourseDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface CourseMapper {

    CourseDTO sourceToDestination(Course source);
    Course destinationToSource(CourseDTO destination);

}
