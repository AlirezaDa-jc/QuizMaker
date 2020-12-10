package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Teacher;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.TeacherService;
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
@RequestMapping("teacher")
public class TeacherController {

    private Teacher teacher;


    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;

    @GetMapping("home")
    public String showHome(Model model) {
        teacher = (Teacher) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        model.addAttribute("teacher", teacher);
        return "teacher-home";
    }

    @GetMapping("edit-user")
    public String sendEditUserForm(Model model) {
        model.addAttribute("teacher", teacher);
        model.addAttribute("edited", false);
        model.addAttribute("unmatched_error", false);
        model.addAttribute("current_password_error", false);
        return "teacher-edit-user";
    }

    @PostMapping("edit-user")
    public String editUser(Model model, HttpServletRequest req) throws Exception {
        boolean flag = userService.editUser(req, model);
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String nationalCode = req.getParameter("nationalCode");
        if (flag)
            teacherService.edit(firstName, lastName, nationalCode, teacher);
        model.addAttribute("teacher", teacher);
        return "teacher-edit-user";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "user-exception";
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
}
