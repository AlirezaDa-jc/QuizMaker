package ir.maktab.quizmaker.services;


import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.dto.User_;
import ir.maktab.quizmaker.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Service
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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
//        return userRepository.findAllByIsAllowedFalse();
        return userRepository.findAll(notAllowed());
    }
    private Specification<User> notAllowed(){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(User_.IS_ALLOWED),false);
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

    public boolean editUser(HttpServletRequest req, Model model) {

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
        return false;
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}

