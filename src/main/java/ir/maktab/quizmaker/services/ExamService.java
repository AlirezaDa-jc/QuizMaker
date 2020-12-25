package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionService questionService;


    public Exam save(Exam exam) throws Exception {
        if(exam.getTime() > 420){
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

}
