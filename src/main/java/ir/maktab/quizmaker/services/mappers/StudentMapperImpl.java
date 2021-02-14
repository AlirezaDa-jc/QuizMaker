package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.dto.CourseDTO;
import ir.maktab.quizmaker.dto.StudentDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class StudentMapperImpl implements StudentMapper {
    @Override
    public StudentDTO sourceToDestination(Student source) {
        if (source == null) {
            return null;
        }
        StudentDTO studentDTO = new StudentDTO();
        try {

            studentDTO.setFirstName(source.getFirstName());
            studentDTO.setLastName(source.getLastName());
            Set<CourseDTO> courseDTOS = source.getCourses().stream().map(c -> new CourseMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
            studentDTO.setCourses(courseDTOS);
            studentDTO.setNationalCode(source.getNationalCode());
            studentDTO.setStudentCode(source.getStudentCode());
            studentDTO.setAllowed(source.isAllowed());
            studentDTO.setPassword(source.getPassword());
            studentDTO.setUserName(source.getUserName());
        } catch (Exception ignored) {
        }

        return studentDTO;


    }

    @Override
    public Student destinationToSource(StudentDTO destination) {
        if (destination == null) {
            return null;
        }
        Student studentDTO = new Student();

        try {
            studentDTO.setFirstName(destination.getFirstName());
            studentDTO.setLastName(destination.getLastName());
            studentDTO.setPassword(destination.getPassword());
            List<Exam> examDTOS = destination.getExams().stream().map(c -> new ExamMapperImpl().destinationToSource(c)).collect(Collectors.toList());
            Set<Course> courseDTOS = destination.getCourses().stream().map(c -> new CourseMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            studentDTO.setCourses(courseDTOS);
            studentDTO.setNationalCode(destination.getNationalCode());
            studentDTO.setStudentCode(destination.getStudentCode());
            studentDTO.setAllowed(destination.isAllowed());
            studentDTO.setUserName(destination.getUserName());
            studentDTO.setExams(examDTOS);
        } catch (Exception ignored) {
        }
        return studentDTO;
    }
}
