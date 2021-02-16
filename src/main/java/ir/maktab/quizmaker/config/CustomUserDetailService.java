package ir.maktab.quizmaker.config;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.exception.UserNotAllowedException;
import ir.maktab.quizmaker.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
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
/*
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("javainuse".equals(username)) {
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
 */
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
