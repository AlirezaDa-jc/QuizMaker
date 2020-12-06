package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface TeacherRepository  extends JpaRepository<Teacher,Long> {
    List<Teacher> findAllByFirstNameContains(String firstName);
    List<Teacher> findAllByLastNameContains(String lastName);
    List<Teacher> findAllByUserNameContains(String userName);
}
