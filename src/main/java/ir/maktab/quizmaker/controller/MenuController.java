package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.TeacherService;
import ir.maktab.quizmaker.services.UserService;
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
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showMenu() {
        SimpleGrantedAuthority anonymous = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority student = new SimpleGrantedAuthority("ROLE_STUDENT");
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.contains(anonymous))
            return "menu";
        else if (authorities.contains(admin))
            return "redirect:/admin/view";
        else if (authorities.contains(student))
            return "redirect:/student/view";
        else {
            return "redirect:/teacher/view";
        }
    }

    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "menu";
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
            studentService.save(student);
        } catch (Exception ex) {
            model.addAttribute("student", new Student());
            model.addAttribute("role", "STUDENT");
            model.addAttribute("error", true);
            return "student-sign-up";
        }
        return "redirect:/student/view";
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
            teacherService.save(teacher);
        } catch (Exception ex) {
            model.addAttribute("teacher", new Teacher());
            model.addAttribute("role", "TEACHER");
            model.addAttribute("error", true);
            return "teacher-sign-up";
        }
        return "redirect:/teacher/view";
    }
}
