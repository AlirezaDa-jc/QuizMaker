package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.repository.TeacherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional
public class TeacherService {

    private final PasswordEncoder passwordEncoder;

    private final TeacherRepository teacherRepository;

    private final UserService userService;

    public TeacherService(PasswordEncoder passwordEncoder, TeacherRepository teacherRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher signUp(Teacher teacher) {
        teacher.setPassword(
                this.passwordEncoder.encode(
                        teacher.getPassword()
                )
        );
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getForbiddenTeachers() {
        return teacherRepository.findAll().stream()
                .filter(teacher -> !teacher.isAllowed())
                .collect(Collectors.toList());
    }

    public Teacher findById(Long id) {
        Optional<Teacher> byId = teacherRepository.findById(id);

        return byId.get();

    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Set<Teacher> getSearchResults(String userName, String firstName, String lastName) {
        Set<Teacher> users = new HashSet<>();
        if (!userName.equals("")) {
            users.addAll(teacherRepository.findAllByUserNameContains(userName));
        }
        if (!firstName.equals("")) {
            List<Teacher> allByFirstName = teacherRepository.
                    findAllByFirstNameContains(firstName);
            users.addAll(allByFirstName);
        }
        if (!lastName.equals("")) {
            List<Teacher> allByLastName = teacherRepository.findAllByLastNameContains(lastName);
            users.addAll(allByLastName);
        }
        return users;
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

    public void edit(String firstName, String lastName, String nationalCode, Teacher teacher) throws Exception {
        try {
            if (!firstName.equals(""))
                teacher.setFirstName(firstName);
            if (!lastName.equals(""))
                teacher.setLastName(lastName);
            if (!nationalCode.equals(""))
                teacher.setNationalCode(Long.parseLong(nationalCode));
            teacherRepository.save(teacher);
        } catch (Exception ex) {
            throw new Exception("Invalid NationalCode Or Student Code");
        }
    }

    public Teacher save(Teacher teacher, Teacher tempTeacher) {
        if (teacher.getUserName().equals(tempTeacher.getUserName())) {
            teacherRepository.save(teacher);
            return teacher;
        } else if (userService.findByUserName(teacher.getUserName()) == null) {
            teacherRepository.save(teacher);
            return teacher;
        }
        throw new UniqueException("UserName Is Not Unique");
    }
}
