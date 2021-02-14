package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.DescriptiveQuestionDTO;
import ir.maktab.quizmaker.dto.SubjectDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
public class DescriptiveQuestionMapperImpl implements DescriptiveQuestionMapper {
    @Override
    public DescriptiveQuestionDTO sourceToDestination(DescriptiveQuestion source) {
        if (source == null) {
            return null;
        }
        DescriptiveQuestionDTO questionDTO = new DescriptiveQuestionDTO();

        try {
            questionDTO.setAnswer(source.getAnswer());
            questionDTO.setPublic(source.getPublic());
            questionDTO.setTitle(source.getTitle());
            questionDTO.setQuestion(source.getQuestion());
            Set<SubjectDTO> subjectDTOS = source.getSubjects().stream().map(c -> new SubjectMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
            questionDTO.setSubjects(subjectDTOS);
            questionDTO.setTeacher(new TeacherMapperImpl().sourceToDestination(source.getTeacher()));
            questionDTO.setCourse(new CourseMapperImpl().sourceToDestination(source.getCourse()));
        }catch (Exception ignored){}
        return questionDTO;
    }

    @Override
    public DescriptiveQuestion destinationToSource(DescriptiveQuestionDTO destination) {
        if ( destination == null) {
            return null;
        }

        DescriptiveQuestion question = new DescriptiveQuestion();
        try {
            question.setAnswer(destination.getAnswer());
            question.setPublic(destination.getPublic());
            question.setTitle(destination.getTitle());
            question.setQuestion(destination.getQuestion());
            Set<Subject> subjects = destination.getSubjects().stream().map(c -> new SubjectMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
            question.setSubjects(subjects);
            question.setTeacher(new TeacherMapperImpl().destinationToSource(destination.getTeacher()));
        }catch (Exception ignored){}
        return question;
    }
}
