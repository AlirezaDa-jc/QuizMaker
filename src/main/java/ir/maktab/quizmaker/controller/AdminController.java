package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String deleteUserFromCourse(@PathVariable Long userId, Model model, @PathVariable Long courseId) {
        User user = userService.findById(userId);
        Course tempCourse = courseService.findById(courseId);
        adminService.deleteUserFromCourse(user, tempCourse);

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
        } catch (Exception ex) {
            throw new Exception("400 , Subject's Name Must Be Unique!");
        }
    }

    @GetMapping("list-subjects")
    public String sendListSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "list-subjects";
    }

    @GetMapping("subject-courses/{id}")
    public String sendListOfSubjectCourses(@PathVariable Long id, Model model) {
        Subject subject = subjectService.findById(id);
        model.addAttribute("courses", subject.getCourses());
        return "subject-courses";
    }

    @GetMapping("add-course-to-subject/{subjectId}")
    public String sendCreateCourseForm(@PathVariable Long subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("course", new Course(subject));
        model.addAttribute("subject", subject);
        return "create-course";
    }

    @PostMapping("create-course")
    public String createCourse(@ModelAttribute Course course, Model model) throws Exception {
        try {
            courseService.save(course);
            model.addAttribute("courses", courseService.findAll());
            return "list-courses";
        } catch (Exception ex) {
            throw new Exception("500,Server Encountered An Error!");
        }
    }

    @GetMapping("home")
    public String viewHome(Model model) {
        model.addAttribute("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("students", studentService.findAll());
        int usersNotAllowedSize = teacherService.getForbiddenTeachers().size() + studentService.getForbiddenStudents().size();
        model.addAttribute("users_not_allowed", usersNotAllowedSize);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("subjects", subjectService.findAll());

        return "admin-view";
    }

    @GetMapping("edit-teacher/{id}")
    public String sendEditTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "admin-edit-teacher";
    }

    @GetMapping("edit-student/{id}")
    public String sendEditStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "admin-edit-student";
    }

    @GetMapping("delete-teacher/{id}")
    public String deleteTeacher(@PathVariable Long id, Model model) {
        teacherService.deleteById(id);
        model.addAttribute("teachers", teacherService.findAll());
        return "list-teachers";
    }

    @GetMapping("delete-student/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {
        studentService.deleteById(id);
        model.addAttribute("students", studentService.findAll());
        return "list-students";
    }

    @PostMapping("edit-teacher")
    public String editTeacher(@ModelAttribute Teacher teacher, Model model) {
        teacherService.save(teacher);
        model.addAttribute("teachers", teacherService.findAll());
        return "list-teachers";
    }

    @PostMapping("edit-student")
    public String editStudent(@ModelAttribute Student student, Model model) {
        studentService.save(student);
        model.addAttribute("students", studentService.findAll());
        return "list-students";
    }

    @GetMapping("edit-course/{id}")
    public String sendEditCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        model.addAttribute("subjects", subjectService.findAll());
        return "edit-course";
    }

    @PostMapping("edit-course")
    public String editCourse(@ModelAttribute Course course, Model model) {
        courseService.save(course);
        model.addAttribute("courses", courseService.findAll());
        return "list-courses";
    }

    @GetMapping("edit-subject/{id}")
    public String sendEditSubjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("subject",subjectService.findById(id));
        return "edit-subject";
    }

    @GetMapping("delete-subject/{id}")
    public String deleteSubject(@PathVariable Long id, Model model) {
        subjectService.deleteById(id);
        model.addAttribute("subjects", subjectService.findAll());
        return "list-subjects";
    }

    @PostMapping("edit-subject")
    public String editSubject(@ModelAttribute Subject subject,Model model) {
        subjectService.save(subject);
        model.addAttribute("subjects", subjectService.findAll());
        return "list-subjects";
    }

    @GetMapping("edit-admin")
    public String sendEditAdminForm(Model model){
        model.addAttribute("admin",SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("edited",false);
        return "edit-admin";
    }

    @PostMapping("edit-admin")
    public String editAdmin(Model model,HttpServletRequest req){
        String userName = req.getParameter("userName");
        String password = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");
        switch (userService.editUser(userName, password, newPassword, confirmPassword)) {
            case 0:
                model.addAttribute("edited",true);
                break;
            case 1:
                model.addAttribute("unmatched_error",true);
                break;
            case 2:
                model.addAttribute("current_password_error", true);
                break;
        }
        model.addAttribute("admin",SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "edit-admin";
    }
}
