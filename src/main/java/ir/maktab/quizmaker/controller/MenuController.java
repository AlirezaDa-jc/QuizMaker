package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.domains.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
        SimpleGrantedAuthority teacher = new SimpleGrantedAuthority("ROLE_TEACHER");
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.contains(anonymous))
            return "menu";
        else if(authorities.contains(admin))
            return "redirect:/admin/students";
        //TODO
        return "Piaz";
    }

    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "menu";
    }


    @GetMapping("/sign-up/student")
    public String sendStudentSignUpForm() {
        return "student-sign-up";
    }

    @PostMapping("/sign-up/student")
    public String studentSignUp(HttpServletRequest req) {
        User user = new User();
        registerUser(req,user,"STUDENT");
        Student student = new Student();
        student.setFirstName(req.getParameter("firstName"));
        student.setLastName(req.getParameter("lastName"));
        student.setNationalCode(Long.parseLong(req.getParameter("nationalCode")));
        student.setStudentCode(Integer.parseInt(req.getParameter("studentCode")));
        student.setUser(user);
        if (studentService.save(student) != null)
            return "redirect:/student/view";
        return "menu";
    }

    private void registerUser(HttpServletRequest req, User user,String role) {

        user.setUserName(req.getParameter("userName"));
        user.setPassword(req.getParameter("password"));
        user.setRole(role);
        userService.save(user);
    }

    @GetMapping("/sign-up/teacher")
    public String sendSignUpForm() {
        return "teacher-sign-up";
    }

    @PostMapping("/sign-up/teacher")
    public String signUp(HttpServletRequest req) {
        User user = new User();
        registerUser(req, user,"TEACHER");
        Teacher teacher = new Teacher();
        teacher.setFirstName(req.getParameter("firstName"));
        teacher.setLastName(req.getParameter("lastName"));
        teacher.setNationalCode(Long.parseLong(req.getParameter("nationalCode")));
        teacher.setUser(user);
        if (teacherService.save(teacher) != null)
            return "redirect:/teacher/view";
        return "menu";
    }
}
