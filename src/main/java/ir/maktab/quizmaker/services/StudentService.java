package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
@RequestMapping("student")
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public Student save(Student student) {
       return studentRepository.save(student);
    }

    public List<Student> getForbiddenStudents() {
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getUser().isAllowed())
                .collect(Collectors.toList());
    }

    public List<Student> findAllByFirstNameContains(String firstName){
        return studentRepository.findAllByFirstNameContains(firstName);
    }


    public List<Student> findAllByLastNameContains(String lastName) {
        return studentRepository.findAllByLastNameContains(lastName);
    }
}
