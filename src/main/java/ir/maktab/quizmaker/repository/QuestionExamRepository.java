package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.QuestionExamScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alireza.d.a
 */
@Repository
public interface QuestionExamRepository extends JpaRepository<QuestionExamScore,Long> {
    QuestionExamScore findDistinctByExamAndQuestion(Exam exam, Question question);
}
