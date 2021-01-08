package ir.maktab.quizmaker.config;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.exception.UserNotAllowedException;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private static int state;
    private final UserService userService;
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    public static int getState() {
        return state;
    }

    public static void setState(int state) {
        CustomUserDetailService.state = state;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            state=1;
            throw new UsernameNotFoundException("Username Or Password Is Wrong!");
        }else if(!user.isAllowed()){
            state=2;
            throw new UserNotAllowedException("You Are Not Allowed To Login! "+ username);
        }
        return new CustomUserDetail(user);
    }
}
