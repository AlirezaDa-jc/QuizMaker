package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);
    List<User> findAllByUserNameContains(String userName);
}
