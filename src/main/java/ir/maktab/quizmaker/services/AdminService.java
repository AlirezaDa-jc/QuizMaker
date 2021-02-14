package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.dto.UserDTO;
import ir.maktab.quizmaker.services.mappers.UserMapperImpl;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class AdminService {

    private final TeacherService teacherService;

    private final StudentService studentService;

    private final CourseService courseService;


    public AdminService(TeacherService teacherService, StudentService studentService, CourseService courseService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

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

    public UserDTO convertToDto(User user){
        return new UserMapperImpl().sourceToDestination(user);
    }

    public User convertToEntity(UserDTO userDTO) {
        return new UserMapperImpl().destinationToSource(userDTO);
    }
}

