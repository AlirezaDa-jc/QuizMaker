package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.MultipleChoiceQuestionDTO;
import ir.maktab.quizmaker.dto.SubjectDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class MultipleChoiceQuestionMapperImpl implements MultipleChoiceQuestionMapper{

    @Override
    public MultipleChoiceQuestionDTO sourceToDestination(MultipleChoiceQuestion source) {
        if (source == null) {
            return null;
        }
        MultipleChoiceQuestionDTO questionDTO = new MultipleChoiceQuestionDTO();
        questionDTO.setAnswer(source.getAnswer());
        questionDTO.setPublic(source.getPublic());
        questionDTO.setTitle(source.getTitle());
        questionDTO.setOptions(source.getOptions());
        questionDTO.setQuestion(source.getQuestion());
        Set<SubjectDTO> subjectDTOS = source.getSubjects().stream().map(c -> new SubjectMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
        questionDTO.setSubjects(subjectDTOS);
        questionDTO.setTeacher(new TeacherMapperImpl().sourceToDestination(source.getTeacher()));
        questionDTO.setCourse(new CourseMapperImpl().sourceToDestination(source.getCourse()));

        return questionDTO;
    }

    @Override
    public MultipleChoiceQuestion destinationToSource(MultipleChoiceQuestionDTO destination) {
        MultipleChoiceQuestion questionDTO = new MultipleChoiceQuestion();
        questionDTO.setAnswer(destination.getAnswer());
        questionDTO.setPublic(destination.getPublic());
        questionDTO.setTitle(destination.getTitle());
        questionDTO.setOptions(destination.getOptions());
        questionDTO.setQuestion(destination.getQuestion());
        Set<Subject> subjectDTOS = destination.getSubjects().stream().map(c -> new SubjectMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
        questionDTO.setSubjects(subjectDTOS);
        questionDTO.setTeacher(new TeacherMapperImpl().destinationToSource(destination.getTeacher()));

        return questionDTO;
    }

}
