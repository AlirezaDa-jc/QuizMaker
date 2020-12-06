package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.services.CourseService;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.TeacherService;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
//TODO Controller Or Rest?
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    private User tempUser;


    @GetMapping("allow-user")
    public String sendUsersList(Model model) {
        model.addAttribute("teachers", teacherService.getForbiddenTeachers());
        model.addAttribute("students", studentService.getForbiddenStudents());
        return "not-allowed-users";
    }

    @GetMapping("allow-user/{id}")
    public String allowUser(@PathVariable Long id, Model model) {
        userService.allowUser(id);
        model.addAttribute("teachers", teacherService.getForbiddenTeachers());
        model.addAttribute("students", studentService.getForbiddenStudents());
        return "not-allowed-users";

    }

    @GetMapping("search-results")
    public String searchResults(Model model, HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (request.getParameter("role").equals("Teacher")) {
            Set<Teacher> users = new HashSet<>();
            if (!userName.equals("")) {
                users = userService.findAllByUsernameLike(userName).stream()
                        .filter(user -> user.getRole().equals("TEACHER"))
                        .map(User::getTeacher)
                        .collect(Collectors.toSet());
            }
            if (!firstName.equals("")) {
                List<Teacher> allByFirstName = teacherService.
                        findAllByFirstNameContains(firstName);
                users.addAll(allByFirstName);
            }
            if (!lastName.equals("")) {
                List<Teacher> allByLastName = teacherService.findAllByLastNameContains(lastName);
                users.addAll(allByLastName);
            }
            model.addAttribute("users", users);
        } else {
            Set<Student> users = new HashSet<>();
            if (!userName.equals("")) {
                users = userService.findAllByUsernameLike(userName).stream()
                        .filter(user -> user.getRole().equals("STUDENT"))
                        .map(User::getStudent)
                        .collect(Collectors.toSet());
            }
            if (!firstName.equals("")) {
                List<Student> allByFirstNameLikeOrLastNameLike = studentService.findAllByFirstNameContains(firstName);
                users.addAll(allByFirstNameLikeOrLastNameLike);
            }
            if (!lastName.equals("")) {
                List<Student> allByLastName = studentService.findAllByLastNameContains(lastName);
                users.addAll(allByLastName);
            }
            model.addAttribute("users", users);
        }
        return "search-results";
    }

    @GetMapping("search")
    public String sendSearchList() {
        return "search";
    }

    @GetMapping("teachers")
    public String sendListOfTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "list-teachers";
    }

    @GetMapping("students")
    public String sendListOfStudentss(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "list-students";
    }


    @GetMapping("assign-course-user/{id}")
    public String sendListOfCourses(@PathVariable Long id, Model model) {
        tempUser = userService.findById(id);
        if(tempUser.getTeacher()!=null){
            model.addAttribute("courses", courseService.findAllWithoutTeacher());
        }else {
            model.addAttribute("courses",courseService.findAllWithoutThisStudent(tempUser.getStudent()));
        }
        return "courses";
    }

    @GetMapping("assign-course-by-role/{id}")
    public String assignCourse(@PathVariable Long id, Model model) throws Exception {
        Course course = courseService.findById(id);
        Teacher teacher = tempUser.getTeacher();
        Student student = tempUser.getStudent();
        if (teacher != null) {
            course.setTeacher(teacher);
            //TODO Home Of Admin
            courseService.save(course);
            teacher.getUser().setAllowed(true);
            teacherService.save(teacher);
            model.addAttribute("teachers", teacherService.findAll());

            return "list-teachers";
        }else if(student != null) {
            course.addStudent(student);
            courseService.save(course);
            student.getUser().setAllowed(true);
            studentService.save(student);
            model.addAttribute("students",studentService.findAll());

            return "list-students";
        }
        throw new Exception("500 , Server Encountered An Error!");

//        model.addAttribute("message","500 , Server Encountered A Problem!");
//        return "exception";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model){
        model.addAttribute("message",ex.getMessage());
        return "admin-exception";
    }
//    @GetMapping("students")
    //TODO

}
