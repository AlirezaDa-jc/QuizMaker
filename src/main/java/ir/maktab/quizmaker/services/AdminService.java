package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional

public class AdminService {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    public void deleteUserFromCourse(User user, Course tempCourse) {
        if (user.getRole().equals("TEACHER")) {
            Teacher teacher = (Teacher) user;
            teacher.removeCourse(tempCourse);
            teacherService.save(teacher);
        } else if (user.getRole().equals("STUDENT")) {
            Student student = (Student) user;
            student.removeCourse(tempCourse);
            studentService.save(student);
        }
        courseService.save(tempCourse);
    }
}
