package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getForbiddenTeachers() {
        return teacherRepository.findAll().stream()
                .filter(teacher -> !teacher.isAllowed())
                .collect(Collectors.toList());
    }

    public List<Teacher> findAllByFirstNameContains(String firstName) {
        return teacherRepository.findAllByFirstNameContains(firstName);
    }

    public List<Teacher> findAllByLastNameContains(String lastName) {
        return teacherRepository.findAllByLastNameContains(lastName);
    }

    public Teacher findById(Long id) {
        Optional<Teacher> byId = teacherRepository.findById(id);

        return byId.get();

    }

    public List<Teacher> findAllByUserNameContains(String userName){
        return teacherRepository.findAllByUserNameContains(userName);
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
}
