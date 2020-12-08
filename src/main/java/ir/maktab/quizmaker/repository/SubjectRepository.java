package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alireza.d.a
 */
@Repository
public interface SubjectRepository  extends JpaRepository<Subject,Long> {
}
