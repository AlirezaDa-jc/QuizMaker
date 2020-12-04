package ir.maktab.quizmaker.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

/**
 * @author Alireza.d.a
 */

@Controller
public class MenuController {
    @GetMapping("menu")
    public String showMenu() throws Exception {
        SimpleGrantedAuthority anonymous = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority student = new SimpleGrantedAuthority("ROLE_STUDENT");
        SimpleGrantedAuthority teacher = new SimpleGrantedAuthority("ROLE_TEACHER");
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.contains(anonymous))
            return "menu";
        else if(authorities.contains(admin))
            return "redirect:/admin/view";
        //TODO
        return "Piaz";
    }

    @GetMapping("student")
    public String showStudent() {
        return "menu";
    }
}
