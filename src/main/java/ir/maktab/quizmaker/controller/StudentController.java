package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Alireza.d.a
 */
@Controller
@RequestMapping("student")
public class StudentController {

    private Student student;

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;


    @GetMapping("home")
    public String showHome(Model model) {
        student = (Student) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        model.addAttribute("student", student);
        return "student-home";
    }

    @GetMapping("edit-user")
    public String sendEditUserForm(Model model) {
        model.addAttribute("student", student.getUserName());
        model.addAttribute("edited", false);
        model.addAttribute("unmatched_error", false);
        model.addAttribute("current_password_error", false);
        return "student-edit-user";
    }

    @PostMapping("edit-user")
    public String editUser(Model model, HttpServletRequest req) throws Exception {
        userService.editUser(req, model);
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String nationalCode = req.getParameter("nationalCode");
        String studentCode = req.getParameter("studentCode");
        studentService.edit(firstName, lastName, nationalCode, studentCode, student);
        model.addAttribute("student", student.getUserName());
        return "student-edit-user";
    }

    @ExceptionHandler(UniqueException.class)
    public String handle(UniqueException ex, Model model) {

        model.addAttribute("message", ex.getMessage());
        return "user-exception";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(IllegalArgumentException ex, Model model) {

        model.addAttribute("message", ex.getMessage());
        return "user-exception";
    }


//    @RequestMapping("student")
}
