package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.dto.StudentDTO;
import ir.maktab.quizmaker.dto.Student_;
import ir.maktab.quizmaker.repository.StudentRepository;
import ir.maktab.quizmaker.services.mappers.StudentMapperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Service
public class StudentService {

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final TeacherService teacherService;

    private final UserService userService;


    @Lazy
    public StudentService(PasswordEncoder passwordEncoder, StudentRepository studentRepository, TeacherService teacherService, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student signUp(Student student) throws Exception {
        if (teacherService.findAllByNationalCode(student.getNationalCode()).isEmpty() &&
                studentRepository.findAllByNationalCode(student.getNationalCode()).isEmpty()) {

            userService.save(student);
            return studentRepository.save(student);
        }
        throw new Exception("National Code In use");
    }

    public List<Student> getForbiddenStudents() {
        return studentRepository.findAll(notAllowed());
    }

    private Specification<Student> notAllowed(){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(Student_.IS_ALLOWED),false);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }


    public Student findById(Long id) {
        return studentRepository.findById(id).get();
    }

    public Set<Student> getSearchResults(String userName, String firstName, String lastName) {
        Set<Student> users = new HashSet<>();
        if (!userName.equals("")) {
            users.addAll(studentRepository.findAll(searchByUserName(userName)));
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

    private Specification<Student> searchByUserName(String userName){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(Student_.USERNAME),"%"+userName+"%");
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public void edit(String firstName, String lastName, String nationalCode, String studentCode, Student student) {
//        try {
//            if (!firstName.equals(""))
        student.setFirstName(firstName);
//            if (!lastName.equals(""))
        student.setLastName(lastName);
//            if (!nationalCode.equals(""))
        student.setNationalCode(Long.parseLong(nationalCode));
//            if (!studentCode.equals(""))
        student.setStudentCode(Integer.parseInt(studentCode));

        studentRepository.save(student);
//        } catch (Exception ex) {
//            throw new Exception("Invalid NationalCode Or Student Code");
//        }
    }

    public Student save(Student student, Student tempStudent) throws SQLIntegrityConstraintViolationException {

        if (studentRepository.findAllByNationalCode(student.getNationalCode()).size() < 2 && teacherService.findAllByNationalCode(student.getNationalCode()).size() == 0 && studentRepository.findAllByStudentCode(student.getStudentCode()).size() < 2) {
            tempStudent.setId(student.getId());
            student.setAllowed(tempStudent.isAllowed());
            userService.save(student);
            studentRepository.save(student);
            return student;
        }

        throw new java.sql.SQLIntegrityConstraintViolationException();

    }

    public List<Student> findAllByNationalCode(Long nationalCode) {
        return studentRepository.findAllByNationalCode(nationalCode);
    }

    public StudentDTO convertToDto(Student student) {
        return new StudentMapperImpl().sourceToDestination(student);
    }

    public Student convertToEntity(StudentDTO student) {
        return new StudentMapperImpl().destinationToSource(student);
    }
}
