package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    public List<Course> findAllWithoutTeacher(){
        return courseRepository.findAll().stream().filter(course -> course.getTeacher()==null).collect(Collectors.toList());
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).get();
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findAllWithoutThisStudent(Student student) {
        return courseRepository.findAll()
                .stream()
                .filter(course -> !course.getStudents()
                        .contains(student))
                .collect(Collectors.toList());
    }

    public Course addUser(Course course, User tempUser) {
        if(tempUser.getRole().equals("STUDENT")){
            course.addStudent((Student) tempUser);
        }else{
            course.setTeacher((Teacher) tempUser);
        }
        return courseRepository.save(course);
    }


}
