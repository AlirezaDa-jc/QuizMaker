package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.config.CustomUserDetailService;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.dto.StudentDTO;
import ir.maktab.quizmaker.dto.TeacherDTO;
import ir.maktab.quizmaker.services.CourseService;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.TeacherService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Alireza.d.a
 */

@Controller
@RequestMapping("home")
public class HomeController {

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final CourseService courseService;

    public HomeController(StudentService studentService, TeacherService teacherService, CourseService courseService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showMenu() {
        SimpleGrantedAuthority anonymous = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority student = new SimpleGrantedAuthority("ROLE_STUDENT");
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.contains(anonymous))
            return "home";
        else if (authorities.contains(admin))
            return "redirect:/admin/home";
        else if (authorities.contains(student))
            return "redirect:/student/home";
        else {
            return "redirect:/teacher/home";
        }
    }

    @GetMapping("/sign-up/student")
    public String sendStudentSignUpForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("role", "STUDENT");
        return "student-sign-up";
    }

    @PostMapping("/sign-up/student")
    public String studentSignUp(@Valid @ModelAttribute StudentDTO studentDTO, Model model) {
        try {
            Student student = studentService.convertToEntity(studentDTO);
            studentService.signUp(student);
        } catch (Exception ex) {
            model.addAttribute("student", new StudentDTO());
            model.addAttribute("role", "STUDENT");
            model.addAttribute("error", true);
            return "student-sign-up";
        }
        model.addAttribute("wait", true);
        return "home";
    }


    @GetMapping("/sign-up/teacher")
    public String sendSignUpForm(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        model.addAttribute("role", "TEACHER");
        return "teacher-sign-up";
    }

    @PostMapping("/sign-up/teacher")
    public String teacherSignUp(@NotNull @Valid @ModelAttribute TeacherDTO teacher, Model model) {
        try {
            teacherService.signUp(teacherService.convertToEntity(teacher));
        } catch (Exception ex) {
            model.addAttribute("teacher", new TeacherDTO());
            model.addAttribute("role", "TEACHER");
            model.addAttribute("error", true);
            return "teacher-sign-up";
        }
        model.addAttribute("wait", true);
        return "home";
    }

    @GetMapping("courses")
    public String sendListCoursesAndSubject(Model model){
        model.addAttribute("courses",courseService.findAll());
        return "home-show-courses";
    }

    @GetMapping("login_error")
    public String sendLoginError(Model model){
        if(CustomUserDetailService.getState()==1) {
            model.addAttribute("loginError", true);
            model.addAttribute("not_allowed", false);
        }else if(CustomUserDetailService.getState() == 2){
            model.addAttribute("not_allowed",true);
            model.addAttribute("loginError",false);
        }
        model.addAttribute("wait", false);
        return "home";
    }
}
