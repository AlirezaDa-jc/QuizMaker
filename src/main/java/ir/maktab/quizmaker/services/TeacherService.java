package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getForbiddenTeachers() {
        return teacherRepository.findAll().stream()
                .filter(teacher -> !teacher.getUser().isAllowed())
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

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
}
