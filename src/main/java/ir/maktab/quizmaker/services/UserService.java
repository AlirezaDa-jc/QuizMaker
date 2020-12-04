package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public User save(User user) {

        user.setPassword(
                this.passwordEncoder.encode(
                        user.getPassword()
                )
        );
        return userRepository.save(user);
//        System.out.println("saved user");
    }

    public boolean editPassword(String password,String userName) {
        String encodedPassword = passwordEncoder.encode(password);
        User byUserName = userRepository.findByUserName(userName);
        byUserName.setPassword(encodedPassword);
        userRepository.save(byUserName);
        return true;
    }
}
