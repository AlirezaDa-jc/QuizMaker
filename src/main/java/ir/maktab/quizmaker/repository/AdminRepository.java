package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alireza.d.a
 */
@Repository
public interface AdminRepository extends JpaRepository<User,Long> {

}
