package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
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

    public boolean editUser(HttpServletRequest req, Model model) throws UniqueException {
        try {
            String userName = req.getParameter("userName");
            String password = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");
            String oldUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findByUserName(oldUserName);
            if (passwordEncoder.matches(password, user.getPassword())) {

                if (userRepository.findByUserName(userName) == null) {
                    user.setUserName(userName);

                    if (!newPassword.equals("") && confirmPassword.equals(newPassword)) {
                        user.setPassword(newPassword);
                    } else {
                        model.addAttribute("unmatched_error", true);
                    }

                    userRepository.save(user);
                    model.addAttribute("edited", true);
                    return true;

                } else {
                    model.addAttribute("duplicate_username", true);
                }

            } else {
                model.addAttribute("current_password_error", true);
            }
        } catch (Exception ex) {
            throw new UniqueException("Invalid UserName");
        }
        return false;
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}

