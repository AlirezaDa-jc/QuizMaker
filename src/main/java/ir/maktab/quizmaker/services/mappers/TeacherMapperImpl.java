package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.dto.CourseDTO;
import ir.maktab.quizmaker.dto.TeacherDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class TeacherMapperImpl implements TeacherMapper {
    @Override
    public TeacherDTO sourceToDestination(Teacher source) {
        if (source == null) {
            return null;
        }
        TeacherDTO teacherDTO = new TeacherDTO();

        try {
            teacherDTO.setFirstName(source.getFirstName());
            teacherDTO.setLastName(source.getLastName());
            teacherDTO.setAllowed(source.isAllowed());
            teacherDTO.setPassword(source.getPassword());
            teacherDTO.setUserName(source.getUserName());
            teacherDTO.setNationalCode(source.getNationalCode());
            Set<CourseDTO> courseDTOS = source.getCourses().stream().map(c -> new CourseMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
            teacherDTO.setCourses(courseDTOS);

        } catch (Exception ignored) {
        }
        return teacherDTO;
    }

    @Override
    public Teacher destinationToSource(TeacherDTO destination) {
        if (destination == null) {
            return null;
        }
        Teacher teacher = new Teacher();

        try {

            teacher.setFirstName(destination.getFirstName());
            teacher.setLastName(destination.getLastName());
            teacher.setAllowed(destination.isAllowed());
            teacher.setPassword(destination.getPassword());
            teacher.setUserName(destination.getUserName());
            teacher.setNationalCode(destination.getNationalCode());
            Set<Exam> examDTOS = destination.getExams().stream().map(c -> new ExamMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            teacher.setExams(examDTOS);
            Set<Course> courseDTOS = destination.getCourses().stream().map(c -> new CourseMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            teacher.setCourses(courseDTOS);
            Set<Question> questionDTOS = destination.getQuestions().stream().map(c -> new QuestionMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            teacher.setQuestions(questionDTOS);

        } catch (Exception ignored) {
        }

        return teacher;
    }
}
