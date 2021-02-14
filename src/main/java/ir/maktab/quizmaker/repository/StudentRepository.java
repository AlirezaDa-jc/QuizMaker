package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Student;
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
public interface StudentRepository extends JpaRepository<Student,Long>,
        JpaSpecificationExecutor<Student> {
    List<Student> findAllByFirstNameContains(String firstName);
    List<Student> findAllByLastNameContains(String lastName);
    List<Student> findAllByUserNameContains(String userName);
    List<Student> findAllByIsAllowedFalse();

    List<Student> findAllByNationalCode(Long nationalCode);
    List<Student> findAllByStudentCode(int studentCode);

    @NotNull
    List<Student> findAll(Specification<Student> specs);
}
