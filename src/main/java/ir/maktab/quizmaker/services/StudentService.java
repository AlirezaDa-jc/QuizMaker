package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;


    public Student save(Student student) {
       return studentRepository.save(student);
    }
}
