package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Alireza.d.a
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("view")
    public String view(){
        return "view";
    }

//    @RequestMapping("student")
}
