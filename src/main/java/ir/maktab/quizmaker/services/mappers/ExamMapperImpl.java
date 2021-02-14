package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.dto.ExamDTO;
import ir.maktab.quizmaker.dto.QuestionExamScoreDTO;
import ir.maktab.quizmaker.dto.StudentDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class ExamMapperImpl implements ExamMapper {
    @Override
    public ExamDTO sourceToDestination(Exam source) {
        if (source == null) {
            return null;
        }
        ExamDTO examDTO = new ExamDTO();
        try {
            examDTO.setTitle(source.getTitle());
            examDTO.setAvailable(source.isAvailable());
            examDTO.setDescription(source.getDescription());
            examDTO.setTime(source.getTime());
            examDTO.setCourse(new CourseMapperImpl().sourceToDestination(source.getCourse()));
            List<QuestionExamScoreDTO> scores = source.getScores().stream().map(c -> new QuestionExamScoreMapperImpl().sourceToDestination(c)).collect(Collectors.toList());
            examDTO.setScores(scores);
            Set<StudentDTO> studentDTOS = source.getStudents().stream().map(c -> new StudentMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
            examDTO.setStudents(studentDTOS);

        } catch (Exception ignored) {
        }

        return examDTO;
    }

    @Override
    public Exam destinationToSource(ExamDTO destination) {
        if (destination == null) {
            return null;
        }
        Exam exam = new Exam();

        try {
            exam.setTitle(destination.getTitle());
            exam.setAvailable(destination.isAvailable());
            exam.setDescription(destination.getDescription());
            exam.setTime(destination.getTime());

            List<QuestionExamScore> scores = destination.getScores().stream().map(c -> new QuestionExamScoreMapperImpl().destinationToSource(c)).collect(Collectors.toList());
            exam.setScores(scores);
            exam.setTeacher(new TeacherMapperImpl().destinationToSource(destination.getTeacher()));
            Set<Student> studentDTOS = destination.getStudents().stream().map(c -> new StudentMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            exam.setStudents(studentDTOS);
        } catch (Exception ignored) {
        }

        return exam;
    }
}
