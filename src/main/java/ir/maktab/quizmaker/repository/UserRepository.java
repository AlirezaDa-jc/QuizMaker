package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>,
        JpaSpecificationExecutor<User> {
    User findByUserName(String userName);
    List<User> findAllByUserNameContains(String userName);
    List<User> findAllByIsAllowedFalse();
    @NotNull
    List<User> findAll(Specification<User> spec);
}
