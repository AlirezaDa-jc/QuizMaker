package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student signUp(Student student) {
        student.setPassword(
                this.passwordEncoder.encode(
                        student.getPassword()
                )
        );
        return studentRepository.save(student);
    }

    public List<Student> getForbiddenStudents() {
        return studentRepository.findAll()
                .stream()
                .filter(student -> !student.isAllowed())
                .collect(Collectors.toList());
    }

    public List<Student> findAllByFirstNameContains(String firstName) {
        return studentRepository.findAllByFirstNameContains(firstName);
    }


    public List<Student> findAllByLastNameContains(String lastName) {
        return studentRepository.findAllByLastNameContains(lastName);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

//    public void saveO(Student student) {
//        student.getUser().setAllowed(true);
//        studentRepository.save(student);
//    }

    public Student findById(Long id) {
        return studentRepository.findById(id).get();
    }

    public List<Student> findAllByUserNameContains(String userName) {
        return studentRepository.findAllByUserNameContains(userName);
    }

    public Set<Student> getSearchResults(String userName, String firstName, String lastName) {
        Set<Student> users = new HashSet<>();
        if (!userName.equals("")) {
            users.addAll(studentRepository.findAllByUserNameContains(userName));
        }
        if (!firstName.equals("")) {
            List<Student> allByFirstNameLikeOrLastNameLike = studentRepository.findAllByFirstNameContains(firstName);
            users.addAll(allByFirstNameLikeOrLastNameLike);
        }
        if (!lastName.equals("")) {
            List<Student> allByLastName = studentRepository.findAllByLastNameContains(lastName);
            users.addAll(allByLastName);
        }
        return users;
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public void edit(String firstName, String lastName, String nationalCode, String studentCode, Student student) throws Exception {
        try {
            if (!firstName.equals(""))
                student.setFirstName(firstName);
            if (!lastName.equals(""))
                student.setLastName(lastName);
            if (!nationalCode.equals(""))
                student.setNationalCode(Long.parseLong(nationalCode));
            if (!studentCode.equals(""))
                student.setStudentCode(Integer.parseInt(studentCode));
            studentRepository.save(student);
        } catch (Exception ex) {
            throw new Exception("Invalid NationalCode Or Student Code");
        }
    }

    public Student save(Student student, Student tempStudent) {
        if (student.getUserName().equals(tempStudent.getUserName())) {
            studentRepository.save(student);
            return student;
        } else if (userService.findByUserName(student.getUserName()) == null) {
            studentRepository.save(student);
            return student;
        }
        throw new UniqueException("Student Username Should Be Unique");
    }
}
