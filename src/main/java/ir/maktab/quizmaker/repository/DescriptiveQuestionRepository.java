package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.DescriptiveQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alireza.d.a
 */
@Repository
public interface DescriptiveQuestionRepository extends JpaRepository<DescriptiveQuestion,Long> {
}
