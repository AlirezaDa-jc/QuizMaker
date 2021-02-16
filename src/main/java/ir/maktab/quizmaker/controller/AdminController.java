package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final TeacherService teacherService;

    private final StudentService studentService;

    private final CourseService courseService;

    private final SubjectService subjectService;

    private final AdminService adminService;

    public AdminController(UserService userService, TeacherService teacherService, StudentService studentService,
                           CourseService courseService, SubjectService subjectService, AdminService adminService) {
        this.userService = userService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.subjectService = subjectService;
        this.adminService = adminService;
    }

    private TeacherDTO tempTeacherDTO;
    private StudentDTO tempStudentDTO;
    private SubjectDTO subjectDTO;

    @GetMapping("allow-user")
    public String sendUsersList(Model model) {
        model.addAttribute("teachers", teacherService.getForbiddenTeachers());
        model.addAttribute("students", studentService.getForbiddenStudents());
        return "admin-not-allowed-users";
    }

    @GetMapping("allow-user/{id}")
    public String allowUser(@PathVariable Long id, Model model) {
        userService.allowUser(id);
        model.addAttribute("teachers", teacherService.getForbiddenTeachers());
        model.addAttribute("students", studentService.getForbiddenStudents());
        return "admin-not-allowed-users";

    }

    @GetMapping("search-results")
    public String searchResults(Model model, @RequestParam(required = false) String userName,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam String role) {

        if (role.equals("Teacher")) {
            Set<Teacher> users = teacherService.getSearchResults(userName, firstName, lastName);
            model.addAttribute("users", users);
        } else {
            Set<Student> users = studentService.getSearchResults(userName, firstName, lastName);
            model.addAttribute("users", users);
        }
        return "admin-search-results";
    }

    @GetMapping("search")
    public String sendSearchList() {
        return "admin-search";
    }

    @GetMapping("list-teachers")
    public String sendListOfTeachers(Model model) {

        model.addAttribute("teachers", teacherService.findAll().stream()
                .filter(Teacher::isAllowed).collect(Collectors.toList()));
        return "admin-list-teachers";
    }

    @GetMapping("list-students")
    public String sendListOfStudents(Model model) {
        model.addAttribute("students", studentService.findAll().stream()
                .filter(Student::isAllowed).collect(Collectors.toList()));
        return "admin-list-students";
    }


    @GetMapping("assign-course-user/{id}")
    public String sendListOfCourses(@PathVariable Long id, Model model) {
        User tempUser = userService.findById(id);
        UserDTO userDTO = adminService.convertToDto(tempUser);
        if (userDTO.getRole().equals("TEACHER")) {
            model.addAttribute("courses", courseService.findAllWithoutTeacher());
        } else {
            model.addAttribute("courses", courseService.findAllWithoutThisStudent((Student) tempUser));
        }
        model.addAttribute("userId", id);
        return "admin-courses";
    }


    @GetMapping("assign-course-by-role/{courseId}/{userId}")
    public String assignCourse(@PathVariable Long courseId, @PathVariable Long userId, Model model) {
        Course course = courseService.findById(courseId);
        courseService.addUser(course, userService.findById(userId));
        model.addAttribute("courses", courseService.findAll());
        return "admin-list-courses";

    }

    @GetMapping("list-courses")
    public String sendListOfCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin-list-courses";
    }

    @GetMapping("course-users/{id}")
    public String sendListOfCourseUsers(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        return "admin-course-users";
    }

    @GetMapping("delete-course-user/{userId}/{courseId}")
    public String deleteUserFromCourse(@PathVariable Long userId, Model model, @PathVariable Long courseId) {
        User user = userService.findById(userId);
        Course tempCourse = courseService.findById(courseId);
        adminService.deleteUserFromCourse(user, tempCourse);

        model.addAttribute("course", tempCourse);
        return "admin-course-users";
    }

    @GetMapping("create-subject")
    public String sendFormCreateSubject(Model model) {
        model.addAttribute("subject", new SubjectDTO());
        return "admin-create-subject";
    }

    @PostMapping("create-subject")
    public String createSubject(@NotNull @Valid @ModelAttribute SubjectDTO subjectDto, Model model) throws UniqueException {
        try {
            Subject subject = subjectService.convertToEntity(subjectDto);
            subjectService.save(subject);
            model.addAttribute("subjects", subjectService.findAll());
            return "admin-list-subjects";
        } catch (Exception ex) {
            throw new UniqueException("400 , Subject's Name Must Be Unique!");
        }
    }

    @GetMapping("list-subjects")
    public String sendListSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "admin-list-subjects";
    }

    @GetMapping("subject-courses/{id}")
    public String sendListOfSubjectCourses(@PathVariable Long id, Model model) {
        Subject subject = subjectService.findById(id);
        model.addAttribute("courses", subject.getCourses());
        return "admin-subject-courses";
    }

    @GetMapping("add-course-to-subject/{subjectId}")
    public String sendCreateCourseForm(@PathVariable Long subjectId, Model model) {
        Subject subject = subjectService.findById(subjectId);
        subjectDTO = subjectService.convertToDto(subject);
        CourseDTO courseDTO = new CourseDTO();
        model.addAttribute("course", courseDTO);
        model.addAttribute("subject", subjectDTO);
        return "admin-create-course";
    }

    @PostMapping("create-course")
    public String createCourse(@NotNull @Valid @ModelAttribute CourseDTO courseDTO, Model model) {
        courseDTO.setSubject(subjectDTO);
        Course course = courseService.convertToEntity(courseDTO);
        if (courseService.checkDate(course)) {
            model.addAttribute("date_error", true);
            model.addAttribute("course", courseDTO);
            model.addAttribute("subject", courseDTO.getSubject());
            return "admin-create-course";
        }
        courseService.save(course);
        model.addAttribute("courses", courseService.findAll());
        subjectDTO = null;
        return "admin-list-courses";
    }

    @GetMapping("home")
    public String viewHome(Model model, @NotNull HttpSession session) {
        model.addAttribute("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("students", studentService.findAll());
        int usersNotAllowedSize = teacherService.getForbiddenTeachers().size() + studentService.getForbiddenStudents().size();
        model.addAttribute("users_not_allowed", usersNotAllowedSize);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        session.setAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());


        return "admin-home";
    }

    @GetMapping("edit-teacher/{id}")
    public String sendEditTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        TeacherDTO teacherDTO = teacherService.convertToDto(teacher);
        tempTeacherDTO = teacherDTO;
        model.addAttribute("teacher", teacherDTO);
        model.addAttribute("id", id);
        return "admin-edit-teacher";
    }

    @GetMapping("edit-student/{id}")
    public String sendEditStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        StudentDTO studentDTO = studentService.convertToDto(student);
        tempStudentDTO = studentDTO;
        model.addAttribute("student", studentDTO);
        model.addAttribute("id", id);

        return "admin-edit-student";
    }

    @GetMapping("delete-teacher/{id}")
    public String deleteTeacher(@PathVariable Long id, Model model) {
        teacherService.deleteById(id);
        model.addAttribute("teachers", teacherService.findAll());
        return "admin-list-teachers";
    }

    @GetMapping("delete-student/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {
        studentService.deleteById(id);
        model.addAttribute("students", studentService.findAll());
        return "admin-list-students";
    }

    @PostMapping("edit-teacher")
    public String editTeacher(@NotNull @Valid @ModelAttribute TeacherDTO teacherDTO,
                              Model model,
                              @RequestParam Long id,
                              @RequestParam(required = false) String password
                              ) throws SQLIntegrityConstraintViolationException {
        Teacher teacher = teacherService.convertToEntity(teacherDTO);
        Teacher tempTeacher = teacherService.convertToEntity(tempTeacherDTO);
        teacher.setId(id);
        if (password != null && !password.equals("")) {
            teacher.setPassword(password);
        } else {
            teacher.setPassword(tempTeacher.getPassword());
        }
        teacherService.save(teacher, tempTeacher);
        model.addAttribute("teachers", teacherService.findAll());
        return "admin-list-teachers";
    }

    @PostMapping("edit-student")
    public String editStudent(@NotNull @Valid @ModelAttribute StudentDTO studentDTO, Model model, @RequestParam Long id,
                              @RequestParam(required = false) String password) throws SQLIntegrityConstraintViolationException {
        Student student = studentService.convertToEntity(studentDTO);
        Student tempStudent = studentService.convertToEntity(tempStudentDTO);

        student.setId(id);
        if (password != null && !password.equals("")) {
            student.setPassword(password);
        } else {
            student.setPassword(tempStudent.getPassword());
        }
        studentService.save(student, tempStudent);
        model.addAttribute("students", studentService.findAll());
        return "admin-list-students";
    }

    @GetMapping("edit-course/{id}")
    public String sendEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        CourseDTO courseDTO = courseService.convertToDto(course);
        subjectDTO = courseDTO.getSubject();
        courseDTO.setSubject(null);
        model.addAttribute("course", courseDTO);
        model.addAttribute("subject", subjectDTO);
        model.addAttribute("id", id);
        return "admin-edit-course";
    }

    @PostMapping("edit-course")
    public String editCourse(@NotNull @Valid @ModelAttribute CourseDTO courseDTO, Model model, @RequestParam Long id) {
        courseDTO.setSubject(subjectDTO);
        Course course = courseService.convertToEntity(courseDTO);
        if (!courseService.checkDate(course)) {
            course.setId(id);
            courseService.save(course);
            model.addAttribute("courses", courseService.findAll());
            return "admin-list-courses";
        }
        model.addAttribute("date_error", true);
        model.addAttribute("course", courseService.convertToDto(course));
        model.addAttribute("subject", courseDTO.getSubject());
        model.addAttribute("id", id);
        return "admin-edit-course";
    }

    @GetMapping("delete-course/{courseId}")
    public String deleteCourse(@PathVariable Long courseId, Model model) {
        courseService.deleteById(courseId);
        model.addAttribute("courses", courseService.findAll());
        return "admin-list-courses";
    }

    @GetMapping("edit-subject/{id}")
    public String sendEditSubjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("subject", subjectService.convertToDto(subjectService.findById(id)));
        model.addAttribute("id", id);
        return "admin-edit-subject";
    }

    @GetMapping("delete-subject/{id}")
    public String deleteSubject(@PathVariable Long id, Model model) {
        subjectService.deleteById(id);
        model.addAttribute("subjects", subjectService.findAll());
        return "admin-list-subjects";
    }

    @PostMapping("edit-subject")
    public String editSubject(@NotNull @Valid @ModelAttribute SubjectDTO subjectDTO, Model model,@RequestParam Long id) {
        Subject subject = subjectService.convertToEntity(subjectDTO);
        subject.setId(id);
        subjectService.save(subject);
        model.addAttribute("subjects", subjectService.findAll());
        return "admin-list-subjects";
    }

    @GetMapping("edit-admin")
    public String sendEditAdminForm(Model model) {
        model.addAttribute("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("edited", false);
        return "admin-edit-user";
    }

    @PostMapping("edit-admin")
    public String editAdmin(Model model, @NotNull HttpServletRequest req) throws ResourceNotFoundException {
        userService.editUser(req, model);
        model.addAttribute("admin", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "admin-edit-user";
    }

//    @ExceptionHandler(UniqueException.class)
//    public String handle(UniqueException ex, Model model) {
//
//        model.addAttribute("message", ex.getMessage());
//        return "admin-exception";
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public String handle(IllegalArgumentException ex, Model model) {
//
//        model.addAttribute("message", ex.getMessage());
//        return "admin-exception";
//    }
}
