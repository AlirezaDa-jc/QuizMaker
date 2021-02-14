package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.CourseDTO;
import ir.maktab.quizmaker.dto.SubjectDTO;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author Alireza.d.a
 */
public class SubjectMapperImpl implements SubjectMapper{

    @Override
    public SubjectDTO sourceToDestination(Subject source) {
        if ( source == null ) {
            return null;
        }
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName( source.getName() );
        Set<CourseDTO> courseDTOS = source.getCourses().stream().map(c -> new CourseMapperImpl().sourceToDestination(c)).collect(Collectors.toSet());
        subjectDTO.setCourses(courseDTOS);


        return subjectDTO;
    }



    @Override
    public Subject destinationToSource(SubjectDTO destination) {
        if ( destination == null ) {
            return null;
        }
        Subject subject = new Subject();
        subject.setName( destination.getName() );
        Set<Course> course = destination.getCourses().stream().map(c -> new CourseMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
        subject.setCourses(course);
        Set<Question> question = destination.getQuestions().stream().map(c -> new QuestionMapperImpl().destinationToSource(c)).collect(Collectors.toSet());
        subject.setQuestions(question);

        return subject;
    }
}
