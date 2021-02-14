package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.QuestionDTO;
import ir.maktab.quizmaker.dto.SubjectDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class QuestionMapperImpl implements QuestionMapper{
    public QuestionDTO sourceToDestination(Question source) {
        if (source == null) {
            return null;
        }
        QuestionDTO questionDTO = new QuestionDTO();

        try {
        questionDTO.setAnswer(source.getAnswer());
        questionDTO.setPublic(source.getPublic());
        questionDTO.setTitle(source.getTitle());
        questionDTO.setQuestion(source.getQuestion());
        Set<SubjectDTO> subjectDTOS = source.getSubjects().stream().map(c -> new SubjectMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
        questionDTO.setSubjects(subjectDTOS);
        questionDTO.setTeacher(new TeacherMapperImpl().sourceToDestination(source.getTeacher()));
        }catch (Exception ignored){}
        return questionDTO;
    }

    @Override
    public Question destinationToSource(QuestionDTO destination) {
        if ( destination == null) {
            return null;
        }

        Question question = new Question();
        try {
            question.setAnswer(destination.getAnswer());
            question.setPublic(destination.getPublic());
            question.setTitle(destination.getTitle());

            question.setCourse(new CourseMapperImpl().destinationToSource(destination.getCourse()));
            question.setQuestion(destination.getQuestion());
            Set<Subject> subjects = destination.getSubjects().stream().map(c -> new SubjectMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            question.setSubjects(subjects);
            question.setTeacher(new TeacherMapperImpl().destinationToSource(destination.getTeacher()));
        }catch (Exception ignored){}
        return question;
    }
}
