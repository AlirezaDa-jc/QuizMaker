package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllByUsernameLike(String username) {
        return userRepository.findAllByUserNameContains(username);
    }

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
    }

    public boolean editPassword(String password, String userName) {
        String encodedPassword = passwordEncoder.encode(password);
        User byUserName = userRepository.findByUserName(userName);
        byUserName.setPassword(encodedPassword);
        userRepository.save(byUserName);
        return true;
    }

    public List<User> getNotAllowedUsers() {
        List<User> all = userRepository.findAll();
        return all.stream()
                .filter(user -> !user.isAllowed())
                .collect(Collectors.toList());
    }

    public User allowUser(Long id) {
        User user = userRepository.findById(id).get();
        user.setAllowed(true);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public int editUser(String userName, String password, String newPassword, String confirmPassword) {
        String oldUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUserName(oldUserName);
        if (passwordEncoder.matches(password, user.getPassword())) {
            if (confirmPassword.equals(newPassword)) {
                if (!(userName == null || userName.equals(""))) {
                    user.setUserName(userName);
                }
                user.setPassword(newPassword);
                userRepository.save(user);
                return 0;
            } else {
                return 1;
            }
        }
        return 2;
    }
}
