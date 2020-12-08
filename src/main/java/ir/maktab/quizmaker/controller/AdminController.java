package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SubjectService subjectService;


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
            Set<Teacher> users = teacherService.getSearchResults(userName, firstName, lastName);
            model.addAttribute("users", users);
        } else {
            Set<Student> users = studentService.getSearchResults(userName, firstName, lastName);
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
        if (tempUser.getRole().equals("TEACHER")) {
            model.addAttribute("courses", courseService.findAllWithoutTeacher());
        } else {
            model.addAttribute("courses", courseService.findAllWithoutThisStudent((Student) tempUser));
        }
        model.addAttribute("userId", id);
        return "courses";
    }


    @GetMapping("assign-course-by-role/{courseId}/{userId}")
    public String assignCourse(@PathVariable Long courseId, @PathVariable Long userId, Model model) throws Exception {
        //Input Hidden
        Course course = courseService.findById(courseId);
        if (courseService.addUser(course, userService.findById(userId)) != null) {
            model.addAttribute("courses", courseService.findAll());
            return "list-courses";
        }
        throw new Exception("500 , Server Encountered An Error!");
    }

    @GetMapping("list-courses")
    public String sendListOfCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "list-courses";
    }

    @GetMapping("course-users/{id}")
    public String sendListOfCourseUsers(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        return "course-users";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "admin-exception";
    }


    @GetMapping("delete-course-user/{userId}/{courseId}")
    public String deleteUser(@PathVariable Long userId, Model model, @PathVariable Long courseId) {
        User user = userService.findById(userId);
        Course tempCourse = courseService.findById(courseId);
        if (user.getRole().equals("TEACHER")) {
            Teacher teacher = teacherService.findById(userId);
            teacher.removeCourse(tempCourse);
            teacherService.save(teacher);
            courseService.save(tempCourse);
        } else if (user.getRole().equals("STUDENT")) {
            Student student = studentService.findById(userId);
            student.removeCourse(tempCourse);
            studentService.save(student);
            courseService.save(tempCourse);
        }
        model.addAttribute("course", tempCourse);
        return "course-users";
    }

    @GetMapping("create-subject")
    public String sendFormCreateSubject(Model model) {
        model.addAttribute("subject", new Subject());
        return "create-subject";
    }

    @PostMapping("create-subject")
    public String createSubject(@ModelAttribute Subject subject, Model model) throws Exception {
        try {
            subjectService.save(subject);
            model.addAttribute("subjects", subjectService.findAll());
            return "list-subjects";
        }catch (Exception ex) {
            throw new Exception("400 , Subject's Name Must Be Unique!");
        }
    }

    @GetMapping("list-subjects")
    public String sendListSubjects(Model model){
        model.addAttribute("subjects", subjectService.findAll());
        return "list-subjects";
    }

    @GetMapping("subject-courses/{id}")
    public String sendListOfSubjectCourses(@PathVariable Long id,Model model){
        Subject subject= subjectService.findById(id);
        model.addAttribute("courses",subject.getCourses());
        return "subject-courses";
    }

    @GetMapping("add-course-to-subject/{subjectId}")
    public String sendCreateCourseForm(@PathVariable Long subjectId,Model model){
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("course",new Course(subject));
        model.addAttribute("subject",subject);
        return "create-course";
    }

    @PostMapping("create-course")
    public String createCourse(@ModelAttribute Course course,Model model) throws Exception {
        try {
            courseService.save(course);
            model.addAttribute("courses", courseService.findAll());
            return "list-courses";
        }catch (Exception ex) {
            throw new Exception("500,Server Encountered An Error!");
        }
    }

    @GetMapping("home")
    public String viewHome(Model model) {
        model.addAttribute("admin",SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("students",studentService.findAll());
        int usersNotAllowedSize = teacherService.getForbiddenTeachers().size() + studentService.getForbiddenStudents().size();
        model.addAttribute("users_not_allowed",usersNotAllowedSize);
        model.addAttribute("courses",courseService.findAll());
        model.addAttribute("subjects",subjectService.findAll());

        return "admin-view";
    }
    //ToDo Edit Name Student ! Teacher ! Course ! Edit!!! Front Add Login
}
