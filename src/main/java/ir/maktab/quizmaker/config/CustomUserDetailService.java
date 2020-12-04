package ir.maktab.quizmaker.config;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.exception.UserNotAllowedException;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private  UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username Or Password Is Wrong!");
        }else if(!user.isAllowed()){
            throw new UserNotAllowedException("You Are Not Allowed To Login! "+ username);
        }
        return new CustomUserDetail(user);
    }
}
