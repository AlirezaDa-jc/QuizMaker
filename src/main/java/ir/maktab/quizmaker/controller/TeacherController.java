package ir.maktab.quizmaker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alireza.d.a
 */
@RestController
@RequestMapping("teacher")
public class TeacherController {
    @GetMapping("view")
    public String view(){
        return "view";
    }

}
