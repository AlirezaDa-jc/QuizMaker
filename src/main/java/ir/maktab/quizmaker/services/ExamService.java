package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;


    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public Set<Exam> findAllByTeacher(Teacher teacher) {
        return examRepository.findAllByTeacher(teacher);
    }

    public Exam setExamAvailble(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        exam.setAvailable(true);
        return examRepository.save(exam);
    }
    public Exam setExamUnAvailble(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        exam.setAvailable(false);
        return examRepository.save(exam);
    }

    public Exam findById(Long examId) {
        return examRepository.findById(examId).get();
    }
}
