package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.dto.ExamDTO;
import ir.maktab.quizmaker.repository.ExamRepository;
import ir.maktab.quizmaker.services.mappers.ExamMapperImpl;
import ir.maktab.quizmaker.services.mappers.TeacherMapperImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService( ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public Exam save(Exam exam) throws Exception {
        if(exam.getTime() > 420 || exam.getTime() <= 0){
            throw new Exception("Invalid Time");
        }
        return examRepository.save(exam);
    }

    public Set<Exam> findAllByTeacher(Teacher teacher) {
        return examRepository.findAllByTeacher(teacher);
    }

    public Exam setExamAvailable(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        exam.setAvailable(true);
        return examRepository.save(exam);
    }

    public Exam setExamUnAvailable(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        exam.setAvailable(false);
        return examRepository.save(exam);
    }

    public Exam findById(Long examId) {
        return examRepository.findById(examId).get();
    }

    public void deleteById(Long examId) {
        examRepository.deleteById(examId);
    }

    public List<MultipleChoiceQuestion> findMultipleChoiceQuestions(Exam exam) {
        List<Question> questions = new LinkedList<>();
        exam.getScores().forEach((c) -> questions.add(c.getQuestion()));
        return questions.stream()
                .filter(c -> c instanceof MultipleChoiceQuestion)
                .map(c -> (MultipleChoiceQuestion) c)
                .collect(Collectors.toList());
    }



    public List<DescriptiveQuestion> findDescriptiveQuestions(Exam exam) {
        List<Question> questions = new LinkedList<>();
        exam.getScores().forEach((c) -> questions.add(c.getQuestion()));
        return questions.stream()
                .filter(c -> c instanceof DescriptiveQuestion)
                .map(c -> (DescriptiveQuestion) c)
                .collect(Collectors.toList());
    }

    public boolean checkStudentJoinedExam(Student student, Exam exam) {
        for (Student temp : exam.getStudents()) {
            if (temp.getId().equals(student.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<Question> getQuestions(List<QuestionExamScore> scores) {
        List<Question> questions = new ArrayList<>();
        scores.forEach(c -> questions.add(c.getQuestion()));

        return questions;
    }

    public void endExam(Exam exam, Student student) throws Exception {
            exam.addStudent(student);
            save(exam);
    }
    public ExamDTO convertToDto(Exam exam){

        ExamDTO examDTO = new ExamMapperImpl().sourceToDestination(exam);
        examDTO.setTeacher(new TeacherMapperImpl().sourceToDestination(exam.getTeacher()));
        return examDTO;
    }

    public Exam convertToEntity(ExamDTO examDTO) {
        Exam exam = new ExamMapperImpl().destinationToSource(examDTO);
//        exam.setTeacher(new TeacherMapperImpl().destinationToSource(examDTO.getTeacher()));
        return exam;
    }
}
