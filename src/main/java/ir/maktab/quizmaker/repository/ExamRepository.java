package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Set<Exam> findAllByTeacher(Teacher teacher);


}
