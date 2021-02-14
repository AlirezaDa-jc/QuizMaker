package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.aspect.LogRuntime;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.dto.TeacherDTO;
import ir.maktab.quizmaker.repository.TeacherRepository;
import ir.maktab.quizmaker.services.mappers.TeacherMapperImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Component
@Service
public class TeacherService {


    private final TeacherRepository teacherRepository;
    private final StudentService studentService;

    private final UserService userService;

    public TeacherService(TeacherRepository teacherRepository, StudentService studentService, UserService userService) {

        this.teacherRepository = teacherRepository;
        this.studentService = studentService;
        this.userService = userService;
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @LogRuntime
    public Teacher signUp(Teacher teacher) throws Exception {
        if(teacherRepository.findAllByNationalCode(teacher.getNationalCode()).isEmpty() &&
                studentService.findAllByNationalCode(teacher.getNationalCode()).isEmpty()) {

            userService.save(teacher);

            return teacherRepository.save(teacher);
        }
        throw new Exception("National Code In use");
    }

    public List<Teacher> getForbiddenTeachers() {
        return teacherRepository.findAllByIsAllowedFalse();
    }

    public Teacher findById(Long id) {
        Optional<Teacher> byId = teacherRepository.findById(id);
        return byId.orElse(null);
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

    public void edit(String firstName, String lastName, String nationalCode, Teacher teacher) {

        teacher.setFirstName(firstName);

        teacher.setLastName(lastName);

        teacher.setNationalCode(Long.parseLong(nationalCode));
        teacherRepository.save(teacher);

    }

    public Teacher save(Teacher teacher, Teacher tempTeacher) throws SQLIntegrityConstraintViolationException {
        if (teacherRepository.findAllByNationalCode(teacher.getNationalCode()).size() < 2
                && studentService.findAllByNationalCode(teacher.getNationalCode()).size() == 0) {
            tempTeacher.setId(teacher.getId());
            teacher.setAllowed(tempTeacher.isAllowed());
            userService.save(teacher);
            teacherRepository.save(teacher);
            return teacher;
        }
        throw new java.sql.SQLIntegrityConstraintViolationException();
    }

    public TeacherDTO convertToDto(Teacher teacher) {
        return new TeacherMapperImpl().sourceToDestination(teacher);
    }

    public Teacher convertToEntity(TeacherDTO teacherDTO) {
        return new TeacherMapperImpl().destinationToSource(teacherDTO);

    }

    public List<Teacher> findAllByNationalCode(Long nationalCode) {
        return teacherRepository.findAllByNationalCode(nationalCode);
    }
}
