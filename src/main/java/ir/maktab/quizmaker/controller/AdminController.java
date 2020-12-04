package ir.maktab.quizmaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alireza.d.a
 */
//TODO Controller Or Rest?
@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/view")
    public String view(){
        return "WTF";
    }
}
