package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.dto.CourseDTO;

/**
 * @author Alireza.d.a
 */
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseDTO sourceToDestination(Course source) {
        if (source == null) {
            return null;
        }
        CourseDTO courseDTO = new CourseDTO();
        try {
            courseDTO.setName(source.getName());
            courseDTO.setStartDate(source.getStartDate());
            courseDTO.setEndDate(source.getEndDate());

        } catch (NullPointerException ignored) {
        }
        return courseDTO;
    }

    @Override
    public Course destinationToSource(CourseDTO destination) {
        if (destination == null) {
            return null;
        }
        Course course = new Course();
        try {
            course.setName(destination.getName());
            course.setStartDate(destination.getStartDate());
            course.setEndDate(destination.getEndDate());
            course.setTeacher(new TeacherMapperImpl().destinationToSource(destination.getTeacher()));

        } catch (NullPointerException ignored) {
        }
        return course;
    }
}
