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
import java.util.Set;

/**
 * @author Alireza.d.a
 */
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
            Set<Teacher> users = teacherService.getSearchResults(userName,firstName,lastName);
            model.addAttribute("users", users);
        } else {
            Set<Student> users = studentService.getSearchResults(userName,firstName,lastName);
            model.addAttribute("users", users);
        }
        return "search-results";
    }

    @GetMapping("search")
    public String sendSearchList() {
        return "search";
    }

    @GetMapping("list-teachers")
    public String sendListOfTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "list-teachers";
    }

    @GetMapping("list-students")
    public String sendListOfStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "list-students";
    }


    @GetMapping("assign-course-user/{id}")
    public String sendListOfCourses(@PathVariable Long id, Model model) {
        User tempUser = userService.findById(id);
        if(tempUser.getRole().equals("TEACHER")){
            model.addAttribute("courses", courseService.findAllWithoutTeacher());
        }else {
            model.addAttribute("courses",courseService.findAllWithoutThisStudent((Student) tempUser));
        }
        model.addAttribute("userId",id);
        System.out.println();
        return "courses";
    }


    @GetMapping("assign-course-by-role/{courseId}/{userId}")
    public String assignCourse(@PathVariable Long courseId,@PathVariable Long userId, Model model) throws Exception {
        //Input Hidden
        Course course = courseService.findById(courseId);
        if(courseService.addUser(course,userService.findById(userId)) != null) {
            model.addAttribute("courses",courseService.findAll());
            return "list-courses";
        }
        throw new Exception("500 , Server Encountered An Error!");
    }

    @GetMapping("list-courses")
    public String sendListOfCourses(Model model){
        model.addAttribute("courses",courseService.findAll());
        return "list-courses";
    }
    @GetMapping("course-users/{id}")
    public String sendListOfCourseUsers(@PathVariable Long id,Model model){
        model.addAttribute("course",courseService.findById(id));
        return "course-users";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model){
        model.addAttribute("message",ex.getMessage());
        return "admin-exception";
    }


    @GetMapping("delete-course-user/{userId}/{courseId}")
    public String deleteUser(@PathVariable Long userId, Model model, @PathVariable Long courseId){
        User user = userService.findById(userId);
        Course tempCourse = courseService.findById(courseId);
        if(user.getRole().equals("TEACHER")){
            Teacher teacher = teacherService.findById(userId);
            teacher.removeCourse(tempCourse);
            teacherService.save(teacher);
            courseService.save(tempCourse);
        }else if(user.getRole().equals("STUDENT")){
            Student student = studentService.findById(userId);
            student.removeCourse(tempCourse);
            studentService.save(student);
            courseService.save(tempCourse);
        }
        model.addAttribute("course",tempCourse);
        return "course-users";
    }

    //ToDo Edit Name Student ! Teacher ! Course ! Edit!!! Front Add Login
}
