package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.services.StudentService;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Alireza.d.a
 */
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping("/sign-up")
    public String sendSignUpForm() {
        return "student-sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(HttpServletRequest req) {

        User user = new User();
        user.setUserName(req.getParameter("userName"));
        user.setPassword(req.getParameter("password"));
        user.setRole("STUDENT");
        user = userService.save(user);
        Student student = new Student();
        student.setFirstName(req.getParameter("firstName"));
        student.setLastName(req.getParameter("lastName"));
        student.setNationalCode(Long.parseLong(req.getParameter("nationalCode")));
        student.setStudentCode(Integer.parseInt(req.getParameter("studentCode")));
        student.setUser(user);
        if (studentService.save(student) != null)
            return "redirect:/student";
        //TODO Unsuccessful Registration
        return "menu";
    }

//    @RequestMapping("student")
}
