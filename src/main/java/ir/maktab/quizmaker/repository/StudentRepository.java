package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAllByFirstNameContainsOrLastNameContains(String firstName, String lastName);

    List<Student> findAllByFirstNameContains(String firstName);
    List<Student> findAllByLastNameContains(String lastName);
}
