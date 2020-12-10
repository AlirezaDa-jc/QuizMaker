package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.services.CourseService;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * @author Alireza.d.a
 */

@Controller
@RequestMapping("home")
public class HomeController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;



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

    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "home";
    }


    @GetMapping("/sign-up/student")
    public String sendStudentSignUpForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("role", "STUDENT");
        return "student-sign-up";
    }

    @PostMapping("/sign-up/student")
    public String studentSignUp(@ModelAttribute Student student, Model model) {
        try {
            studentService.signUp(student);
        } catch (Exception ex) {
            model.addAttribute("student", new Student());
            model.addAttribute("role", "STUDENT");
            model.addAttribute("error", true);
            return "student-sign-up";
        }
        return "home";
    }


    @GetMapping("/sign-up/teacher")
    public String sendSignUpForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("role", "TEACHER");
        return "teacher-sign-up";
    }

    @PostMapping("/sign-up/teacher")
    public String signUp(@ModelAttribute Teacher teacher, Model model) throws Exception {
        try {
            teacherService.signUp(teacher);
        } catch (Exception ex) {
            model.addAttribute("teacher", new Teacher());
            model.addAttribute("role", "TEACHER");
            model.addAttribute("error", true);
            return "teacher-sign-up";
        }
        return "home";
    }

    @GetMapping("courses")
    public String sendListCoursesAndSubject(Model model){
        model.addAttribute("courses",courseService.findAll());
        return "home-show-courses";
    }
}
